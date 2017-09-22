package test.tool.gui.dbtool.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;
import test.tool.gui.dbtool.consts.Const;

public class JarLoadUtil {

	private static Logger log = Logger.getLogger(JarLoadUtil.class);
	private static URL lib_url = ClassLoader.getSystemClassLoader().getResource("lib");
	private static URLClassLoader loader = null;
	/*
	 * 加载lib目录下所有jar文件，并返回相应的的URLClassLoader
	 */
	public static URLClassLoader getURLClassLoader(){	
		
		if(loader == null){		
			String fileNames[] = listFileNames();
			if(fileNames != null && fileNames.length > 0){	 
				URL urls[] = new URL[fileNames.length];
				for(int i = 0;i < fileNames.length;i++){
					try {
						urls[i] = new URL(lib_url+"/"+fileNames[i]);
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.debug("加载包："+fileNames[i]);
						}
					} catch (MalformedURLException e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error("加载lib目录下jar文件出错！",e);
						}
						throw new RuntimeException("加载lib目录下jar文件出错！",e);
					}
				}
				loader = new URLClassLoader(urls);
		}else{
			throw new RuntimeException(lib_url+"路径下缺少数据库驱动包！");
		}
		}
		return loader;
	}
	/*
	 * 查询lib目录下的所有文件名称
	 */
	private static String[] listFileNames(){

		File lib_directory = new File("lib");
		return lib_directory.list();
	}
}
