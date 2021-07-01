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

public class Mysql2PgForIadon {

	private static Logger log = Logger.getLogger(Mysql2PgForIadon.class);
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
	
	private static String tableNames[] = { "gorp_migrations", "ladon_action", "ladon_policy",
			"ladon_policy_action_rel", "ladon_policy_permission", "ladon_policy_resource",
			"ladon_policy_resource_rel","ladon_policy_subject","ladon_policy_subject_rel"
			,"ladon_resource","ladon_subject"};
	static{
		try {
			initConfig();
			initMysqlConnection();
			initPgConnection();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws SQLException {
		
		//初始化表结构
		initLadonStru();
		
		//逐个表迁移
		for (String tableName : tableNames) {
			migrate(tableName);
		}
		
		//追加索引
		initLadonIndex();
	}
	
	private static void initLadonStru() throws SQLException{
		executeSqlFile("ladon-stru.sql");
	}
	private static void initLadonIndex() throws SQLException{
		executeSqlFile("ladon-stru-index.sql");
	}
	
	private static void executeSqlFile(String fileName) throws SQLException{
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
					psPG = pgConnection.prepareStatement(strsql);
					int affline = psPG.executeUpdate();
					System.out.println(strsql);
					System.out.println(affline);
				} catch (SQLException e) {
					throw e;
				}finally{
					psPG.close();
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
					if ((tableName.equalsIgnoreCase("ladon_action") 
							|| tableName.equalsIgnoreCase("ladon_resource")
							|| tableName.equalsIgnoreCase("ladon_subject"))
							&& rm.getColumnName(j).equalsIgnoreCase("has_regex")) {
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
							if((tableName.equalsIgnoreCase("ladon_action") 
									|| tableName.equalsIgnoreCase("ladon_resource")
									|| tableName.equalsIgnoreCase("ladon_subject")) && k == boolColumn){
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
				//将insertsql在pg中执行
				log.debug(insertSql);
				psPG = pgConnection.prepareStatement(insertSql.toString());
				int line = psPG.executeUpdate();
				log.debug("executed,affect lines "+line);
			}
			} catch (Exception e) {
				System.err.println(tableName);
				e.printStackTrace();
			} finally {
				closeResultSetAndPreparedStatement(rs, ps);
				closeResultSetAndPreparedStatement(rsPG, psPG);
			}
		System.out.println("finish "+tableName);
		}
	/**
	 * 初始化数据库配置信息
	 * @throws FileNotFoundException 
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
		mysqlConnection = DriverManager.getConnection("jdbc:mysql://"+mysqlIP+":"+mysqlPort+"/ladon", mysqlUser, mysqlPassword);
	}  
	/**
	 * 初始化pg链接
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void initPgConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		pgConnection = DriverManager.getConnection("jdbc:postgresql://"+pgIP+":"+pgPort+"/ladon", pgUser, pgPassword);
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
	
}
