package test.tool.gui.dbtool.window;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;

import com.sun.awt.AWTUtilities;

import test.tool.gui.common.SysFontAndFace;
import test.tool.gui.dbtool.image.ImageIcons;
import test.tool.gui.dbtool.util.ColorUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

public class JWindowWait extends JWindow{

	private static final long serialVersionUID = 1L;
	private static JWindowWait waitWindow = null;
	private JWindowWait(Frame parent) {
		super(parent);

		JLabel tips = new JLabel("正在执行，请稍后...");
//		tips.setOpaque(true);//设置组件JLabel不透明，只有设置为不透明，设置背景色才有效
//		tips.setBackground(ColorUtil.LU_DOU_SHA);
		tips.setFont(new Font("宋体", Font.PLAIN, 18));
		tips.setForeground(Color.BLUE);
		tips.setIcon(ImageIcons.wait_gif32);
		getContentPane().add(tips, BorderLayout.CENTER);
		setLocationRelativeTo(parent);
		setSize(250, 50);
	}

	public static JWindowWait getWaitWindow(Frame parent){
		if(waitWindow == null){
			waitWindow = new JWindowWait(parent);
			
			//设置window透明度,0.5 表示设置窗口透明度为50%
			float ca = 0.5f;
			AWTUtilities.setWindowOpacity(waitWindow, ca);
			
			//false 设置window完全透明，true 设置window完全不透明
			//AWTUtilities.setWindowOpaque(waitWindow, false);
		}
		return waitWindow;
	}
	public static void main(String[] args) {
		JFrame f = new JFrame();
		getWaitWindow(f).setVisible(true);
	}
}
