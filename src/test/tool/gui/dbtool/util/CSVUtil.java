package test.tool.gui.dbtool.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import test.tool.gui.dbtool.consts.Const;

public class CSVUtil {
	
	private static Logger log = Logger.getLogger(CSVUtil.class);

	public static List<String> csv2sql(File csv) {
	      Scanner s = null;
	      String table_name = null;
	      List<String> sqls = new ArrayList<String>();
	      String dbType = DBUtil.getDBProductInfo().getProductName();
	      
	      table_name = csv.getName();
	      if(table_name.contains(".")){
	    	  table_name = table_name.substring(0, table_name.indexOf("."));
	      }
	      try {
			String code = EncodingDetect.getJavaEncode(csv.getAbsolutePath());
			if (code == null) {
				code = "UTF-8";
			}
			s = new Scanner(csv, code);
		} catch (FileNotFoundException e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(null, e);
			}
		}
	      //设置分隔符，默认使用空格进行分割，此处设置分隔符为“折行符”
	      s.useDelimiter("\\n");
	      
	      String column_names[] = null;
	      int column_count = 0;
	      StringBuilder header =  null;
	      if(dbType.contains("MYSQL")){
	    	  header = new StringBuilder("insert into `"+table_name+"` (");
	      }else{
	    	  header = new StringBuilder("insert into "+table_name+" (");
	      }
	      
	      int i = 0;
	      while(s.hasNext()){
	    	  i++;
	         String line = s.next();
	         if(i == 1){//第一行，字段名
	        	 column_names = line.split(",");
	        	 column_count = column_names.length;
	        	 for (int j = 0; j < column_names.length; j++) {
	        		 if(dbType.contains("MYSQL")){
	        			 header.append("`").append(column_names[j].trim()).append("`").append(",");	 
	        		 }else{
	        			 header.append(column_names[j].trim()).append(",");
	        		 }
				}
	        	 header.deleteCharAt(header.length()-1);
	        	 header.append(")");
	         }else{
	        	StringBuilder sql = new StringBuilder(header);
	        	String values[] = line.split(",");
	        	if(column_count == values.length){//列value中不包含逗号，该情况下处理简单
	        		sql.append(" values(");
	        		for (int j = 0; j < values.length; j++) {
	        			String value = values[j];
	        			if(j == column_count -1){//最后一个元素，去除末端换行符(\r\n)
	        				value = value.replaceAll("\r|\n", "");
	        			}
	        			sql.append(value);
						sql.append(",");
					}
	        		sql.deleteCharAt(sql.length()-1);
	        		sql.append(")");
	        	}else{
	        		//列value中也包含逗号的场景
	        		sql.append(" values(");
	        		for (int j = 0; j < values.length; j++) {
	        			String true_value = null;
	        			String value = values[j];
	        			if(j == values.length -1){//最后一个元素，去除末端换行符(\r\n)
	        				value = value.replaceAll("\r|\n", "");
	        			}
	        			if(value.startsWith("\"")){
	        				if(value.endsWith("\"")){
	        					true_value = value;
	        				}else{
	        					while(true){
	        						j++;
	        						if(j == values.length){
	        							break;
	        						}
	        						value = value + "," + values[j];
	        						if(values[j].endsWith("\"")){
	        							true_value = value;
	        							break;
	        						}
	        					}
	        				}
	        			}else{
	        				true_value = value;
	        			}
	        			sql.append(true_value);
						sql.append(",");
					}
	        		sql.deleteCharAt(sql.length()-1);
	        		sql.append(")");
	        	}
	        	sqls.add(sql.toString());
	         }
	      }
	      if(s != null){
	    	  s.close();//关闭流
	      }
	      return sqls;
	   }
	
	public static void main(String[] args) {
	}
}
//id, attach_ids, cert_name, cert_code, attach_count, upload_time, cert_type, status, uid
//"1","1048,1047,740,726,727","我的身份证","4","5","1365318453","1","0","23908"
//"11","875","护照1","0011","1","1364883498","2","0","23908"
//"5","877,734","eee","eee","2","1364883810","1","0","23908"
//"10","874,872,873","护照1","001","3","1364883464","2","0","23908"
//"12","1265,1263,1264","111","111","3","1365492559","2","0","3"
