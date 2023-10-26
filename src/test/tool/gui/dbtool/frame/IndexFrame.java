package test.tool.gui.dbtool.frame;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.fasterxml.jackson.databind.ObjectMapper;

import sun.security.jca.GetInstance.Instance;
import test.tool.gui.common.MyColor;
import test.tool.gui.common.SysFontAndFace;
import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.dialog.CloseChoice;
import test.tool.gui.dbtool.dialog.SetValue;
import test.tool.gui.dbtool.image.ImageIcons;
import test.tool.gui.dbtool.mycomponent.MyJTextField;
import test.tool.gui.dbtool.mycomponent.MyJextArea;
import test.tool.gui.dbtool.mycomponent.MyTableModel;
import test.tool.gui.dbtool.util.ConfigUtil;
import test.tool.gui.dbtool.util.ConnUtil;
import test.tool.gui.dbtool.util.LocationUtil;

import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class IndexFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static IndexFrame instance;
	
	// 获取操作系统托盘
    private static SystemTray tray = SystemTray.getSystemTray(); 
    public static TrayIcon trayIcon = null;

	public static IndexFrame getInstance(){
		if(instance == null){
			instance = new IndexFrame(null);
		
			// 使窗口居中显示
			int location[] = LocationUtil.getCenterLocation(instance);
			instance.setLocation(location[0], location[1]);
			instance.setResizable(false);
		}
		return instance;
	}
	

	/**
	 * Create the frame.
	 */
	private IndexFrame(JFrame parent) {
		if(parent != null){
			this.setLocationRelativeTo(parent);//设置父组件
		}
		setBounds(100, 100, 600, 400);
		
		this.setIconImage(ImageIcons.ico_png.getImage());
		this.setTitle("工具箱");
		
		//使用网格布局，同时指定行数、列数、水平间隙、垂直间隙
		GridLayout layout = new GridLayout(0,3,5,5);
		getContentPane().setLayout(layout);
		JButton dbtool = new JButton("数据库工具");
		dbtool.setFont(SysFontAndFace.font14);
		JButton jwttool = new JButton("JWT解码");
		jwttool.setFont(SysFontAndFace.font14);
		JButton notepad = new JButton("记事本");
		notepad.setFont(SysFontAndFace.font14);
		JButton base64 = new JButton("Base64编码解码");
		base64.setFont(SysFontAndFace.font14);
		JButton url = new JButton("URL编码解码");
		url.setFont(SysFontAndFace.font14);
		JButton datetime = new JButton("时间戳转换");
		datetime.setFont(SysFontAndFace.font14);
		JButton json = new JButton("JSON格式化");
		json.setFont(SysFontAndFace.font14);
		JButton sql = new JButton("SQL格式化");
		sql.setFont(SysFontAndFace.font14);
		JButton hash = new JButton("哈希计算");
		hash.setFont(SysFontAndFace.font14);
		getContentPane().add(dbtool);
		getContentPane().add(jwttool);
		getContentPane().add(notepad);
		getContentPane().add(base64);
		getContentPane().add(url);
		getContentPane().add(hash);
		getContentPane().add(datetime);
		getContentPane().add(sql);
		getContentPane().add(json);
		
		
		dbtool.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() instanceof JButton){
					//DB工具连接配置窗口
					SetValue.getInstance().setVisible(true);
				}
			}
		});
		jwttool.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() instanceof JButton){
					Base64Frame.getInstance(instance).showJwtTab();
	            	Base64Frame.getInstance(instance).setBackColor();
	            	Base64Frame.getInstance(instance).setVisible(true);
	            	Base64Frame.getInstance(instance).setFont((Font)ConfigUtil.getConfInfo().get(Const.SQL_FONT));
				}
			}
		});
		notepad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyNotePad notePad = new MyNotePad(getInstance(),null,null,null);
            	notePad.setVisible(true);
			}
		});
		base64.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() instanceof JButton){
					Base64Frame.getInstance(instance).showBase64Tab();
	            	Base64Frame.getInstance(instance).setBackColor();
	            	Base64Frame.getInstance(instance).setVisible(true);
	            	Base64Frame.getInstance(instance).setFont((Font)ConfigUtil.getConfInfo().get(Const.SQL_FONT));
				}
			}
		});
		json.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Base64Frame.getInstance(instance).showJsonTab();
            	Base64Frame.getInstance(instance).setBackColor();
            	Base64Frame.getInstance(instance).setVisible(true);
            	Base64Frame.getInstance(instance).setFont((Font)ConfigUtil.getConfInfo().get(Const.SQL_FONT));
			}
		});
		sql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Base64Frame.getInstance(instance).showSqlTab();
            	Base64Frame.getInstance(instance).setBackColor();
            	Base64Frame.getInstance(instance).setVisible(true);
            	Base64Frame.getInstance(instance).setFont((Font)ConfigUtil.getConfInfo().get(Const.SQL_FONT));
			}
		});
		datetime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	Base64Frame.getInstance(instance).showTimeStampTab();
            	Base64Frame.getInstance(instance).setBackColor();
            	Base64Frame.getInstance(instance).setVisible(true);
            	Base64Frame.getInstance(instance).setFont((Font)ConfigUtil.getConfInfo().get(Const.SQL_FONT));
            }
		});
		url.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	Base64Frame.getInstance(instance).showUrlTab();
            	Base64Frame.getInstance(instance).setBackColor();
            	Base64Frame.getInstance(instance).setVisible(true);
            	Base64Frame.getInstance(instance).setFont((Font)ConfigUtil.getConfInfo().get(Const.SQL_FONT));
            }
		});
		hash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	Base64Frame.getInstance(instance).showHashTab();
            	Base64Frame.getInstance(instance).setBackColor();
            	Base64Frame.getInstance(instance).setVisible(true);
            	Base64Frame.getInstance(instance).setFont((Font)ConfigUtil.getConfInfo().get(Const.SQL_FONT));
            }
		});
		
		//系统托盘，右键弹出式菜单
        final PopupMenu pop = new PopupMenu(); 
        MenuItem show = new MenuItem("Show");
        MenuItem exit = new MenuItem("Exit");
        show.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
            	 instance.setVisible(true); // 显示窗口
            }
        });
        exit.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // 退出程序
            }
        });
        pop.add(show);
        pop.add(exit);
        
        //创建一个自定义托盘，并为自定义托盘指定右键菜单。
        trayIcon = new TrayIcon(ImageIcons.ico_png.getImage(), "工具箱", pop);
        try {
        	//将自定义托盘加入系统托盘列表
			tray.add(trayIcon);
		} catch (AWTException e) {
			JOptionPane.showMessageDialog(null, "当前系统不支持托盘！");
		}
        //托盘悬浮提示
		trayIcon.setToolTip("工具箱");
		//托盘主动冒泡提示信息
		trayIcon.displayMessage("提示", trayIcon.getToolTip(), MessageType.INFO);
		//为托盘添加鼠标监听事件
        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	if(e.getButton()==1){

            		//如果当前处于不可见状态，则置为可见。
            		if(!instance.isVisible()){
            			instance.setVisible(true); 		
            		
            			//如果当前是可见状态，但是处于最小化任务栏时的场景
            		}else if(instance.isVisible() 
            				&&(instance.getExtendedState()==1 //正常大小状态时，最小化任务栏
            				||instance.getExtendedState()==7)){ //最大化状态时，最小化任务栏
            			instance.setState(0);//还原窗口
            		}else{
            			//如果当前处于正常可见状态，则置为不可见。
            			instance.setVisible(false);
            		}
            	}
            }
        });
		
		//设置点击窗口关闭按钮时，什么也不做。
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		//为窗口添加监听事件，自己处理关闭事件
    	this.addWindowListener(new WindowAdapter(){

    		//移除托盘图标
        	//tray.remove(trayIcon);
        	
    		//windowClosing事件：当用户点击窗口右上角的关闭按钮时触发。
			public void windowClosing(WindowEvent arg0) {
				//（1：关闭，2：最小化到托盘，3：弹出供用户选择）
				String action = ConfigUtil.getConfInfo().get(Const.CLOSE_ACTION)+"";
				if ("1".equals(action)) {
					ConnUtil.getInstance().closeConnection();
					System.exit(0);
				} else if ("2".equals(action)) {
					IndexFrame.getInstance().dispose();
				}else{
					CloseChoice.getInstance(getInstance(), true).setVisible(true);
				} 
			}
    	});
	}
}
