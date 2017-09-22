package test.tool.gui.dbtool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import test.tool.gui.dbtool.beans.DataSourceInfo;
import test.tool.gui.dbtool.consts.Const;

public class DataSourceMapUtil {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(DataSourceMapUtil.class);
	
	/*
	 * 读取DS文件，datasource.data
	 */
	public static TreeMap<String, DataSourceInfo> getDataSourceTreeMap(){
		ObjectInputStream oips = null;
		TreeMap<String,DataSourceInfo> TreeMap = null;
		File file = new File(Const.DATA_SOURCE_PATH);

		if (file.exists()) {
			try {
				oips = new ObjectInputStream(new FileInputStream(Const.DATA_SOURCE_PATH));
				TreeMap=(TreeMap<String, DataSourceInfo>)oips.readObject();
			} catch (Exception e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error("读取文件"+Const.DATA_SOURCE_PATH+"出错！",e);
				}
				throw new RuntimeException("读取文件"+Const.DATA_SOURCE_PATH+"出错！",e);
			}finally{
				if(oips!=null)
					try {
						oips.close();
					} catch (IOException e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e);
						}
					}
			}
		}
		return TreeMap;
	}
	/*
	 * 保存数据源信息至磁盘
	 */
	public static void saveDs2Disk( TreeMap<String,DataSourceInfo> table){
		ObjectOutputStream oops = null;
		try {
			oops = new ObjectOutputStream(new FileOutputStream(Const.DATA_SOURCE_PATH));// 对象输出流
			oops.writeObject(table);
		} catch (Exception e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error("保存文件"+Const.DATA_SOURCE_PATH+"出错！",e);
			}
			throw new RuntimeException("保存文件"+Const.DATA_SOURCE_PATH+"出错！",e);
		}finally{
			if(oops!=null){			
				try {
					oops.flush();
					oops.close();
				} catch (IOException e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
				}
			}
		}
	}
	/*
	 * 根据key删除一条记录
	 */
	public static void delete(String key){		
		TreeMap<String, DataSourceInfo> table  = getDataSourceTreeMap();
		table.remove(key); 
		saveDs2Disk(table);
	}
	/*
	 * 获取当前数据源连接
	 * sn 值以current结尾的 是当前连接，如果不存在，则返回null。
	 */
	public static DataSourceInfo getCurrentCon(Map<String,DataSourceInfo> map){
		 Iterator<String> itor = map.keySet().iterator();   
	        while(itor.hasNext())   
	        {   
	            String key = itor.next(); 
				DataSourceInfo ds = map.get(key);
				if(ds.getSn().endsWith(Const.CURRENT)){
					return ds;
				}	
	        }  
	        return null;
	}
	/*
	 * 获取当前数据源连接
	 * sn 值以current结尾的 是当前连接，如果不存在，则返回null。
	 */
	public static DataSourceInfo getCurrentCon() {
		Map<String, DataSourceInfo> map = getDataSourceTreeMap();
		if (map == null) {
			return null;
		}
		Iterator<String> itor = map.keySet().iterator();
		while (itor.hasNext()) {
			String key = itor.next();
			DataSourceInfo ds = map.get(key);
			if (ds.getSn().endsWith(Const.CURRENT)) {
				return ds;
			}
		}
		return null;
	}
	/*
	 * 检查某个dataSourceInfo在TreeMap中是否已存在。
	 * 如果已存在，则返回已存在的连接，如果不存在，则返回null。
	 */
	public static DataSourceInfo isExisted(DataSourceInfo ds){

		TreeMap<String, DataSourceInfo> map = DataSourceMapUtil.getDataSourceTreeMap();
		if(map == null || map.size()==0){
			return null;
		}else{	
			for(Entry<String, DataSourceInfo> entry : map.entrySet()){
				
				if(entry.getValue().equals(ds)){
					return entry.getValue();
				}
			}
			return null;
		}
	}
}
