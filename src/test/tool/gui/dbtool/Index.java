package test.tool.gui.dbtool;

import java.awt.SplashScreen;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.theme.SubstanceBarbyPinkTheme;
import org.jvnet.substance.theme.SubstanceBottleGreenTheme;
import org.jvnet.substance.theme.SubstanceBrownTheme;
import org.jvnet.substance.theme.SubstanceLightAquaTheme;
import org.jvnet.substance.theme.SubstanceLimeGreenTheme;

import test.tool.gui.common.SysFontAndFace;
import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.dialog.SetValue;
import test.tool.gui.dbtool.frame.IndexFrame;
import test.tool.gui.dbtool.util.ConfigUtil;

public class Index {
	
	//静态初始化块，最先执行。
	static{	
		//1、验证应用程序同级目录下，log文件夹是否存在，如果不存在，则创建。
		File file_log = new File("log");
		if(!file_log.exists() || !file_log.isDirectory()){
			if(!file_log.mkdirs()){
				JOptionPane.showMessageDialog(null, "创建log目录失败！");
				System.exit(0);
			}
		}
		//2、验证应用程序同级目录下，config文件夹是否存在，如果不存在，则创建。
		File file_config = new File("config");
		if(!file_config.exists() || !file_config.isDirectory()){
			if(!file_config.mkdirs()){
				JOptionPane.showMessageDialog(null, "创建config目录失败！");
				System.exit(0);
			}
		}
		//3、验证应用程序目录下，lib目录是否存在，如果不存在，则创建。
		File file_lib = new File("lib");
		if(!file_lib.exists() || !file_lib.isDirectory()){
			if(!file_lib.mkdirs()){
				JOptionPane.showMessageDialog(null, "创建lib目录失败！");
				System.exit(0);
			}
		}
	}
	private static Logger log = Logger.getLogger(Index.class);

	/*
	 * 程序入口
	 */
		public static void main(String args[]) {
			
		//使用substance皮肤，必须将代码放到SwingUtilities.invokeLater中执行（Swing组件的更新一定要在EDT线程中进行）
		 SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					try {			
						
						// 读取配置文件，根据配置设置界面风格
						String skin = ConfigUtil.getConfInfo().get(Const.SKIN).toString();

					// 苹果风格
					if (skin.startsWith("苹果风格")) {
						
						//设置边框
						if (System.getProperty("substancelaf.useDecorations") == null) {
							JFrame.setDefaultLookAndFeelDecorated(true);
							JDialog.setDefaultLookAndFeelDecorated(true);
						}
						UIManager.setLookAndFeel(new SubstanceLookAndFeel());
						SubstanceLookAndFeel.setCurrentTitlePainter(new org.jvnet.substance.title.Glass3DTitlePainter()); //设置标题  	
						SubstanceLookAndFeel.setCurrentGradientPainter(new org.jvnet.substance.painter.StandardGradientPainter()); //设置渲染方式  
						SubstanceLookAndFeel.setCurrentButtonShaper(new org.jvnet.substance.button.StandardButtonShaper()); //设置按钮外观   

						// 扩展水印
						// SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark()); //水泡 水印
						 //SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBinaryWatermark());//二进制 01 水印
						 //SubstanceLookAndFeel.setCurrentWatermark(new SubstanceImageWatermark(new FileInputStream(new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Penguins.jpg"))));
						
						if(skin.equals("苹果风格-浅绿")){			
							SubstanceLookAndFeel.setCurrentTheme(new SubstanceLimeGreenTheme()); //浅绿色调
						}else if(skin.equals("苹果风格-深绿")){						
							SubstanceLookAndFeel.setCurrentTheme(new SubstanceBottleGreenTheme()); //深绿色调
						}else if(skin.equals("苹果风格-粉红")){
							SubstanceLookAndFeel.setCurrentTheme(new SubstanceBarbyPinkTheme()); //粉红色调						
						}else if(skin.equals("苹果风格-浅褐")){
							SubstanceLookAndFeel.setCurrentTheme(new SubstanceBrownTheme()); //浅褐					
						}else{					
							SubstanceLookAndFeel.setCurrentTheme(new SubstanceLightAquaTheme());// 浅蓝色调
						}		
					} else if (skin.equals("Windows风格")) {
						String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
						UIManager.setLookAndFeel(lookAndFeel);

					}else if (skin.equals("Java6风格")) {
						String lookAndFeel = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
						UIManager.setLookAndFeel(lookAndFeel);
					} else {
						// java标准风格，不需要做任何设置
					}

					// 设置系统字体
					SysFontAndFace.setSysFontAndFace();

					// 设置输入中文时，不显示输入框
					System.setProperty("java.awt.im.style", "on-the-spot");

					//显示设置连接窗口
					IndexFrame.getInstance().setVisible(true);

					// 关闭欢迎信息
					SplashScreen ss = SplashScreen.getSplashScreen();
					if (ss != null) {
						ss.close();
					}
				} catch (Exception e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
					}
				}
			});
	}		
}
