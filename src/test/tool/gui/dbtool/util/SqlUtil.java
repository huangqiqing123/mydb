package test.tool.gui.dbtool.util;

public class SqlUtil {

	public static String formatSql(String originSql){
		if (originSql == null || originSql.isEmpty()) {
			return null;
		}
		String sql = originSql.trim();
		String sqlTmp = sql.toLowerCase();
		if(sqlTmp.startsWith("select") 
				|| sqlTmp.startsWith("on")
				|| sqlTmp.startsWith("insert")
				|| sqlTmp.startsWith("delete")
				|| sqlTmp.startsWith("update")){
			return new SQLFormaterBasic().format(sql).trim();
		}else if(sqlTmp.startsWith("alter") 
				|| sqlTmp.startsWith("create")
				|| sqlTmp.startsWith("comment")){
			
			return SQLFormaterDDL.INSTANCE.format(sql).trim();
		}else{
			return originSql;
		}
	
	}
}
