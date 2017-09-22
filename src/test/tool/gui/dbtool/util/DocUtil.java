package test.tool.gui.dbtool.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import test.tool.gui.dbtool.consts.Const;

public class DocUtil {
	
	private static Logger log = Logger.getLogger(DocUtil.class);
	
	/**
	 * 根据文件路径，读取文本文件
	 * @param docPath docPath有可能是相对路径，返回的str[0]后会转化成绝对路径。
	 * @return String[] 共包含3个元素，第一个元素是文件的绝对路径，第二个元素是文件内容，第三个是文件编码
	 */
	public static String[] getCharDocContent(String docPath){
		
		BufferedReader br = null;
    	StringBuilder content = new StringBuilder();
    	String[] strArr = new String[3];
    	try {
    		File file = new File(docPath);
    		strArr[0] = file.getAbsolutePath();
    		FileInputStream fr = new FileInputStream(file);
    		String code = EncodingDetect.getJavaEncode(docPath);
    		InputStreamReader brs = null;
    		if(code == null || "".equals(code)){
    			brs = new InputStreamReader(fr);
    		}else{
    			brs = new InputStreamReader(fr,EncodingDetect.getJavaEncode(docPath));
    		}
    		strArr[2] = brs.getEncoding();
    		br = new BufferedReader(brs); 
    		String readline;
			while ((readline = br.readLine()) != null) {
				content.append(readline+"\n");
			}
		} catch (FileNotFoundException e) {
			content.append("未找到文件 "+docPath);
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(null, e);
			}
		} catch (IOException e) {
			content.append("读取文件 "+docPath+" 出错");
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(null, e);
			}
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
				}		
			}
		}	
		strArr[1] = content.toString();
		return strArr;
	}
}
