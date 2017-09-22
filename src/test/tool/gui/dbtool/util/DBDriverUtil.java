package test.tool.gui.dbtool.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import test.tool.gui.dbtool.consts.Const;

public class DBDriverUtil {

	private static Map<String,String> dbDrivers = null;
	private static List<String> dbTypes = null;
	/*
	 * 获取驱动map信息
	 */
	private static Map<String,String> getDBDriverMap(){
		if(dbDrivers == null){
		
			//指定排序器
			dbDrivers = new TreeMap<String, String>(new Comparator<String>(){
				public int compare(String o1, String o2) {		
					
					return o1.compareTo(o2);//指定排序器按照升序序排列
				}	
			});

			//加载驱动配置文件
			Properties p = new Properties();
			InputStream inputStream = null;
			try {
				inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(Const.DB_DRIVER_PATH);
				if(inputStream == null){
					throw new RuntimeException("找不到文件"+Const.DB_DRIVER_PATH);
				}
				p.load(inputStream);
				
				//将Properties中数据转存至TreeMap，是为了按照key排序。
				Set<Entry<Object, Object>> set = p.entrySet();
				for(Entry<Object, Object> entry:set){
					dbDrivers.put(entry.getKey().toString(), entry.getValue().toString());
				}
			} catch (IOException e) {
				throw new RuntimeException("读取文件出错！"+Const.DB_DRIVER_PATH);
			}finally{
				try {
					if(inputStream != null){
						inputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dbDrivers;
	}
	/*
	 * 获取数据库类型
	 */
	public static List<String> getDBTypes(){
		if(dbTypes == null){
			dbTypes = new ArrayList<String>();
			Set<String> set = getDBDriverMap().keySet();
			dbTypes.addAll(set);
		}
		return dbTypes;
	}
	/*
	 * 根据数据库类型，获取相对应的驱动类。
	 */
	public static String getDriverClass(String dbtype){
		return getDBDriverMap().get(dbtype).split("#")[0];
	}
	/*
	 * 根据数据库类型，获取相对应的驱动URL。
	 */
	public static String getURL(String dbtype){
		return getDBDriverMap().get(dbtype).split("#")[1];
	}
}
