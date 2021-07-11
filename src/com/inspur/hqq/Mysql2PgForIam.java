package com.inspur.hqq;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Logger;


public class Mysql2PgForIam {

	private static Logger log = Logger.getLogger(Mysql2PgForIam.class);
	private static String mysqlIP;
	private static String mysqlPort;
	private static String mysqlUser;
	private static String mysqlPassword;
	
	private static String pgIP;
	private static String pgPort;
	private static String pgUser;
	private static String pgPassword;
	
	private static Connection mysqlConnection;
	private static Connection pgConnection;
	
	//不需要迁的表
	//"iam_log","databasechangeloglock","iam_server","iam_product_version" //没有迁的必要
    //"security_config","security_config_def","security_config_group",//环境上还没有
	
	//需要迁的表
	private static String tableNames[] = 
		   {"databasechangelog","iam_action",
		    "iam_exec",  "iam_policy",
			"iam_policy_subjects",
			"iam_proxy","iam_proxy_policy",
			"iam_proxy_service_client","iam_proxy_service_inst_client",
			"iam_proxy_service_inst_secret","iam_proxy_service_secret",
			"iam_resource_type","iam_resource_type_attribute",
			"iam_secret","iam_service",
			"iam_service_attribute","iam_service_open_policy",
			"iam_token_exchange_record","role_url",
			"url"};
	
	private static void initIamStru() throws SQLException{
		executeSqlFile("iam-stru.sql");
	}
	private static void fixDatabasechangelog(){
		//置空md5sum，启动iam-apiserver会重新生成新的。
		executeSql("update databasechangelog set md5sum=null");
		
		//changelog路径变更mysql-->master
		executeSql("update databasechangelog set filename=replace(filename,'db/changelog/iam/mysql/','db/changelog/iam/master/')");
		
		//去除BOOT-INF/classes/前缀
		executeSql("update databasechangelog set filename=replace(filename,'BOOT-INF/classes/db','db')");
	}
	//md5sum 值为null
	//测试 新增changeset 是否正常
	private static void appendDdlDatabasechangelog(){
		executeSql("INSERT INTO databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id ) VALUES ( 'iam_service_open_policy-20210324', 'huangqiqing', 'db/changelog/iam/master/t202103/20210324-ddl-adduniqueconstraint-hqq.xml', '2021-04-25 01:21:25.004247', 127, 'EXECUTED', '8:8f00dbe3c27a41d2de6fed98f5824dfa', 'addUniqueConstraint constraintName=iam_service_open_policy_unique_key01, tableName=iam_service_open_policy', '', null, '3.5.4', null, null, '9313651516' )");
		executeSql("INSERT INTO databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id ) VALUES ( 'create_security_config_group', 'hqq', 'db/changelog/iam/master/t202105/20210525-ddl-iam-security-config-hqq.xml', '2021-05-28 06:20:58.873763', 255, 'EXECUTED', '8:80623acd71209d44335125ff5dc2c596', 'createTable tableName=security_config_group; createTable tableName=security_config_def; createTable tableName=security_config', '', null, '3.5.4', null, null, '2182858077' )");
	}
	private static void executeSql(String sql){
		PreparedStatement psPG = null;
		try {
			log.debug(sql);
			psPG = pgConnection.prepareStatement(sql);
			int affline = psPG.executeUpdate();
			log.debug(affline);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				psPG.close();
			} catch (Exception e2) {
			}
		}
	}
	private static void executeSqlFile(String fileName){
		boolean multiComment = false;
		StringBuilder sql = new StringBuilder();
		Scanner s = new Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName),"utf-8");
		while(s.hasNextLine()){
			String line = s.nextLine();
			if(line.startsWith("/*")){
				multiComment = true;
				continue;
			}
			if(line.startsWith("*/")){
				multiComment = false;
				continue;
			}
			if(line.startsWith("--")){
				continue;
			}
			if(multiComment){
				continue;
			}
			if(line != null){
				sql.append(line);
			}
			if(line.endsWith(";")){//一条完整sql
				String strsql = sql.toString();
				sql = new StringBuilder();
				PreparedStatement psPG = null;
				try {
					log.debug(strsql);
					psPG = pgConnection.prepareStatement(strsql);
					int affline = psPG.executeUpdate();
					log.debug(affline);
				} catch (SQLException e) {
					e.printStackTrace();
				}finally{
					try {
						psPG.close();
					} catch (Exception e2) {
					}
				}
			}
		}	
	}
	private static void migrate(String tableName){
		log.debug("migrate table "+tableName);
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement psPG = null;
		ResultSet rsPG = null;
		try {
			int boolColumn = 0;
			ps = mysqlConnection.prepareStatement("select * from "+tableName);
			rs = ps.executeQuery();
			ResultSetMetaData rm = rs.getMetaData();
			int col_number = rm.getColumnCount();
			StringBuffer insertInit = new StringBuffer();
		    {
				insertInit.append("INSERT INTO " + tableName + " (");
				for (int j = 1; j <= col_number - 1; j++) {
					insertInit.append(rm.getColumnName(j) + ", ");
					if(tableName.equalsIgnoreCase("iam_secret") && rm.getColumnName(j).equalsIgnoreCase("enabled")){
						boolColumn = j;
					}	
					if(tableName.equalsIgnoreCase("url") && rm.getColumnName(j).equalsIgnoreCase("enable")){
						boolColumn = j;
					}	
				}
				insertInit.append(rm.getColumnName(col_number)+ " ) VALUES ( ");
			}
			while (rs.next()) {
				StringBuffer insertSql = new StringBuffer();
				insertSql.append(insertInit);
				for (int k = 1; k <= col_number - 1; k++) {
					int type = rm.getColumnType(k);
					String col = rs.getString(k);
					if (col == null || "(null)".equals(col)) {
						insertSql.append("null, ");
					} else {
						if(isChar(type)){
							col = col.replace("'", "''");
							insertSql.append("'" + col + "', ");
						} else {
							if((tableName.equalsIgnoreCase("url") || tableName.equalsIgnoreCase("iam_secret")) 
									&& k == boolColumn){
								insertSql.append((col.equals("0")?false:true) + ", ");
							}else{
								insertSql.append(col + ", ");
							}
						}
					}
				}
				// 对于最后一列的特殊处理
				int type = rm.getColumnType(col_number);
				String col = rs.getString(col_number);
				if (col == null || "(null)".equals(col)) {
					insertSql.append("null );");
				} else {
					if(isChar(type)){
						col = col.replace("'", "''");
						insertSql.append("'" + col + "' );");
					} else {
						insertSql.append(col + " );");
					}
				}
				String sql = insertSql.toString();
				log.debug(sql);
			
				//同一个表，部分行插入出错，不影响其他行
				try {
					psPG = pgConnection.prepareStatement(sql);
					int line = psPG.executeUpdate();
					log.debug("executed,affect lines "+line);
				} catch (Exception e) {
					log.error(sql, e);
					System.err.println(sql);
				}finally{
					closeResultSetAndPreparedStatement(null, psPG);
				}
			}
			} catch (Exception e) {
				System.err.println(tableName);
				e.printStackTrace();
			} finally {
				closeResultSetAndPreparedStatement(rs, ps);
				closeResultSetAndPreparedStatement(rsPG, psPG);
			}
		}
	/**
	 * 初始化数据库配置信息
	 */
	public static void initConfig() {
		Properties p = new Properties();
		InputStream inputStream;
		try {
			inputStream = new FileInputStream("db.properties");
		} catch (FileNotFoundException e1) {
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
		}
		try {
			p.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		mysqlIP = p.getProperty("mysqlIP");
		mysqlPort = p.getProperty("mysqlPort");
		mysqlUser = p.getProperty("mysqlUser");
		mysqlPassword = p.getProperty("mysqlPassword");
		
		pgIP = p.getProperty("pgIP");
		pgPort = p.getProperty("pgPort");
		pgUser = p.getProperty("pgUser");
		pgPassword = p.getProperty("pgPassword");
	}
	/**
	 * 初始化mysql链接
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void initMysqlConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		mysqlConnection = DriverManager.getConnection("jdbc:mysql://"+mysqlIP+":"+mysqlPort+"/iam", mysqlUser, mysqlPassword);
	}  
	/**
	 * 初始化pg链接
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void initPgConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		pgConnection = DriverManager.getConnection("jdbc:postgresql://"+pgIP+":"+pgPort+"/iam", pgUser, pgPassword);
	}  

	/**
	 * 检查是否是数据库的字符类型
	 * @param type
	 * @return boolean
	 */
	public static boolean isChar(int type){
		if (type == Types.BIGINT || type == Types.DECIMAL
				|| type == Types.DOUBLE || type == Types.FLOAT
				|| type == Types.INTEGER || type == Types.NUMERIC
				|| type == Types.SMALLINT 
				|| type == Types.BIT 
				|| type == Types.NULL) {
			return false; 
		}else{
			return true;
		}
	}
	/*
	 * 关闭数据库连接
	 * 关闭顺序：ResultSet——PreparedStatement
	 */
	public static  void closeResultSetAndPreparedStatement(ResultSet rs,PreparedStatement ps){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {}
		}
		if(ps!=null){
			try {
				ps.close();
			} catch (SQLException e) {}
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		//加载源数据源、目标数据源配置信息
		initConfig();
		System.out.println("1.加载数据源配置完成");
		
		//初始化到源MySQL的链接
		initMysqlConnection();
		System.out.println("2.初始化到源MySQL的数据库链接完成");
		
		//初始化到目标PG的链接
		initPgConnection();
		System.out.println("3.初始化到目标PG的数据库链接完成");
		
		//初始化表结构（最新3.5表结构、iam-apiserver的所有数据表，含databasechangelog相关表）
		initIamStru();
		System.out.println("4.数据库表结构初始化完成");
		
		//表数据迁移
		System.out.println("5.开始迁移业务表数据");
		for (String tableName : tableNames) {
			System.out.println("  migrate table "+tableName);
			migrate(tableName);
			System.out.println("  migrate table "+tableName+" 完成");
		}
		System.out.println("6.业务表数据迁移完成");
		
		//修正databasechangelog
		//1、设置md5sum=null；
		//2、replace(filename,'db/changelog/iam/mysql/','db/changelog/iam/master/'）
		//3、去除BOOT-INF/classes/前缀
		fixDatabasechangelog();
		System.out.println("7.fixDatabasechangelog完成");
		
		//追加项目现场没有执行的ddl变更的changelog(3.1.2至3.5)
		appendDdlDatabasechangelog();
		System.out.println("8.append 3.1.2-->3.5 ddl changelog");
		
		System.out.println("9.数据迁移完成，可以启动iam-apiserver应用了");
	}
}

