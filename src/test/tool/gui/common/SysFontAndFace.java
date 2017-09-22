package test.tool.gui.common;

import java.awt.Font;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.util.ConfigUtil;

/*
 * swing全局字体设置类
 */
public class SysFontAndFace {

	private static Logger log = Logger.getLogger(SysFontAndFace.class);
	public static Font font = new Font("宋体", Font.PLAIN, 13);
	public static Font font14 = new Font("宋体", Font.PLAIN, 14);
	public static Font font15 = new Font("宋体", Font.PLAIN, 15);
	
	/*
	 * 自定义全局字体
	 */
	public static void setSysFontAndFace() {
		try {
			UIManager.put("ToolTip.font", font);
			UIManager.put("Table.font", font);
			UIManager.put("TableHeader.font", font);
			UIManager.put("TextField.font", font);
			UIManager.put("ComboBox.font", font);
			UIManager.put("TextField.font", font);
			UIManager.put("PasswordField.font", font);
			UIManager.put("TextArea.font", font15);
			UIManager.put("TextPane.font", font);
			UIManager.put("EditorPane.font", font);
			UIManager.put("FormattedTextField.font", font);
			UIManager.put("Button.font", font);
			UIManager.put("CheckBox.font", font14);
			UIManager.put("RadioButton.font", font);
			UIManager.put("ToggleButton.font", font);
			UIManager.put("ProgressBar.font", font);
			UIManager.put("DesktopIcon.font", font);
			UIManager.put("TitledBorder.font", font);
			UIManager.put("Label.font", font);
			UIManager.put("List.font", font);
			UIManager.put("TabbedPane.font", font);
			UIManager.put("MenuBar.font", font14);
			UIManager.put("Menu.font", font14);
			UIManager.put("MenuItem.font", font14);
			UIManager.put("PopupMenu.font", font);
			UIManager.put("CheckBoxMenuItem.font", font);
			UIManager.put("RadioButtonMenuItem.font", font);
			UIManager.put("Spinner.font", font);
			UIManager.put("ToolBar.font", font);
			UIManager.put("OptionPane.messageFont", font);
			UIManager.put("OptionPane.buttonFont", font); 

		} catch (Exception e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(null, e);
			}
		}
	}
}
