package test.tool.gui.dbtool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import org.apache.log4j.Logger;

import test.tool.gui.common.MyColor;
import test.tool.gui.common.SysFontAndFace;
import test.tool.gui.dbtool.consts.Const;

public class ConfigUtil {
	
	private static Logger log = Logger.getLogger(ConfigUtil.class);

	/*
	 * 获取配置信息
	 */
	private static Hashtable<String,Object> conf_table = null;
	public static Hashtable<String,Object> getConfInfo(){
		if(conf_table==null){
			conf_table = loadConfInfo();
		}
		return conf_table;
	}
	/*
	 * 更新配置文件，config/conf.data
	 */
	public static void updateConfInfo(){

		ObjectOutputStream oops = null;
		try {
			oops = new ObjectOutputStream(new FileOutputStream(Const.CONF_PATH));
			oops.writeObject(getConfInfo());
		} catch (Exception e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error("更新配置文件出错！",e);
			}
			throw new RuntimeException("更新配置文件出错！",e);
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
	 * 读取配置文件，config/conf.data
	 */
	private static Hashtable<String,Object> loadConfInfo(){
		ObjectInputStream oips = null;
		try {
			File file = new File(Const.CONF_PATH);
			if(file.exists()){		
				oips = new ObjectInputStream(new FileInputStream(file));
				conf_table=(Hashtable<String,Object>)oips.readObject();
			}
		} catch (Exception e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error("读取配置文件出错！",e);
			}
			throw new RuntimeException("读取配置文件出错！",e);
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
		if (conf_table == null) {
			conf_table = new Hashtable<String, Object>(); //首次使用本软件时，conf_table为null
		}
		if(conf_table.get(Const.SQL_FONT)==null){		
			conf_table.put(Const.SQL_FONT, SysFontAndFace.font15);// sql编辑器字体
		}
		if(conf_table.get(Const.JTABLE_FONT)==null){		
			conf_table.put(Const.JTABLE_FONT, SysFontAndFace.font14);// 列表字体
		}
		if(conf_table.get(Const.JTREE_FONT)==null){		
			conf_table.put(Const.JTREE_FONT, SysFontAndFace.font);// 树节点字体
		}
		if(conf_table.get(Const.NOTEPAD_FONT)==null){		
			conf_table.put(Const.NOTEPAD_FONT, SysFontAndFace.font14);//记事本字体
		}
		if(conf_table.get(Const.JTABLE_LINE_HEIGHT)==null){		
			conf_table.put(Const.JTABLE_LINE_HEIGHT, "20");// 查询结果列表行高
		}
		if(conf_table.get(Const.IS_ENABLE_SMART_TIPS)==null){	
			conf_table.put(Const.IS_ENABLE_SMART_TIPS, "true");// 是否启用智能提示
		}
		if(conf_table.get(Const.IS_AUTO_COMMIT)==null){	
			conf_table.put(Const.IS_AUTO_COMMIT, "true");// 事务是否自动提交
		}
		if(conf_table.get(Const.CLOSE_ACTION)==null){	
			conf_table.put(Const.CLOSE_ACTION, "3");// 关闭按钮行为
		}
		if(conf_table.get(Const.IS_GENERATE_SQL)==null){
			conf_table.put(Const.IS_GENERATE_SQL, "true");// Jtree是否同步生成sql
		}
		if(conf_table.get(Const.IS_LOG)==null){
			conf_table.put(Const.IS_LOG, "true");// 是否启用日志，默认启用(便于定位问题，有些机器启动时就报错了)
		}
		if(conf_table.get(Const.EYE_SAFETY_COLOR)==null){
			conf_table.put(Const.EYE_SAFETY_COLOR,new MyColor("LU_DOU_SHA","绿豆沙",ColorUtil.LU_DOU_SHA));// 眼睛保护色（默认是绿豆沙）
		}
		if(conf_table.get(Const.SKIN)==null){
			conf_table.put(Const.SKIN, "Java标准风格");// 界面风格
		}
		return conf_table;
	}
}
