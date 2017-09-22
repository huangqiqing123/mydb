package test.tool.gui.dbtool.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

public class LocationUtil {

	/*
	 * 获取组件居中的位置坐标
	 */
	public static int[] getCenterLocation(Component componet){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		int left = (width - componet.getSize().width) / 2;
		int top = (height - componet.getSize().height) /2;
		int location[] = {left,top};
		return location;
	}
}
