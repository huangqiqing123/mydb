package test.tool.gui.dbtool.util;

import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import test.tool.gui.dbtool.beans.DatabaseProduct;
import test.tool.gui.dbtool.beans.FieldInfo;
import test.tool.gui.dbtool.beans.IndexBean;
import test.tool.gui.dbtool.beans.SqlResult;
import test.tool.gui.dbtool.consts.Const;

public class DBUtil {
	
	private static Logger log = Logger.getLogger(DBUtil.class);
	
	 /**  
     * 获得数据库中所有方案名称  
     */  
    public static List<String> getAllSchemas()   
    {   
    	ResultSet rs = null;
    	List<String> list = new ArrayList<String>();
        try  
        {   
           rs = ConnUtil.getInstance().getConn().getMetaData().getSchemas();   
            while (rs.next())   
            {   
                String tableSchem = rs.getString("TABLE_SCHEM");   
                list.add(tableSchem);   
            }   
        } catch (SQLException e){   
        	if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error("获取数据库模式名时出错！",e);
			} 
        	throw new RuntimeException("获取数据库模式名时出错！",e);
        }finally{
        	DBUtil.closeResultSetAndPreparedStatement(rs, null);
        }   
        return list;
    } 
    /**  
     * 获得以指定前缀的表或视图  
     * 仅适用sqlserver数据库
     */  
    public static List<String> getTableAndViewNamesBeginWith(String pre)   
    {
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			rs = ConnUtil.getInstance().getConn().getMetaData().getTables(null,null, pre+"%", new String[] { "TABLE","VIEW" });
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				String tableType = rs.getString("TABLE_TYPE");			
				list.add(tableName+","+tableType);//以逗号分隔
			}
		} catch (SQLException e) {
			if ("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG) + "")) {
				log.error("出错了！", e);
				}
			throw new RuntimeException("出错了！", e);
		} finally {
			DBUtil.closeResultSetAndPreparedStatement(rs, null);
		}
		return list;
	}
	 /**  
     * 获得数据库中所有table名称  
     * 仅适用于SQLserver数据库
     */  
    public static List<String> getAllTableNames()   
    {
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			rs = ConnUtil.getInstance().getConn().getMetaData().getTables(null,null, "%", new String[] { "TABLE" });
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				list.add(tableName);
			}
		} catch (SQLException e) {
			if ("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG) + "")) {
				log.error("出错了！", e);
			}
			throw new RuntimeException("出错了！", e);
		} finally {
			DBUtil.closeResultSetAndPreparedStatement(rs, null);
		}
		return list;
	}
	 /**  
     * 获得数据库中所有View名称  
     */  
    public static List<String> getAllViewNames()   
    {
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			rs = ConnUtil.getInstance().getConn().getMetaData().getTables(null,null, "%", new String[] { "VIEW" });
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				list.add(tableName);
			}
		} catch (SQLException e) {
			if ("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG) + "")) {
				log.error("出错了！", e);
			}
			throw new RuntimeException("出错了！", e);
		} finally {
			DBUtil.closeResultSetAndPreparedStatement(rs, null);
		}
		return list;
	}
    /**  
     * 根据表名，查询字段信息，适用于sqlserver、postgresql
     * 表名、字段名称、字段类型、长度、是否可以为空、默认值
     */  
    public static List<FieldInfo> getTableColums(String tableName,String pre) {
    	List<String> list = new ArrayList<String>();
    	list.add(tableName);
    	return getTableColums(list,pre);
    }
    /**  
     * 根据表名，查询字段信息，适用于sqlserver、postgresql
     * 表名、字段名称、字段类型、长度、是否可以为空、默认值
     */  
    public static List<FieldInfo> getTableColums(List<String> tableNames,String pre)   
    {
		ResultSet rs = null;
		List<FieldInfo> list = new ArrayList<FieldInfo>();
		for(String tableName:tableNames){		
			try {
				rs = ConnUtil.getInstance().getConn().getMetaData().getColumns(null, null, tableName.trim(), pre+"%");   
				while (rs.next()) {
					FieldInfo field = new FieldInfo();
					field.setTableName(tableName);
					field.setFieldName(rs.getString("COLUMN_NAME"));
					field.setFieldType(rs.getString("TYPE_NAME"));
					field.setCanBeNull(rs.getInt("NULLABLE")==1?true:false);
					field.setFieldLength(rs.getString("COLUMN_SIZE"));
					field.setRemarks(rs.getString("REMARKS"));
					
					String defaultValue = rs.getString("COLUMN_DEF");
					if(defaultValue != null){
						//sqlserver数据库，默认值一般是由小括弧()括起来的。
						if(defaultValue.startsWith("(")){
							defaultValue = defaultValue.substring(1);
						}
						if(defaultValue.endsWith(")")){
							defaultValue = defaultValue.substring(0,defaultValue.length()-1);
						}
					}else{
						defaultValue = "";
					}
					field.setDefaultValue(defaultValue);//默认值
					list.add(field);
				}
			} catch (SQLException e) {
				if ("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG) + "")) {
					log.error("获取数据库表字段信息时出错！", e);
					}
				throw new RuntimeException("获取数据库表字段信息时出错！", e);
			} finally {
				DBUtil.closeResultSetAndPreparedStatement(rs, null);
			}
		}
		return list;
	}
    /**
     * 根据表名动态生成创建表的sql语句
     * @param tableName
     * @return Map<
     * key--msg ，msg存放的是错误信息，当msg不为null时，表示生成建表sql出错了。
     * key--sql ，生成的建表sql
     * >
     */
	public static Map<String,String> getCreateSql(String tableName){
		
		String dbType = getDBProductInfo().getProductName();
		if(dbType.contains("MICROSOFT SQL SERVER")){
			
			Map<String,String> returnMap = new HashMap<String, String>();
			List<FieldInfo> fieldInfoList = getTableColums(tableName, "");
			if(fieldInfoList.size() == 0){
				returnMap.put("msg", "对象名 "+tableName+" 无效！");
				return returnMap;
			}
			StringBuilder createSQL = new StringBuilder("-- 创建表 ");
			createSQL.append(tableName);
			createSQL.append("\n");
			createSQL.append("CREATE TABLE ");
			createSQL.append(tableName);
			createSQL.append("(");
			createSQL.append("\n");
			
			for(FieldInfo field : fieldInfoList){
				createSQL.append("    ");//每列缩进4个空格，好看
				
				//字段名称
				createSQL.append(field.getFieldName());
				createSQL.append(" ");
				
				//字段类型
				createSQL.append(field.getFieldType());
				
				//字段长度
				if(! "int".equals(field.getFieldType())){//SQL SERVER数据库，int类型不能指定长度，否则会报错。				
					createSQL.append("(");
					createSQL.append(field.getFieldLength());
					createSQL.append(")");
				}
				
				//默认值信息
				if(!"(null)".equals(field.getDefaultValue()) && !"".equals(field.getDefaultValue())){
					createSQL.append(" DEFAULT ");
					createSQL.append(field.getDefaultValue());
				}
				
				//是否可以为空
				if(! field.getCanBeNull()){
					createSQL.append(" NOT NULL");
				}
				
				//添加逗号分隔，和换行符
				createSQL.append(",");
				createSQL.append("\n");
			}
			createSQL.deleteCharAt(createSQL.length()-1);//删除换行符
			createSQL.deleteCharAt(createSQL.length()-1);//删除末尾逗号
			createSQL.append("\n");
			createSQL.append(");");
			
			
			//设置主键信息，一个表只能有一个主键约束名称，一个主键名称可以关联多个字段。
			List<String> pk_cols = getPrimaryKeyList(tableName);			
			
			//表存在主键的场景
			if(pk_cols.size()>0){
				StringBuilder pk_field_names = new StringBuilder();
				for(String pk_col : pk_cols){
					pk_field_names.append(pk_col);
					pk_field_names.append(",");
				}
				pk_field_names.deleteCharAt(pk_field_names.length()-1);//删除末尾逗号
				createSQL.append("\n");
				createSQL.append("-- 设置主键");
				createSQL.append("\n");
				createSQL.append("ALTER TABLE ");
				createSQL.append(tableName);
				createSQL.append(" ADD CONSTRAINT ");
				createSQL.append(tableName);
				createSQL.append("_pk");
				createSQL.append(" PRIMARY KEY(");
				createSQL.append(pk_field_names);
				createSQL.append(");");
				
			}	
			//设置外键信息，一个表可以有多个外键约束名称，一个外键约束对应一个字段。		
			List<FieldInfo> field_list = getForeignKeys(tableName);
			
			//表存在外键的场景
			if(field_list.size()>0){
				
				createSQL.append("\n");
				createSQL.append("-- 设置外键");
				
				int count = 0;
				for(FieldInfo field : field_list){
					count++;
					createSQL.append("\n");
					createSQL.append("ALTER TABLE ");
					createSQL.append(tableName);
					createSQL.append(" ADD CONSTRAINT ");
					createSQL.append(tableName);
					createSQL.append("_fk");
					createSQL.append(count);
					createSQL.append(" FOREIGN KEY(");
					createSQL.append(field.getFieldName());
					createSQL.append(") REFERENCES ");
					createSQL.append(field.getParentTableName());
					createSQL.append(" (");
					createSQL.append(field.getParentTableFieldName());
					createSQL.append(");");
				}
			}
			//创建索引信息
			//CREATE UNIQUE INDEX IDTABLE_IDX ON PUB_IDTABLE (ID_ID ASC,ORGAN_ID ASC);
			List<IndexBean> index_list = getIndexForSqlServer(tableName);
			if(index_list.size()>0){
				createSQL.append("\n");
				createSQL.append("-- 设置索引");
				for(IndexBean bean : index_list){
					createSQL.append("\n");
					createSQL.append("CREATE  ");
					if(bean.isUniqueIndex()){						
						createSQL.append("UNIQUE ");
					}
					createSQL.append("INDEX ");
					createSQL.append(bean.getIndexName());
					createSQL.append(" ON ");
					createSQL.append(tableName);
					createSQL.append(" (");
					
					//关联列
					List<String> colNameList = bean.getColNameList();
					List<String> asc_desc_list = bean.getColASC_DESC();
					for(int i=0;i<colNameList.size();i++){
						createSQL.append(colNameList.get(i));
						createSQL.append(" ");
						createSQL.append(asc_desc_list.get(i));
						createSQL.append(",");
					}
					createSQL.deleteCharAt(createSQL.length()-1);
					createSQL.append(");");
				}
			}
			returnMap.put("sql", createSQL.toString());
			return returnMap;

		//DB2、Oracle数据库
		}else if(dbType.contains("DB2") || dbType.contains("ORACLE")){
			Map<String,String> returnMap = new HashMap<String,String>();
			
			Map<String,Object> getMap = descTableInfo(tableName);
			if(getMap.get("msg")!=null){
				returnMap.put("msg", getMap.get("msg").toString());
				return returnMap;
			}else{	
				List<FieldInfo> fieldInfoList = (List<FieldInfo>)getMap.get("result"); 
				StringBuilder createSQL = new StringBuilder("-- 创建表 ");
				createSQL.append(tableName);
				createSQL.append("\n");
				createSQL.append("CREATE TABLE ");
				createSQL.append(tableName);
				createSQL.append("(");
				createSQL.append("\n");
				
				for(FieldInfo field : fieldInfoList){
					createSQL.append("    ");//每列缩进4个空格，好看
					
					//字段名称
					createSQL.append(field.getFieldName());
					createSQL.append(" ");
					
					//字段类型
					createSQL.append(field.getFieldType());
					
					//字段长度
					createSQL.append("(");
					createSQL.append(field.getFieldLength());
					createSQL.append(")");
					
					//默认值信息
					if(!"(null)".equals(field.getDefaultValue())){
						createSQL.append(" DEFAULT ");
						createSQL.append(field.getDefaultValue());
					}
					
					//是否可以为空
					if(! field.getCanBeNull()){
						createSQL.append(" NOT NULL");
					}
					
					//添加逗号分隔，和换行符
					createSQL.append(",");
					createSQL.append("\n");
				}
				createSQL.deleteCharAt(createSQL.length()-1);//删除换行符
				createSQL.deleteCharAt(createSQL.length()-1);//删除末尾逗号
				createSQL.append("\n");
				createSQL.append(");");
				
				
				//设置主键信息，一个表只能有一个主键约束名称，一个主键名称可以关联多个字段。
				String pk_CONSTRAINT_name = null;
				StringBuilder pk_field_names = new StringBuilder();
				for(FieldInfo field : fieldInfoList){
					if("主键".equals(field.getConstraintType())){
						pk_CONSTRAINT_name = field.getConstraintName();
						pk_field_names.append(field.getFieldName());
						pk_field_names.append(",");
					}
				}
				if(pk_CONSTRAINT_name != null){//表存在主键的场景
					pk_field_names.deleteCharAt(pk_field_names.length()-1);//删除末尾逗号
					createSQL.append("\n");
					createSQL.append("-- 设置主键");
					createSQL.append("\n");
					createSQL.append("ALTER TABLE ");
					createSQL.append(tableName);
					createSQL.append(" ADD CONSTRAINT ");
					createSQL.append(pk_CONSTRAINT_name);
					createSQL.append(" PRIMARY KEY(");
					createSQL.append(pk_field_names);
					createSQL.append(");");
					
				}
				
				//设置外键信息，一个表可以有多个外键约束名称，一个外键约束对应一个字段。						
				List<String> fk_CONSTRAINT_names = new LinkedList<String>();
				List<String> fk_field_names = new LinkedList<String>();
				List<String> fk_ref_table_names = new LinkedList<String>();
				List<String> fk_ref_table_field_names = new LinkedList<String>();
				for(FieldInfo field : fieldInfoList){
					if("外键".equals(field.getConstraintType())){
						fk_CONSTRAINT_names.add(field.getConstraintName());
						fk_field_names.add(field.getFieldName());
						fk_ref_table_names.add(field.getParentTableName());
						fk_ref_table_field_names.add(field.getParentTableFieldName());
					}
				}
				if(fk_CONSTRAINT_names.size()>0){//表存在外键的场景
					
					createSQL.append("\n");
					createSQL.append("-- 设置外键");
					
					for(int i=0;i<fk_CONSTRAINT_names.size();i++){					
						createSQL.append("\n");
						createSQL.append("ALTER TABLE ");
						createSQL.append(tableName);
						createSQL.append(" ADD CONSTRAINT ");
						createSQL.append(fk_CONSTRAINT_names.get(i));
						createSQL.append(" FOREIGN KEY(");
						createSQL.append(fk_field_names.get(i));
						createSQL.append(") REFERENCES ");
						createSQL.append(fk_ref_table_names.get(i));
						createSQL.append(" (");
						createSQL.append(fk_ref_table_field_names.get(i));
						createSQL.append(");");
					}	
				}
				//创建索引信息
				//CREATE UNIQUE INDEX IDTABLE_IDX ON PUB_IDTABLE (ID_ID ASC,ORGAN_ID ASC);
				Map<String,Object> indexMap = DBUtil.getIndex(tableName);
				if(indexMap.get("msg") == null){
					LinkedList<Map<String,Object>> indexList = (LinkedList<Map<String,Object>>)indexMap.get("result");
					if(indexList.size()>0){
						createSQL.append("\n");
						createSQL.append("-- 设置索引");
						for(Map<String,Object> record : indexList){
							createSQL.append("\n");
							createSQL.append("CREATE  ");
							if("唯一索引".equals(record.get("索引类型")) || "主键索引".equals(record.get("索引类型"))){						
								createSQL.append("UNIQUE ");
							}
							createSQL.append("INDEX ");
							String indexName = record.get("索引名称").toString();
							createSQL.append(indexName);
							createSQL.append(" ON ");
							createSQL.append(tableName);
							createSQL.append(" (");
							
							//关联列   (ID_ID:ASC,ORGAN_ID:ASC)
							StringBuilder tempCols= new StringBuilder(record.get("关联列").toString());
							tempCols.deleteCharAt(0);
							tempCols.deleteCharAt(tempCols.length()-1);
							String columns[] = tempCols.toString().split(",");
							for(String column : columns){
								String columnSeq[] = column.split(":");
								createSQL.append(columnSeq[0]);
								createSQL.append(" ");
								createSQL.append(columnSeq[1]);
								createSQL.append(",");
							}
							createSQL.deleteCharAt(createSQL.length()-1);
							createSQL.append(");");
						}
					}
				}
				returnMap.put("sql", createSQL.toString());
				return returnMap;
			}
		}else if(dbType.contains("MYSQL")){
			Map<String,String> returnMap = new HashMap<String, String>();
			
			//使用mysql提供的show create table命令
			Map<String,Object> getMap = executeQuery("show create table "+tableName);
			if(getMap.get("msg") != null){
				returnMap.put("msg", getMap.get("msg").toString());
			}else{
				List<Map<String, Object>> getList = (List<Map<String, Object>>)getMap.get("result");
				returnMap.put("sql", "-- 创建表 "+tableName+"\n"+getList.get(0).get("Create Table")+";");
			}
			return returnMap;
		}else if(dbType.contains("POSTGRESQL")){
			Map<String,String> returnMap = new HashMap<String, String>();
			List<FieldInfo> fieldInfoList = getTableColums(tableName, "");
			if(fieldInfoList.size() == 0){
				returnMap.put("msg", "对象名 "+tableName+" 无效！");
				return returnMap;
			}
			StringBuilder createSQL = new StringBuilder("-- 创建表 ");
			createSQL.append(tableName);
			createSQL.append("\n");
			createSQL.append("CREATE TABLE ");
			createSQL.append(tableName);
			createSQL.append("(");
			createSQL.append("\n");
			
			for(FieldInfo field : fieldInfoList){
				createSQL.append("    ");//每列缩进4个空格，好看
				
				//字段名称
				createSQL.append(field.getFieldName());
				createSQL.append(" ");
				
				//字段类型
				createSQL.append(field.getFieldType());
				
				//字段长度(bool/bytea/int，不需要设置长度)
				if(! "bool".equals(field.getFieldType()) && 
						! "bytea".equals(field.getFieldType())&& 
						! "int4".equals(field.getFieldType())&& 
						! "int8".equals(field.getFieldType())&& 
						! "int10".equals(field.getFieldType())
						){				
					createSQL.append("(");
					createSQL.append(field.getFieldLength());
					createSQL.append(")");
				}
				
				//默认值信息
				if(!"(null)".equals(field.getDefaultValue()) && !"".equals(field.getDefaultValue())){
					createSQL.append(" DEFAULT ");
					createSQL.append(field.getDefaultValue());
				}
				
				//是否可以为空
				if(! field.getCanBeNull()){
					createSQL.append(" NOT NULL");
				}
				
				//添加逗号分隔，和换行符
				createSQL.append(",");
				createSQL.append("\n");
			}
			createSQL.deleteCharAt(createSQL.length()-1);//删除换行符
			createSQL.deleteCharAt(createSQL.length()-1);//删除末尾逗号
			createSQL.append("\n");
			createSQL.append(");");
			
			//为字段和表添加注释（如果存在的话）
			boolean isFirst = true;
			for(FieldInfo field : fieldInfoList){
				String remark = field.getRemarks();
				if(remark != null){
					if(isFirst){
						createSQL.append("\n");
						createSQL.append("-- 为字段添加注释");
						isFirst = false;
					}
					createSQL.append("\n");
					createSQL.append("COMMENT ON COLUMN  ");
					createSQL.append(field.getTableName()).append(".").append(field.getFieldName());
					createSQL.append(" IS ");
					createSQL.append("'").append(remark).append("';");
				}
			}
			
			//设置主键信息，一个表只能有一个主键约束名称，一个主键名称可以关联多个字段。
			List<String> pk_cols = getPrimaryKeyList(tableName);			
			
			//表存在主键的场景
			if(pk_cols.size()>0){
				StringBuilder pk_field_names = new StringBuilder();
				for(String pk_col : pk_cols){
					pk_field_names.append(pk_col);
					pk_field_names.append(",");
				}
				pk_field_names.deleteCharAt(pk_field_names.length()-1);//删除末尾逗号
				createSQL.append("\n");
				createSQL.append("-- 设置主键");
				createSQL.append("\n");
				createSQL.append("ALTER TABLE ");
				createSQL.append(tableName);
				createSQL.append(" ADD CONSTRAINT ");
				createSQL.append(tableName);
				createSQL.append("_pk");
				createSQL.append(" PRIMARY KEY(");
				createSQL.append(pk_field_names);
				createSQL.append(");");
				
			}	
			//设置外键信息，一个表可以有多个外键约束名称，一个外键约束对应一个字段。		
			List<FieldInfo> field_list = getForeignKeys(tableName);
			
			//表存在外键的场景
			if(field_list.size()>0){
				
				createSQL.append("\n");
				createSQL.append("-- 设置外键");
				
				int count = 0;
				for(FieldInfo field : field_list){
					count++;
					createSQL.append("\n");
					createSQL.append("ALTER TABLE ");
					createSQL.append(tableName);
					createSQL.append(" ADD CONSTRAINT ");
					createSQL.append(tableName);
					createSQL.append("_fk");
					createSQL.append(count);
					createSQL.append(" FOREIGN KEY(");
					createSQL.append(field.getFieldName());
					createSQL.append(") REFERENCES ");
					createSQL.append(field.getParentTableName());
					createSQL.append(" (");
					createSQL.append(field.getParentTableFieldName());
					createSQL.append(");");
				}
			}
			//创建索引信息
			//CREATE UNIQUE INDEX IDTABLE_IDX ON PUB_IDTABLE (ID_ID ASC,ORGAN_ID ASC);
			List<IndexBean> index_list = getIndexForSqlServer(tableName);
			if(index_list.size()>0){
				createSQL.append("\n");
				createSQL.append("-- 设置索引");
				for(IndexBean bean : index_list){
					createSQL.append("\n");
					createSQL.append("CREATE  ");
					if(bean.isUniqueIndex()){						
						createSQL.append("UNIQUE ");
					}
					createSQL.append("INDEX ");
					createSQL.append(bean.getIndexName());
					createSQL.append(" ON ");
					createSQL.append(tableName);
					createSQL.append(" (");
					
					//关联列
					List<String> colNameList = bean.getColNameList();
					List<String> asc_desc_list = bean.getColASC_DESC();
					for(int i=0;i<colNameList.size();i++){
						createSQL.append(colNameList.get(i));
						createSQL.append(" ");
						createSQL.append(asc_desc_list.get(i));
						createSQL.append(",");
					}
					createSQL.deleteCharAt(createSQL.length()-1);
					createSQL.append(");");
				}
			}
			returnMap.put("sql", createSQL.toString());
			return returnMap;
		}else{
			Map<String,String> returnMap = new HashMap<String, String>();
			returnMap.put("msg", "无效的数据库类型！"+dbType);
			return returnMap;
		}
	}
	/*
	 * 获取指定表的索引信息
	 * 适用于sqlserver数据库
	 */
	public static List<IndexBean> getIndexForSqlServer(String tableName){
		
		ResultSet rs = null;
		List<String> indexNames = new ArrayList<String>();
		List<IndexBean> list = new ArrayList<IndexBean>();
		try {
			rs = ConnUtil.getInstance().getConn().getMetaData().getIndexInfo(null, null, tableName, false, false);
			while (rs.next()) {
				String indexName = rs.getString(6);
				if(indexName != null){	
					if(indexNames.contains(indexName)){//如果已经包含了同名的索引，则将这些索引进行合并，一个索引可能对应多个列。
						for(IndexBean index:list){
							if(index.getIndexName().equals(indexName)){
								index.getColNameList().add(rs.getString(9));
								index.getColASC_DESC().add(rs.getString(10).equals("A")?"ASC":"DESC");
								break;
							}
						}
					}else{
						IndexBean bean = new IndexBean();
						bean.setTableName(tableName);
						bean.setIsUniqueIndex(rs.getString(4).equals("0")?true:false);
						bean.setIndexName(rs.getString(6));
						
						List<String> colNameList = new ArrayList<String>();
						colNameList.add(rs.getString(9));
						List<String> colASC_DESC = new ArrayList<String>();
						colASC_DESC.add(rs.getString(10).equals("A")?"ASC":"DESC");
						bean.setColNameList(colNameList);
						bean.setColASC_DESC(colASC_DESC);
						
						list.add(bean);
						indexNames.add(indexName);
					}
				}
			}
		} catch (SQLException e) {
			if ("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG) + "")) {
				log.error("获取索引信息出错！", e);
			}
		} finally {
			DBUtil.closeResultSetAndPreparedStatement(rs, null);
		}
		return list;
	}
	
    /*
     * 仅适用于Oracle、DB2数据库
	 * 执行查询操作,返回Map<String,Object>
	 * 内含2个元素
	 * result：LinkedList<Map<String,Object>>
	 * msg：出错信息，如果没出错，则该值是null。
	 */
	public static Map<String,Object> getIndex(String tableName){
		
		//首先检查表是否存在，如果表不存在。。。
		Map<String,Object> tmp = executeQuery("select * from "+tableName+" where 1=2");
		if(tmp.get("msg")!= null){	
			return tmp;
			
		//如果表存在。。。
		}else{
			Map<String,Object> returnMap = new HashMap<String, Object>();
			LinkedList<Map<String,Object>> list = new LinkedList<Map<String,Object>>();/////表名、索引名称、关联列（列1：desc，列2：asc）
			String dbType = getDBProductInfo().getProductName();
			if(dbType.contains("ORACLE")){
				String indexSql1 = "SELECT DISTINCT INDEX_NAME,UNIQUENESS,INDEX_TYPE  FROM user_indexes  WHERE TABLE_NAME='"+tableName+"'";
				Map<String,Object> getMap = executeQuery(indexSql1);
				if(getMap.get("msg")!=null){
					return getMap;
				}
				List<Map<String, Object>> indexNames = (List<Map<String, Object>>)getMap.get("result");
				if(indexNames==null||indexNames.size()==0){//如果表未建索引   
					returnMap.put("msg", tableName+" 表未建立索引！");
					return returnMap;
				}else{
					for (Map<String, Object> indexNameMap : indexNames) {
						
						//oracle数据库中，如果数据库表包含大字段，则会自动为其创建lob类型的索引，这种索引是不需要手工维护的，故而过滤掉。
						String indexType2 = indexNameMap.get("INDEX_TYPE").toString();
						if("LOB".equals(indexType2)){
							continue;
						}
						String indexName = indexNameMap.get("INDEX_NAME").toString();
						String indexType = indexNameMap.get("UNIQUENESS").toString();
						Map<String,Object> rec = new LinkedHashMap<String, Object>();
						rec.put("表名称", tableName);
						rec.put("索引名称", indexName);
						rec.put("索引类型", getChineseIndexType(indexType));
						StringBuilder revlentCol = new StringBuilder();
						revlentCol.append("(");
						
						String indexSql2 = "SELECT INDEX_NAME,TABLE_NAME,COLUMN_NAME,DESCEND FROM user_ind_columns WHERE INDEX_NAME='"+indexName+"'";
						List<Map<String, Object>> returnValue2 = (List<Map<String, Object>>)executeQuery(indexSql2).get("result");
						for (Map<String, Object> map : returnValue2) {
							revlentCol.append(map.get("COLUMN_NAME"));
							revlentCol.append(":");
							revlentCol.append(map.get("DESCEND"));
							revlentCol.append(",");
						}
						revlentCol.deleteCharAt(revlentCol.length()-1);
						revlentCol.append(")");
						rec.put("关联列",revlentCol);
						list.add(rec);
					}
				}		
			}else if(dbType.contains("DB2")){
				String indexSql1 = "select DISTINCT INDNAME,UNIQUERULE   from SYSCAT.INDEXES  where TABNAME='"+tableName+"'";
				Map<String,Object> getMap = executeQuery(indexSql1);
				if(getMap.get("msg")!=null){
					return getMap;
				}else{
					List<Map<String, Object>> indexNames = (List<Map<String, Object>>)getMap.get("result");
					if(indexNames==null||indexNames.size()==0){//如果表未建索引
						returnMap.put("msg", tableName+" 表未建立索引！");
						return returnMap;
					}else{
						list = new LinkedList<Map<String,Object>>();/////表名、索引名称、关联列（列1：desc，列2：asc）
						for (Map<String, Object> indexNameMap : indexNames) {
							String indexName = indexNameMap.get("INDNAME").toString();
							String indexType = indexNameMap.get("UNIQUERULE").toString();
							Map<String,Object> rec = new LinkedHashMap<String, Object>();
							rec.put("表名称", tableName);
							rec.put("索引名称", indexName);
							rec.put("索引类型", getChineseIndexType(indexType));
							StringBuilder revlentCol = new StringBuilder();
							revlentCol.append("(");
							
							String indexSql2 = "select COLNAME,COLORDER from SYSCAT.INDEXCOLUSE where indname = '"+indexName+"'";
							List<Map<String, Object>> returnValue2 = (List<Map<String, Object>>)executeQuery(indexSql2).get("result");
							for (Map<String, Object> map : returnValue2) {
								revlentCol.append(map.get("COLNAME"));
								revlentCol.append(":");
								revlentCol.append(map.get("COLORDER").toString().equals("D")?"DESC":"ASC");
								revlentCol.append(",");
							}
							revlentCol.deleteCharAt(revlentCol.length()-1);
							revlentCol.append(")");
							rec.put("关联列",revlentCol);
							list.add(rec);
						}
					}	
				}
			}	
			returnMap.put("result", list);
			return returnMap;	
		}
	}
	  /*
	 * 执行更新操作,返回Map<String,String>
	 * 内含2个元素
	 * result：影响的记录行数
	 * msg：出错信息，如果没出错，则该值是null。
	 */
	public static Map<String, String> executeUpdate(String sql){

		PreparedStatement ps = null;
		Map<String, String> returnMap = new HashMap<String,String>();//返回值
		try {
			ps = ConnUtil.getInstance().getConn().prepareStatement(sql);
			int affectLines = ps.executeUpdate();
			returnMap.put("result", affectLines+"");
		} catch (SQLException e) {
			returnMap.put("msg", e.getMessage());
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(sql, e);
			}
		} finally {
			DBUtil.closeResultSetAndPreparedStatement(null, ps);
		}
		//记录执行的sql
		if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
			log.debug(sql);
		}
		return returnMap;
	}
	  /**
	 * 执行查询操作,返回Map<String,Object>
	 * 内含2个元素
	 * result：List<Map<String columnName, Object columnValue>>
	 * msg：出错信息，如果没出错，则该值是null。
	 */
	public static Map<String,Object> executeQuery(String sql){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Map<String, Object>> dataSet = null;//结果集
		String msg = null;
		try {
			ps = ConnUtil.getInstance().getConn().prepareStatement(sql);
			rs =  ps.executeQuery();
			dataSet = buildDataSet(rs);	
		} catch (SQLException e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(sql, e);
			}
			msg = e.getMessage();
		}finally{
			closeResultSetAndPreparedStatement(rs, ps);
		}
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("result", dataSet);
		returnMap.put("msg", msg);//如果msg不等于null，则表示查询出错了。
		
		//记录执行的sql
		if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
			log.debug(sql);
		}
		return returnMap;
	}
	  /*
	 * 执行查询操作,返回Map<String,Object>
	 * 内含3个元素
	 * isResultSet：true、false
	 * result：List<Map<String columnName, Object columnValue>> 或 影响的行数
	 * msg：出错信息，如果没出错，则该值是null。
	 */
	public static Map<String,Object> execute(String sql){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Object result = null;//结果集
		String msg = null;
		boolean isResultSet = false;
		try {
			ps = ConnUtil.getInstance().getConn().prepareStatement(sql);
			isResultSet =  ps.execute();
			if(isResultSet){	
				rs = ps.getResultSet();
				result = buildDataSet(rs);	
			}else{
				result = ps.getUpdateCount();
			}
		} catch (SQLException e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(sql, e);
			}
			msg = e.getMessage();
		}finally{
			closeResultSetAndPreparedStatement(rs, ps);
		}
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("isResultSet", isResultSet);
		returnMap.put("result", result);
		returnMap.put("msg", msg);//如果msg不等于null，则表示查询出错了。
		
		//记录执行的sql
		if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
			log.debug(sql);
		}
		return returnMap;
	}
	/*
	 * 构建dataset数据集
	 */
	private static List<Map<String, Object>> buildDataSet(ResultSet rs) throws SQLException{
		String dbType = getDBProductInfo().getProductName();
		List<Map<String, Object>> dataSet = new LinkedList<Map<String,Object>>();//结果集
		List<String> columnNameList = new LinkedList<String>();//列名称list	
		ResultSetMetaData metadata = rs.getMetaData();//获取数据表元数据
		int columnCount = metadata.getColumnCount();//列数
		for(int i=1;i<=columnCount;i++){
			String columnName = metadata.getColumnName(i);
			columnNameList.add(columnName);
		}
		while(rs.next()){	
			Map<String, Object> record = new LinkedHashMap<String, Object>();//对应数据库表中的一条记录
			for(int j=0;j<columnNameList.size();j++){

				String columnName = columnNameList.get(j);
				int columnType = metadata.getColumnType(j+1);
				Object columnValue = null;
				if(columnType==Types.TIMESTAMP){//对时间戳类型的处理	
					try {
						columnValue = rs.getTimestamp(j+1);
					} catch (Exception e) {
						columnValue = rs.getString(j+1);
						log.error("", e);
					}
					
				}else if(columnType==Types.CLOB){//对于clob字段的处理
					Clob clob = rs.getClob(j+1);
					if(clob != null){		
						columnValue = clob.getSubString(1, (int)clob.length());
					}
				}else if(columnType==Types.DATE){//对于date字段的处理
					columnValue = rs.getDate(j+1);
				}else if(columnType==Types.TIME){//对于time字段的处理
					columnValue = rs.getTime(j+1);
				}else if(columnType==Types.BLOB){//对于blob字段的处理
					byte[] temp = rs.getBytes(j+1);
					if(temp.length>20000){
						columnValue = rs.getObject(j+1);
					}else{					
						try {
							columnValue = new String(temp,"UTF-8");
						} catch (UnsupportedEncodingException e) {
							columnValue = new String(temp);
						}
					}
				}else if(columnType==Types.BIT){
				    if(dbType.contains("MYSQL")){	
				    	//mysql数据库tinyint(1)类型，需要getByte()读取数据，如果使用getObject()，则返回的是true（非0）、false（0）。
				    	columnValue = rs.getByte(j+1);
					}else if(dbType.contains("POSTGRESQL")){
						//对应postgres数据库中的bool类型
						columnValue = rs.getBoolean(j+1);
					}else{
						columnValue = rs.getObject(j+1);
					}
				 //匹配postgres bytea，20kb以下转换为16进制展示
				}else if(columnType==Types.BINARY){
					byte[] temp = rs.getBytes(j+1);
					if(temp != null){
						if(temp.length>20000){
							columnValue = rs.getObject(j+1);
						}else{					
							columnValue = MathUtil.bytesToHex(temp);
							if(dbType.contains("POSTGRESQL")){
								columnValue = "E'\\\\x"+columnValue+"'::bytea";
							}
						}
					}
				}else{
					columnValue = rs.getObject(j+1);
				}
				record.put(columnName, columnValue);
			}
			dataSet.add(record);
		}	
		return dataSet;
	}
	/*
	 * 查询元数据信息
	 */
	public static Map<String,Object> getColumnNameList(String sql) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> columnNameList = new LinkedList<String>();// 列名称list
		String msg = null;
		try {
			ps = ConnUtil.getInstance().getConn().prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData metadata = rs.getMetaData();// 获取数据表元数据
			int columnCount = metadata.getColumnCount();// 列数
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metadata.getColumnName(i);
				columnNameList.add(columnName);
			}
		} catch (Exception e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(sql, e);
			}
			msg = e.getMessage();
		} finally {
			closeResultSetAndPreparedStatement(rs, ps);
		}
		Map<String,Object> returnMap = new HashMap<String, Object>();
		returnMap.put("result", columnNameList);
		returnMap.put("msg", msg);//如果返回的msg的值是null，则说明未产生异常。
		return returnMap;
	}
	/*
	 *获取当前数据库连接信息
	 */
	public static DatabaseProduct getDBProductInfo(){
		
		DatabaseProduct dbp = new DatabaseProduct();
		try {
			DatabaseMetaData metaData = ConnUtil.getInstance().getConn().getMetaData();
			dbp.setCatalog(ConnUtil.getInstance().getConn().getCatalog());
			dbp.setDefaultTransactionIsolation(metaData.getDefaultTransactionIsolation()+"");
			dbp.setDriverName(metaData.getDriverName());
			dbp.setDriverVersion(metaData.getDriverVersion());
			dbp.setProductName(metaData.getDatabaseProductName().toUpperCase());
			dbp.setProductVersion(metaData.getDatabaseProductVersion());
			dbp.setUrl(metaData.getURL());
			dbp.setUsername(metaData.getUserName());
		} catch (SQLException e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(null, e);
			}
			throw new RuntimeException("出错了！",e);
		}
		return dbp;
	}
	/*
	 * 关闭数据库连接
	 * 关闭顺序：ResultSet——PreparedStatement
	 */
	public static  void closeResultSetAndPreparedStatement(ResultSet rs,PreparedStatement ps){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
			}
		}
		if(ps!=null){
			try {
				ps.close();
			} catch (SQLException e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
			}
		}
	}
	/*
	 * 查询表结构信息
	 * 仅适用于DB2、Oracle数据库
	 */
	public static Map<String,Object> descTableInfo(String tableName){

		tableName = tableName.toUpperCase();
		List<FieldInfo> dataSet = new LinkedList<FieldInfo>();
		
		Map<String,Object> returnMap = new HashMap<String, Object>();
		
		//设置字段名称和表名称
		Map<String,Object> getMap = DBUtil.getColumnNameList("select * from "+tableName+" where 1=2");
		
		//执行getColumnNameList的时候出错了，直接返回出错信息。
		if(getMap.get("msg")!=null){
			returnMap.put("msg", getMap.get("msg"));
		}else{
			List<String> columnNameList = (List<String>)getMap.get("result");
			for (int i = 0; i < columnNameList.size(); i++) {
				FieldInfo field = new FieldInfo();
				field.setFieldName(columnNameList.get(i));
				field.setTableName(tableName);
				dataSet.add(field);
			}
			String dbType = DBUtil.getDBProductInfo().getProductName();
			if(dbType.contains("ORACLE")){
				
				//设置数据类型、数据长度、是否可为空
				for (int i = 0; i < columnNameList.size(); i++) {
					String sql = "SELECT DATA_TYPE AS 数据类型,DATA_LENGTH AS 字段长度 , NULLABLE AS 是否可为空,DATA_DEFAULT as 默认值 FROM USER_TAB_COLUMNS  WHERE TABLE_NAME='"+tableName+"' and COLUMN_NAME='"+columnNameList.get(i)+"'";
					List<Map<String,Object>> returnValue = (List<Map<String, Object>>)DBUtil.executeQuery(sql).get("result");
					FieldInfo temp = dataSet.get(i);
					temp.setFieldType(returnValue.get(0).get("数据类型")+"");//因为只返回一条记录，所以直接使用get(0)
					temp.setFieldLength((returnValue.get(0).get("字段长度")+""));
					String canBeNull = returnValue.get(0).get("是否可为空")+"";
					if("N".equals(canBeNull)){			
						temp.setCanBeNull(false);
					}else{
						temp.setCanBeNull(true);
					}
					Object defaultValue = returnValue.get(0).get("默认值");
					temp.setDefaultValue(defaultValue == null?"(null)":defaultValue+"");
				}
				//设置关联的约束信息：约束名称、约束类型、外键关联父表名称、其他
				for (int i = 0; i < columnNameList.size(); i++) {
					String sql = "select  con.constraint_name  as 约束名称, constraint_type as 约束类型,con.status as 约束状态,R_CONSTRAINT_NAME AS 引用约束名称,search_condition as 其他" +
							" from user_constraints con,user_cons_columns col   where col.CONSTRAINT_NAME=con.CONSTRAINT_NAME " +
							"and COL.TABLE_NAME='"+tableName+"' and column_name='"+columnNameList.get(i)+"'";
					List<Map<String,Object>> returnValue = (List<Map<String, Object>>)DBUtil.executeQuery(sql).get("result");//同一个表中的同一个字段可能存在多个约束，所以此处返回记录可能是多条。

					for(int j=0;j<returnValue.size();j++){
						
						Map<String,Object> map = returnValue.get(j);
						if(map.get("约束类型").equals("C")){
							continue;//不记录Check约束
						}			
						dataSet.get(i).setConstraintName(map.get("约束名称")+"");
						dataSet.get(i).setR_constraintName(map.get("引用约束名称")+"");
						
						if(map.get("约束类型").equals("P")){
							String consType = "主键";
							if(dataSet.get(i).getConstraintType()!=null){
								consType = consType+"、"+dataSet.get(i).getConstraintType();
							}
							dataSet.get(i).setConstraintType(consType);
						}else if(map.get("约束类型").equals("R")){
							String consType = "外键";
							if(dataSet.get(i).getConstraintType()!=null){
								consType = consType+"、"+dataSet.get(i).getConstraintType();
							}
							dataSet.get(i).setConstraintType(consType);
							
							//设置外键约束状态
							String constraintStatus = map.get("约束状态")+"";
							if("DISABLED".equals(constraintStatus)){
								dataSet.get(i).setConstraintStatus("已禁用");	
							}else if("ENABLED".equals(constraintStatus)){
								dataSet.get(i).setConstraintStatus("已启用");
							}else{
								dataSet.get(i).setConstraintStatus("未知状态："+constraintStatus);
							}
							//如果是外键，则查询其关联的父表
							String sql2 = "select TABLE_NAME as 父表名称,COLUMN_NAME as 外键关联父表字段名称 from user_cons_columns where constraint_name='"+map.get("引用约束名称")+"'";
							List<Map<String,Object>> returnValue2 = (List<Map<String, Object>>)DBUtil.executeQuery(sql2).get("result");
							dataSet.get(i).setParentTableName(returnValue2.get(0).get("父表名称")+"");
							dataSet.get(i).setParentTableFieldName(returnValue2.get(0).get("外键关联父表字段名称")+"");
							
						}else{
							dataSet.get(i).setConstraintType("CHECK约束");
						}
					}
				}		
			}else if(dbType.contains("DB2")){		
				
				//设置数据类型（字段类型）、数据长度（字段长度）、是否可为空
				for (int i = 0; i < columnNameList.size(); i++) {
					String sql = "SELECT TYPENAME AS 数据类型,LENGTH AS 字段长度 , NULLS AS 是否可为空,DEFAULT AS 默认值 FROM SYSCAT.COLUMNS  WHERE TABNAME='"+tableName+"' and COLNAME='"+columnNameList.get(i)+"'";
					List<Map<String,Object>> returnValue = (List<Map<String, Object>>)DBUtil.executeQuery(sql).get("result");
					FieldInfo temp = dataSet.get(i);
					temp.setFieldType(returnValue.get(0).get("数据类型")+"");//因为只返回一条记录，所以直接使用get(0)
					temp.setFieldLength((returnValue.get(0).get("字段长度")+""));
					String canBeNull = returnValue.get(0).get("是否可为空")+"";
					if("N".equals(canBeNull)){			
						temp.setCanBeNull(false);
					}else{
						temp.setCanBeNull(true);
					}
					Object defaultValue = returnValue.get(0).get("默认值");
					temp.setDefaultValue(defaultValue == null?"(null)":defaultValue+"");
				}
				//设置关联的约束信息：约束名称、约束类型、外键关联父表名称、其他
				for (int i = 0; i < columnNameList.size(); i++) {
					String sql = "select t1.CONSTNAME AS 约束名称,TYPE as 约束类型,t1.ENFORCED as 约束状态  from SYSCAT.TABCONST t1,SYSCAT.KEYCOLUSE t2  " +
							"WHERE t1.CONSTNAME= t2.CONSTNAME " +
							"and t1.TABNAME='"+tableName+"' and t2.colname='"+columnNameList.get(i)+"'";
					
					List<Map<String,Object>> returnValue = (List<Map<String, Object>>)DBUtil.executeQuery(sql).get("result");//同一个表中的同一个字段可能存在多个约束，所以此处返回记录可能是多条。

					for(int j=0;j<returnValue.size();j++){
						
						Map<String,Object> map = returnValue.get(j);
						if(map.get("约束类型").equals("C")){
							continue;//不记录Check约束
						}			
						dataSet.get(i).setConstraintName(map.get("约束名称")+"");
						
						if(map.get("约束类型").equals("P")){//考虑到当一个字段既是主键又是外键的场景。
							String consType = "主键";
							if(dataSet.get(i).getConstraintType()!=null){
								consType = consType+"、"+dataSet.get(i).getConstraintType();
							}
							dataSet.get(i).setConstraintType(consType);
						}else if(map.get("约束类型").equals("F")){
							String consType = "外键";
							if(dataSet.get(i).getConstraintType()!=null){
								consType = consType+"、"+dataSet.get(i).getConstraintType();
							}
							dataSet.get(i).setConstraintType(consType);
							
							//设置外键约束状态
							String constraintStatus = map.get("约束状态")+"";
							if("N".equals(constraintStatus)){
								dataSet.get(i).setConstraintStatus("已禁用");	
							}else if("Y".equals(constraintStatus)){
								dataSet.get(i).setConstraintStatus("已启用");
							}else{
								dataSet.get(i).setConstraintStatus("未知状态："+constraintStatus);
							}
							
							//如果是外键，则查询其关联的父表、引用约束名称
							String sql2 = "select REFTABNAME as 父表名称,PK_COLNAMES AS 外键关联父表字段名称 from syscat.references WHERE TABNAME='"+tableName+"' AND CONSTNAME='"+map.get("约束名称")+"'";
							List<Map<String,Object>> returnValue2 = (List<Map<String, Object>>)DBUtil.executeQuery(sql2).get("result");
							dataSet.get(i).setParentTableName(returnValue2.get(0).get("父表名称")+"");
							dataSet.get(i).setParentTableFieldName((returnValue2.get(0).get("外键关联父表字段名称")+"").trim());
						}else{
							dataSet.get(i).setConstraintType("未知约束类型"+map.get("约束类型"));
						}
					}
				}		
			}
			returnMap.put("result", dataSet);
		}
		return returnMap;
	}
	/*
	 * 获取索引类型的中文描述
	 */
	private static String getChineseIndexType(String enIndexType){
		String chineseIndexType = null;
		if("D".equals(enIndexType) || "NONUNIQUE".equals(enIndexType)){
			chineseIndexType = "非唯一索引";
		}else if("P".equals(enIndexType)){
			chineseIndexType = "主键索引";
		}else if("U".equals(enIndexType) || "UNIQUE".equals(enIndexType)){
			chineseIndexType = "唯一索引";
		}
		return chineseIndexType;
	}
	/**
	 * 查询主键---通用
	 * @param tableName
	 * @return
	 */
    public static List<String> getPrimaryKeyList(String tableName)   
    {
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();	
		try {
			rs = ConnUtil.getInstance().getConn().getMetaData().getPrimaryKeys(null, null, tableName);
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");   
				list.add(columnName);
			}
		} catch (SQLException e) {
			if ("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG) + "")) {
				log.error("获取主键信息出错！", e);
			}
		} finally {
			DBUtil.closeResultSetAndPreparedStatement(rs, null);
		}
		return list;
	}
	/**
	 * 查询主键
	 * @param tableName
	 * @return
	 */
    public static List<String>  getPrimaryKeyList_mysql(String tableName){
    	String table_schema = getSchema_mysql();
    	List<String> pkList = new ArrayList<String>();;
    	if(table_schema != null){
    		String tableNameNew = tableName.replace("`", "");
    		String getPKsql = "select COLUMN_NAME from information_schema.KEY_COLUMN_USAGE where TABLE_SCHEMA='"+table_schema+"' and TABLE_NAME='"+tableNameNew+"' and CONSTRAINT_NAME in(select CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where TABLE_SCHEMA='"+table_schema+"' and table_name='"+tableNameNew+"' and CONSTRAINT_TYPE='PRIMARY KEY')";
    		Map<String, Object> map_pk = DBUtil.executeQuery(getPKsql);
    		if(map_pk.get("msg") == null){
    			List<Map<String, Object>> list_pk = (List<Map<String, Object>>)map_pk.get("result");
    			if(list_pk != null && list_pk.size() > 0){
        			for(Map<String, Object> record:list_pk){
        				pkList.add(record.get("COLUMN_NAME").toString());
        			}
    			}
    	}
    	}
    	return pkList;
    }

	/**
	 * 查询主键
	 * @param tableName
	 * @return
	 */
    public static List<String>  getPrimaryKeyList_oracle(String tableName,String schemaName){
    	
    	List<String> pkList = new ArrayList<String>();;
		String getPKsql = "select COLUMN_NAME from user_cons_columns WHERE CONSTRAINT_NAME in(select CONSTRAINT_NAME from user_constraints where TABLE_NAME='"+tableName.toUpperCase()+"'  AND OWNER='"+schemaName.toUpperCase()+"' AND CONSTRAINT_TYPE='P')";
		Map<String, Object> map_pk = DBUtil.executeQuery(getPKsql);
		if(map_pk.get("msg") == null){
			List<Map<String, Object>> list_pk = (List<Map<String, Object>>)map_pk.get("result");
			if(list_pk != null && list_pk.size() > 0){
    			for(Map<String, Object> record:list_pk){
    				pkList.add(record.get("COLUMN_NAME").toString());
    			}
			}
		}
    	return pkList;
    }
	/**
	 * 查询主键
	 * @param tableName
	 * @return
	 */
    public static List<String>  getPrimaryKeyList_db2(String tableName){
    	
    	List<String> pkList = new ArrayList<String>();;
		String getPKsql = "select COLNAME as COLUMN_NAME from SYSCAT.KEYCOLUSE where CONSTNAME in(select CONSTNAME from SYSCAT.TABCONST where TYPE='P' and  TABNAME='"+tableName.toUpperCase()+"')";
		Map<String, Object> map_pk = DBUtil.executeQuery(getPKsql);
		if(map_pk.get("msg") == null){
			List<Map<String, Object>> list_pk = (List<Map<String, Object>>)map_pk.get("result");
			if(list_pk != null && list_pk.size() > 0){
    			for(Map<String, Object> record:list_pk){
    				pkList.add(record.get("COLUMN_NAME").toString());
    			}
			}
		}
    	return pkList;
    }
    /**
	 * 查询主键，适用于DB2、Oracle、sqlserver、mysql
	 * @param tableName
	 * @return
	 */
    public static List<String>  getPrimaryKeyList(String tableName,String schemaName){
    	
    	List<String> primaryKeyList = null;
    	String dbType = getDBProductInfo().getProductName();
		if(dbType.contains("ORACLE")){
			primaryKeyList = DBUtil.getPrimaryKeyList_oracle(tableName,schemaName);
		}else if(dbType.contains("MYSQL")){	
			primaryKeyList = DBUtil.getPrimaryKeyList_mysql(tableName);
		}else if(dbType.contains("DB2")){
			primaryKeyList = DBUtil.getPrimaryKeyList_db2(tableName);
		}else if(dbType.contains("MICROSOFT SQL SERVER") || dbType.contains("POSTGRESQL")){
			primaryKeyList = DBUtil.getPrimaryKeyList(tableName);
		}
		return primaryKeyList;
    }
    /**
     * 获取单表select * 查询sql。
     * 为防止表数据量过大，需要限制最大返回记录数(默认1000条)。
     * @param tableName
     */
    public static String getSimpleQueryLimitSql(String tableName){
    	String simpleQueryLimitSql = null;
    	String dbType = getDBProductInfo().getProductName();
	    if(dbType.contains("MYSQL")){	
			simpleQueryLimitSql = "select * from `"+tableName+"` limit 0,1000";
		}else if(dbType.contains("POSTGRESQL")){
			simpleQueryLimitSql = "select * from "+tableName+" limit 1000 offset 0";
		}else{
			simpleQueryLimitSql = "select * from "+tableName;
		}
		return simpleQueryLimitSql;
    }
    /**
     * 获取当前模式名
     * @return
     */
    public static String getSchema_mysql(){
    	String table_schema = null;
		try {
			table_schema = ConnUtil.getInstance().getConn().getCatalog();
		} catch (SQLException e1) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error("查询模式名出错", e1);
			}
		}
		return table_schema;
    }
    
    /*
	 * 获取外键信息
	 * 包含信息：子表名称、子表字段、父表名称、父表字段
	 */
    public static List<FieldInfo> getForeignKeys(String tableName)   
    {
		ResultSet rs = null;
		List<FieldInfo> list = new ArrayList<FieldInfo>();
		try {
			DatabaseMetaData metaData = ConnUtil.getInstance().getConn().getMetaData();
			rs = metaData.getImportedKeys(null, null, tableName);
			while (rs.next()) {
				FieldInfo field = new FieldInfo();
				field.setParentTableName(rs.getString("PKTABLE_NAME"));
				field.setParentTableFieldName(rs.getString("PKCOLUMN_NAME"));
				field.setConstraintName(rs.getString("FK_NAME"));//外键约束名称
				field.setFieldName(rs.getString("FKCOLUMN_NAME"));
				field.setTableName(tableName);
				list.add(field);
			}
			//获取该外键约束的状态
			buildConstraintStatus(list);
		} catch (SQLException e) {
			if ("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG) + "")) {
				log.error("获取外键信息出错！", e);
			}
		} finally {
			DBUtil.closeResultSetAndPreparedStatement(rs, null);
		}
		return list;
	}
    //补全外键约束的状态
    private static List<FieldInfo> buildConstraintStatus(List<FieldInfo> list){
    	if(list != null){
    		try {
    			String dbType = getDBProductInfo().getProductName();
    			if(dbType.contains("MICROSOFT SQL SERVER")){
    				DatabaseMetaData metaData = ConnUtil.getInstance().getConn().getMetaData();
    				String databaseProductVersion = metaData.getDatabaseProductVersion();//8.0 sqlserver2000,9.0 sqlserver2005,10.0 sqlserver 2008
    				int databaseProductVersion_int = Integer.parseInt(databaseProductVersion.substring(0, databaseProductVersion.indexOf(".")));
    				for(FieldInfo field : list){
    					//sql server 2000及以下版本
    					if(databaseProductVersion_int <= 8){
    						String sql = "select name as 外键约束名称,status as 约束状态 from sysobjects where name='"+field.getConstraintName()+"' and xtype='F';";
    						List<Map<String,Object>> returnList = (List<Map<String, Object>>)executeQuery(sql).get("result");
    						String constraintStatus = returnList.get(0).get("约束状态")+"";
    						if(constraintStatus.equals("2306")){//sql server 2000中，2306表示已禁用，2表示已启用
    							field.setConstraintStatus("已禁用");
    						}else{
    							field.setConstraintStatus("已启用");
    						}
    						//sqlserver2005 及以上版本
    					}else{
    						String sql = "select name as 约束名称, is_disabled 是否禁用 from sys.foreign_keys where name='"+field.getConstraintName()+"'";
    						List<Map<String,Object>> returnList = (List<Map<String, Object>>)executeQuery(sql).get("result");
    						String constraintStatus = returnList.get(0).get("是否禁用")+"";
    						if(constraintStatus.equals("true")){
    							field.setConstraintStatus("已禁用");
    						}else{
    							field.setConstraintStatus("已启用");
    						}
    					}
    				}
    			}
			} catch (Exception e) {
				log.error("获取外键约束状态信息出错！", e);
			}
    	} 
    	return list;
    }
    /*
	 * 查询子表信息
	 * 包含信息：主表名称，主表字段，子表名称，子表字段
	 */
    public static List<FieldInfo> getChildTables(String parentTableName)   
    {
		ResultSet rs = null;
		List<FieldInfo> list = new ArrayList<FieldInfo>();
		try {
			rs = ConnUtil.getInstance().getConn().getMetaData().getExportedKeys(null, null, parentTableName);
			while (rs.next()) {
				FieldInfo field = new FieldInfo();
				field.setParentTableName(rs.getString("PKTABLE_NAME"));
				field.setParentTableFieldName(rs.getString("PKCOLUMN_NAME"));
				field.setFieldName(rs.getString("FKCOLUMN_NAME"));
				field.setTableName(rs.getString("FKTABLE_NAME"));
				list.add(field);
			}
		} catch (SQLException e) {
			if ("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG) + "")) {
				log.error("获取外键信息出错！", e);
			}
		} finally {
			DBUtil.closeResultSetAndPreparedStatement(rs, null);
		}
		return list;
	}
    /*
	 * 启用or禁止指定表的约束
	 * 支持DB2、Oracle、SQLserver数据库
	 */
	public static List<SqlResult> disableORenbaleFK(boolean isEnable,String...tableNames){

		List<SqlResult> resultList = new ArrayList<SqlResult>();
		String dbType = getDBProductInfo().getProductName();
		if(dbType.equals("MICROSOFT SQL SERVER")){
			
			//加上with check，重启约束时，会对现有数据进行检查，如果现有数据违反了该约束，则启动约束会失败，如果不加with check，则不会对现有数据进行检查，一般都会重启成功。
			String check = isEnable?"with check CHECK":"NOCHECK";
			for(String tableName:tableNames){	
				String sql = "alter table "+tableName.toUpperCase()+" "+check+" constraint all";
				Map<String,String> map = executeUpdate(sql);
				
				SqlResult sqlResult = new SqlResult();//记录每条sql的执行结果
				if(map.get("msg") != null){
					sqlResult.setSuccess(false);
					sqlResult.setResult(map.get("msg"));
				}else{
					sqlResult.setSuccess(true);
					sqlResult.setResult("执行成功，影响记录行数 "+map.get("result"));
				}
				sqlResult.setSql(sql);
				resultList.add(sqlResult);
			}
		}else{
			String sql = null;
			if(dbType.contains("ORACLE")){
				sql = "select 'alter table ' || table_name || ' disable constraint ' || constraint_name  from user_constraints where constraint_type='R' and TABLE_NAME in(";
				if(isEnable){
					sql = sql.replace("disable", "enable");
				}		
			}else if(dbType.contains("DB2")){
				sql = "select 'ALTER TABLE '  || TBNAME ||  ' ALTER FOREIGN KEY ' || NAME ||' NOT ENFORCED ' FROM SYSIBM.SYSTABCONST WHERE CONSTRAINTYP='F' and TBNAME in(";
				if(isEnable){
					sql = sql.replace("NOT ENFORCED", "ENFORCED");
				}		
			}else{
				throw new RuntimeException("当前数据库类型无效！"+dbType);
			}	
			StringBuffer generateSQL = new StringBuffer(sql);
			for(String tableName:tableNames){	
				generateSQL.append(" '");
				generateSQL.append(tableName.toUpperCase());//注意须转换成大写
				generateSQL.append("',");
			}
			generateSQL.deleteCharAt(generateSQL.length()-1);
			generateSQL.append(")");
			List<Map<String, Object>> dataSet = (List<Map<String, Object>>)executeQuery(generateSQL.toString()).get("result");
			
			//启用or停止查询出的外键约束
			for(int i=0;i<dataSet.size();i++){
				Map<String, Object> record = dataSet.get(i);
			     Iterator<Entry<String, Object>> itor = record.entrySet().iterator();   
			        while(itor.hasNext())   
			        {   
			           Entry<String, Object> e = itor.next(); 
			           Map<String,String> map = executeUpdate(e.getValue().toString());
						
						SqlResult sqlResult = new SqlResult();//记录每条sql的执行结果
						if(map.get("msg") != null){
							sqlResult.setSuccess(false);
							sqlResult.setResult(map.get("msg"));
						}else{
							sqlResult.setSuccess(true);
							sqlResult.setResult("执行成功，影响记录行数 "+map.get("result"));
						}
						sqlResult.setSql(e.getValue().toString());
						resultList.add(sqlResult);
			        }   	       
			}
		}	
		return resultList;
	}
	/*
	 * 启用or禁用所有外键约束
	 * DB2、Oracle、SqlServer
	 */
	public static List<SqlResult> disableORenableAllConstraint(boolean isEnable){
		
		List<SqlResult> resultList = new ArrayList<SqlResult>();
		String dbType = getDBProductInfo().getProductName();
		if(dbType.equals("MICROSOFT SQL SERVER")){
			String check = isEnable?"CHECK":"NOCHECK";
			List<String> tableNames = getAllTableNames();
			for(String tableName:tableNames){	
				String sql = "alter table "+tableName.toUpperCase()+" "+check+" constraint all";
				Map<String,String> map = executeUpdate(sql);
				
				SqlResult sqlResult = new SqlResult();//记录每条sql的执行结果
				if(map.get("msg") != null){
					sqlResult.setSuccess(false);
					sqlResult.setResult(map.get("msg"));
				}else{
					sqlResult.setSuccess(true);
					sqlResult.setResult("执行成功，影响记录行数 "+map.get("result"));
				}
				sqlResult.setSql(sql);
				resultList.add(sqlResult);
			}
		}else{
			String sql = null;
			if(dbType.contains("ORACLE")){
				
				//默认是执行禁止操作
				sql = "select 'alter table ' || table_name || ' disable constraint ' || constraint_name  from user_constraints where constraint_type='R'";//R表示外键		
				if(isEnable){			
					sql = sql.replace("disable", "enable");
				}
			}else if(dbType.contains("DB2")){//SYSIBM.SYSTABCONST是DB2字典表的一个视图，可以查询所有表的约束信息。
				sql = "select 'ALTER TABLE '  || TBNAME ||  ' ALTER FOREIGN KEY ' || NAME ||' NOT ENFORCED ' FROM SYSIBM.SYSTABCONST WHERE CONSTRAINTYP='F'";//F表示外键
				if(isEnable){
					sql = sql.replace("NOT ENFORCED", "ENFORCED");
				}		
			}else{
				throw new RuntimeException("无效的数据库类型："+dbType);
			}	
			
			//查询指定表的外键约束
			List<Map<String, Object>> dataSet = (List<Map<String, Object>>)executeQuery(sql).get("result");
			
			//禁止或启用查询出的外键约束
			for(int i=0;i<dataSet.size();i++){
				Map<String, Object> record = dataSet.get(i);
			     Iterator<Entry<String, Object>> itor = record.entrySet().iterator();   
			        while(itor.hasNext())   
			        {   
			           Entry<String, Object> e = itor.next();   
			           Map<String,String> map = executeUpdate(e.getValue().toString());
						
						SqlResult sqlResult = new SqlResult();//记录每条sql的执行结果
						if(map.get("msg") != null){
							sqlResult.setSuccess(false);
							sqlResult.setResult(map.get("msg"));
						}else{
							sqlResult.setSuccess(true);
							sqlResult.setResult("执行成功，影响记录行数 "+map.get("result"));
						}
						sqlResult.setSql(e.getValue().toString());
						resultList.add(sqlResult);
			        }   	       
			}
		}	
		return resultList;
	}
	/**
	 * 查询指定表的元数据信息（所有数据库通用）
	 * @param tableName
	 * @return ResultSetMetaData
	 */
	public static ResultSetMetaData getResultSetMetaData(String tableName){
		PreparedStatement ps = null;
		ResultSet rs = null;	
		ResultSetMetaData  rm  = null;
		try {
			ps = ConnUtil.getInstance().getConn().prepareStatement("select * from "+tableName+" where 1=2");
			rs = ps.executeQuery();
			rm = rs.getMetaData();
		} catch (Exception e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error("获取数据库模式名时出错！",e);
			} 
		}finally{
			closeResultSetAndPreparedStatement(rs, null);//如果关闭ps，oracle外部使用ResultSetMetaData会报错
		}
		return rm;
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
	 * 目前只有DB2、Oracle、SQL Server、MYSQL、POSTGRESQL是支持的数据库类型
	 * 其他数据库只支持标准sql
	 */
	public static boolean isSupportedDBType(){
		String dbType = getDBProductInfo().getProductName();
		if(dbType.contains("ORACLE") 
				|| dbType.contains("DB2") 
				||dbType.contains("MICROSOFT SQL SERVER")
				||dbType.contains("MYSQL")
				||dbType.contains("POSTGRESQL")){
			return true;
		}
		return false;
	}
}
