
package test.tool.gui.dbtool.frame;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.jvnet.substance.SubstanceDefaultTreeCellRenderer;

import test.tool.gui.common.FontSet;
import test.tool.gui.common.MyColor;
import test.tool.gui.common.SysFontAndFace;
import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.dialog.CloseChoice;
import test.tool.gui.dbtool.dialog.ConnectDialog;
import test.tool.gui.dbtool.dialog.FilterDialog;
import test.tool.gui.dbtool.dialog.JWindowTips;
import test.tool.gui.dbtool.dialog.SetDialog;
import test.tool.gui.dbtool.dialog.SetValue;
import test.tool.gui.dbtool.image.ImageIcons;
import test.tool.gui.dbtool.mycomponent.MyJTable;
import test.tool.gui.dbtool.mycomponent.MyJTableCellJCheckBoxEditor;
import test.tool.gui.dbtool.mycomponent.MyJTableCellJCheckBoxRender;
import test.tool.gui.dbtool.mycomponent.MyJTableHeaderCellJCheckBoxRender;
import test.tool.gui.dbtool.mycomponent.MyJTextField;
import test.tool.gui.dbtool.mycomponent.MyJextArea;
import test.tool.gui.dbtool.mycomponent.MyTableModel;
import test.tool.gui.dbtool.util.CSVUtil;
import test.tool.gui.dbtool.util.ColorUtil;
import test.tool.gui.dbtool.util.ConfigUtil;
import test.tool.gui.dbtool.util.ConnUtil;
import test.tool.gui.dbtool.util.DBUtil;
import test.tool.gui.dbtool.util.EncodingDetect;
import test.tool.gui.dbtool.util.KeyWordUtil;
import test.tool.gui.dbtool.util.LocationUtil;
import test.tool.gui.dbtool.util.MathUtil;
import test.tool.gui.dbtool.util.SkinUtil;
import test.tool.gui.dbtool.beans.DataSourceInfo;
import test.tool.gui.dbtool.beans.FieldInfo;
import test.tool.gui.dbtool.beans.IndexBean;
import test.tool.gui.dbtool.beans.SqlResult;

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MainFrame.class);

	//数据库连接的默认值信息。
	private DataSourceInfo info = null;
	public DataSourceInfo getDataSourceInfo(){
		return this.info;
	}
	
	//jTree当前选中节点Value值
	private Object currentSelectedNodeValue = null;
	private Object currentSelectedCellValue = null;
	private String currentPre = null;
	private boolean isOnlyFieldTips = false;
	
	//最小size
	private final Dimension miniSize =  new Dimension(0,0);
	
	//分隔栏初始位置
	private int init_left_right_split_location = 200;
	private int init_top_bottom_split_location = 150;
	
	//分隔栏宽度
	private int dividerSize = 8;
	
	//记录分隔栏位置，最大化、还原时用。
	private int pre_left_right_split_location = 150;
	private int pre_top_bottom_split_location = 150;
	
    // 获得本操作系统托盘的实例
    private static SystemTray tray = SystemTray.getSystemTray(); 
    public static TrayIcon trayIcon = null;
    
    //Jtable单元格的默认文本编辑器
    DefaultCellEditor objectDefaultCellEditor = new DefaultCellEditor(new MyJTextField());
   
    //第一列为选择列，展现形式为复选框
    MyJTableCellJCheckBoxRender jtableCellJcheckRender =  new MyJTableCellJCheckBoxRender();
    MyJTableCellJCheckBoxEditor jtableCellJcheckEditor = new MyJTableCellJCheckBoxEditor(new JCheckBox());
    
	/*
	 * 保证本窗体只有一个实例类
	 */
	private static MainFrame instance = null;
	public static MainFrame getInstance(){//不更新数据库连接时，获取本实例方法（本方法不能用于初次访问的场景）
		return instance;
	}
	public static MainFrame getInstance(DataSourceInfo IN_info){//更新数据库连接时，获取本实例方法。
	
		// 初次访问的场景
		if(instance==null){
			instance = new MainFrame(IN_info);
			int location[] = LocationUtil.getCenterLocation(instance);//设置居中显示
			instance.setLocation(location[0],location[1]);
			instance.refreshSQL();// 刷新JTextarea中的sql
			
			//设置系统托盘
	        final PopupMenu pop = new PopupMenu(); // 构造一个右键弹出式菜单
	        MenuItem show = new MenuItem("Show");
	        MenuItem exit = new MenuItem("Exit");
	        pop.add(show);
	        pop.add(exit);
	        trayIcon = new TrayIcon(ImageIcons.ico_png.getImage(), "DB工具", pop);
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
	        //-------添加全局事件监听，当JtextArea坐标发生变化时，则同步更新提示窗口的位置--------
	        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
				public void eventDispatched(AWTEvent event) {	
					try {
						if(instance.jwindow_help != null && instance.jwindow_help.isVisible()){					
							Rectangle r1 = instance.jTextArea1.modelToView(instance.jTextArea1.getCaretPosition());
							Point jtextAreaPoint = instance.jTextArea1.getLocationOnScreen();
							instance.jwindow_help.setLocation(jtextAreaPoint.x+r1.x,jtextAreaPoint.y+r1.y+15);
						}
					} catch (BadLocationException e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e);
						}
					}			
				}},AWTEvent.HIERARCHY_BOUNDS_EVENT_MASK);// 用于选择层次结构边界事件的事件掩码。
	     
	        //--------------SQL编辑器中，切换输入法时，调整是否启用智能提示（中文不提示，其他提示）----------------------
//	        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
//				public void eventDispatched(AWTEvent event) {	
//					if(instance.jTextArea1 == event.getSource()){
//						instance.jButton_tips.setSelected(false);
//					}
//				}},AWTEvent.INPUT_METHOD_EVENT_MASK);// 用于选择输入方法事件的事件掩码。
	       
	        
	        //设置主窗口的最小size为其最佳size的大小（当size较小时，布局等信息会乱，或遮挡）
	        instance.setMinimumSize(instance.getSize());
		}else{
			instance.info = IN_info;//更新info信息
			instance.clear(true);//清除缓存信息
		}
		 try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
				JOptionPane.showMessageDialog(null, "当前系统不支持托盘！");
			}
			
		instance.refreshTitle();//刷新db窗体的标题
		trayIcon.setToolTip(instance.getTitle());//更新托盘图标的提示信息
		trayIcon.displayMessage("提示", trayIcon.getToolTip(), MessageType.INFO);//托盘主动提示信息
		
		//刷新Jtree
		instance.refreshJtree(null);

		//分隔栏的初始位置
		instance.jSplitPane_left_right.setDividerLocation(instance.init_left_right_split_location);
		
		//根据当前数据库类型刷新关键字列表
		KeyWordUtil.refreshKeyWordList();

		return instance;
	}
	/*
	 * 构造函数
	 */
	private MainFrame(DataSourceInfo info) {
		
		//接收传递来的info信息
		this.info = info;
		
		//设置标题栏图标
		this.setIconImage(ImageIcons.ico_png.getImage());
		
		//初始化组件信息
		initComponents();
		this.setSize(1024, 768);//设置窗口初始大小
		
		//创建定时器
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable(){
					public void run() {
						MemoryUsage mu = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
						long getUsed = mu.getUsed()/(1024*1024);
						jProgressBar_jvm.setValue((int)getUsed);	
						if(getUsed>(mu.getMax()/(1024*1024)/2)){	
							jProgressBar_jvm.setForeground(Color.RED);//当JVM使用内存超过最大内存的一半时，则将进度条颜色置为红色，并自动执行垃圾回收操作。
							System.gc() ;//执行垃圾回收
						}else{
							jProgressBar_jvm.setForeground(Color.BLUE);//JVM使用内存如未超过最大内存的一半，则进度条颜色显示为蓝色
						}
					}
				});
			}
			
		}, 500,3000);//每3秒检查一次
	}
	//LinkedHashMap 是有序的，从而能保证显示执行结果的顺序与执行的sql的顺序保持一尺，故此处使用LinkedHashMap，而不是使用HashMap
	//result/show_componts/sqlMap/jtableSorter 四者 的key相同。
	private Map<String, String> result = new LinkedHashMap<String, String> ();//用于保存执行结果信息（含成功和失败）
	
	//用于保存执行结果的展现组件：Jtable、Jlabel、JtextArea
	private Map<String, Object> show_componts = new LinkedHashMap<String, Object> ();
	
	//用于保存执行的sql
	private Map<String, String> sqlMap = new LinkedHashMap<String, String> ();
	
	//用于保存jtable的排序器，以便动态的启用排序、停止排序
	private Map<String, RowSorter<MyTableModel>> jtableSorterMap = new LinkedHashMap<String, RowSorter<MyTableModel>> ();
	
	private IDatabaseConnection dbUnitconnection = null;
	private MyJextArea jTextArea1 = new MyJextArea(true);
    private JButton jButton_do = new JButton("执行",ImageIcons.go_png_24);
    private JButton jButton_connect = new JButton("切换连接",ImageIcons.key_png24);
    private JButton jButton_clear = new JButton("清空",ImageIcons.empty_png_24);;
   
    private JMenuBar jMenuBar1 = new JMenuBar();
    //jScrollPane1 用于放置 sql编辑器  jTextArea1
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JMenu jMenu_help = new JMenu("关于");
    private JMenuItem connectme = new JMenuItem("关于");
    
	public JWindowTips jwindow_help;//自动补全window窗口
	
    private JTabbedPane jTabbedPane1;//选项卡面板
    
    //面板右下角，工具图标
    private javax.swing.JLabel jLabel_add = new JLabel(ImageIcons.add_png);
    private javax.swing.JLabel jLabel_delete = new JLabel(ImageIcons.delete_png);
    private javax.swing.JLabel jLabel_save = new JLabel(ImageIcons.save_png_16);;
    private JLabel jLabel_filter = new JLabel(ImageIcons.find_png16);
    private JLabel jLabel_excel = new JLabel(ImageIcons.xls_png);
    private JLabel jLabel_html = new JLabel(ImageIcons.ie_png_16);
    private JLabel jLabel_refresh = new JLabel(ImageIcons.go_png_16);
    private JLabel jLabel_sql = new JLabel(ImageIcons.sql_png);;
    private JLabel jLabel_sql_gen_update = new JLabel(ImageIcons.sql_png);;
    
    //行号
    private LineNumber lineNumber = new LineNumber();
    
    //右键菜单--叶子节点--表节点
    private JPopupMenu jPopupMenu_table_node = new JPopupMenu();
    private JMenuItem copy_table_name = new JMenuItem("复制表名称");
    private JMenuItem query_table_data = new JMenuItem("查询/编辑表数据");
    private JMenuItem query_table_stru = new JMenuItem("查看表结构");
    private JMenuItem query_table_index = new JMenuItem("查询表索引");
    private JMenuItem query_sub_table = new JMenuItem("查询子表信息");
    private JMenuItem generate_insert_sql = new JMenuItem("生成插入SQL");
    private JMenuItem generate_create_table_sql = new JMenuItem("生成建表SQL");
    private JMenuItem disableFK = new JMenuItem("禁用当前表外键");
    private JMenuItem enableFK = new JMenuItem("启用当前表外键");
    private JMenuItem export_table = new JMenuItem("导出");
    private JMenuItem drop_table = new JMenuItem("删除");
   
    //右键菜单--叶子节点--视图节点
    private JPopupMenu jPopupMenu_view_node = new JPopupMenu();
    private JMenuItem copy_view_name = new JMenuItem("复制视图名称");
    private JMenuItem query_view_data = new JMenuItem("查看视图数据");
    private JMenuItem query_view_stru = new JMenuItem("查看视图结构");
    private JMenuItem query_view_sql = new JMenuItem("生成创建视图sql");
    private JMenuItem drop_view = new JMenuItem("删除");
    
    //右键菜单--非叶子节点
    private JPopupMenu jPopupMenu_noneLeaf_node = new JPopupMenu();
    private JMenuItem jmenuItem_expand = new JMenuItem("展开节点");
    private JMenuItem jmenuItem_collapse = new JMenuItem("合拢节点");
    private JMenuItem jmenuItem_refresh = new JMenuItem("刷新");
    
    //常用工具 菜单
    private JMenu jMenu_caozuo = new JMenu("工具箱");
    private JMenuItem jMenuItem1_notepad;//记事本
    private JMenuItem jMenuItem1_weather;//天气预报
    private JMenuItem jMenuItem1_translate;//在线翻译
    
    //连接 菜单
    private JMenu jMenu_lianjie = new JMenu("连接");
    private JMenuItem jMenuItem_qiehuanlianjie = new JMenuItem("切换连接",ImageIcons.key_gif);
    
    
    //导入  菜单
    private JMenu jMenu_import;
    private JMenuItem jMenuItem_import_excel;//导入excel
    private JMenuItem jMenuItem_import_xml;//导入xml
    private JMenuItem jMenuItem_import_sql;//导入sql
    private JMenuItem jMenuItem_import_csv;//导入csv
    
    //导出  菜单
    private JMenu jMenu_export;
    private JMenuItem jMenuItem_export;
    
    //查看菜单
    private JMenu jMenu_view = new JMenu("查看");
    private JMenuItem view_log = new JMenuItem("系统日志",ImageIcons.detail_gif);
    private JMenuItem shuoming = new JMenuItem("使用说明");
    private JMenuItem jMenuItem_view_DBInfo = new JMenuItem("数据库信息",ImageIcons.detail_gif);
    
    //设置 菜单
    private JMenu jMenu_set = new JMenu("设置");;
    private JMenu jMenu_font_set = new JMenu("字体");;//字体设置
    private JMenuItem jMenuItem_JtextArea_font_set;//编辑器字体设置
    private JMenuItem jMenuItem_Jtable_font_set;//列表字体设置
    private JMenuItem jMenuItem_Jtree_font_set;//树字体设置

    //工具栏
    private JToolBar jToolBar1;
    private JButton jButton_tips = new JButton("自动补全");//启用智能提示
    private JButton jButton_autoCommit = new JButton("事务自动提交");//事务自动提交
    private JButton jButton_saveAs= new JButton("另存为",ImageIcons.saveas_png_24);
    private JButton jButton_zhushi= new JButton("注释",ImageIcons.disable_png_24);
    private JButton jButton_redo = new JButton("恢复",ImageIcons.redo_png_24);
    private JButton jButton_undo = new JButton("撤销",ImageIcons.undo_png_24);
    private JButton jButton_find = new JButton("查找/替换",ImageIcons.find_png24);
    private JButton jButton_gotoline = new JButton("定位行",ImageIcons.gotoline_png24);
    private JButton jButton_moveup = new JButton("上移",ImageIcons.moveup_png24);
    private JButton jButton_movedown = new JButton("下移",ImageIcons.movedown_png24);
    private JTree jTree1;//左侧导航树
    
	//分隔panel
	private JSplitPane jSplitPane_top_bottom = new JSplitPane();
	private JSplitPane jSplitPane_left_right = new JSplitPane();
	
	//耗时信息
	private JLabel jLabel_time = new JLabel();
	
	//JVM进度条
	 private JProgressBar jProgressBar_jvm = new JProgressBar(JProgressBar.HORIZONTAL);;
	
    //Jtable右键菜单
	private JPopupMenu jPopupMenu_table = new JPopupMenu();
	private JMenuItem jtable_right_copy = new JMenuItem("复制【当前单元格值】");//复制--单元格value
	private JMenuItem linewrapshow = new JMenuItem("换行显示【当前单元格值】");//换行显示

    //完成初始化
    private void initComponents() {
    	
    	//为窗口添加监听事件
    	this.addWindowListener(new WindowAdapter(){

    		//windowClosing事件：当用户点击窗口右上角的关闭按钮时触发。
			public void windowClosing(WindowEvent arg0) {
				
				//1、保存sql脚本
				saveSql();
				
				//2、判断是否有默认关闭方式
				String action = ConfigUtil.getConfInfo().get(Const.CLOSE_ACTION)+"";
				if("1".equals(action)){
					ConnUtil.getInstance().closeConnection();//关闭数据库连接
					System.exit(0);//退出应用程序
				}else if("2".equals(action)){
					instance.setVisible(false);
				}else{			
					//3、如果没有默认关闭方式，则弹出"选择关闭方式"对话框，供用户选择。
					CloseChoice.getInstance(getInstance(), true).setVisible(true);
				}		
			}
    	});
    	this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      
    	/*
    	 * 知识点：大部分时候，事件处理器都没有什么复用价值（可复用代码通常都被抽象成了业务逻辑方法），
    	 * 因此大部分事件监听器只是临时使用一次，所以使用匿名内部类形式的事件监听器是较为推荐的方式。
    	 * 使用方式：
    	 * 1、new 事件监听器接口    
    	 * 2、new 事件适配器
    	 */
  	  
    	// table树节点右键菜单
		jPopupMenu_table_node.add(copy_table_name);
		copy_table_name.setIcon(ImageIcons.copy_gif);
		copy_table_name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//复制表名称
	 			Transferable contents = new StringSelection(currentSelectedNodeValue+"");
	 			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
	 			clip.setContents(contents, null);
			}
		});
		jPopupMenu_table_node.addSeparator();//添加分割线
		jPopupMenu_table_node.add(query_table_data);
		query_table_data.setIcon(ImageIcons.find_png16);
		query_table_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "select * from " + currentSelectedNodeValue;
				doAction(sql, false, 0, null);	
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
					int length = jTextArea1.getText().length();
					jTextArea1.append("\n" + sql + ";");
					jTextArea1.setSelectionStart(length+1);
					jTextArea1.setSelectionEnd(length+1+sql.length()+1);
				}
			}
		});
		jPopupMenu_table_node.addSeparator();//添加分割线
		jPopupMenu_table_node.add(query_table_stru);
		query_table_stru.setIcon(ImageIcons.find_png16);
		query_table_stru.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "desc " + currentSelectedNodeValue;
				doAction(sql, false, 0, null);	
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
					int length = jTextArea1.getText().length();
					jTextArea1.append("\n" + sql + ";");
					jTextArea1.setSelectionStart(length+1);
					jTextArea1.setSelectionEnd(length+1+sql.length()+1);
				}
			}
		});
		jPopupMenu_table_node.addSeparator();//添加分割线
		jPopupMenu_table_node.add(query_table_index);
		query_table_index.setIcon(ImageIcons.find_png16);
		query_table_index.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "index " + currentSelectedNodeValue;
				doAction(sql, false, 0, null);	
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
					int length = jTextArea1.getText().length();
					jTextArea1.append("\n" + sql + ";");
					jTextArea1.setSelectionStart(length+1);
					jTextArea1.setSelectionEnd(length+1+sql.length()+1);
				}
			}
		});
		jPopupMenu_table_node.addSeparator();//添加分割线
		jPopupMenu_table_node.add(query_sub_table);
		query_sub_table.setIcon(ImageIcons.find_png16);
		query_sub_table.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "getChildren " + currentSelectedNodeValue;
				doAction(sql, false, 0, null);	
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
					int length = jTextArea1.getText().length();
					jTextArea1.append("\n" + sql + ";");
					jTextArea1.setSelectionStart(length+1);
					jTextArea1.setSelectionEnd(length+1+sql.length()+1);
				}
			}
		});
		jPopupMenu_table_node.addSeparator();//添加分割线
		jPopupMenu_table_node.add(generate_insert_sql);
		generate_insert_sql.setIcon(ImageIcons.sql_png);
		generate_insert_sql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "generateInsertSql " + currentSelectedNodeValue;
				doAction(sql, false, 0, null);	
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
					int length = jTextArea1.getText().length();
					jTextArea1.append("\n" + sql + ";");
					jTextArea1.setSelectionStart(length+1);
					jTextArea1.setSelectionEnd(length+1+sql.length()+1);
				}
			}
		});
		jPopupMenu_table_node.addSeparator();//添加分割线
		jPopupMenu_table_node.add(generate_create_table_sql);
		generate_create_table_sql.setIcon(ImageIcons.sql_png);
		generate_create_table_sql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "generateCreateSqlForTable " + currentSelectedNodeValue;
				doAction(sql, false, 0, null);	
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
					int length = jTextArea1.getText().length();
					jTextArea1.append("\n" + sql + ";");
					jTextArea1.setSelectionStart(length+1);
					jTextArea1.setSelectionEnd(length+1+sql.length()+1);
				}
			}
		});
		//导出当前表
		jPopupMenu_table_node.addSeparator();//添加分割线
		jPopupMenu_table_node.add(export_table);
		export_table.setIcon(ImageIcons.save_png_16);
		export_table.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(currentSelectedNodeValue == null){
					currentSelectedNodeValue = "";
				}
            	ExportFrame export = ExportFrame.getInstance(currentSelectedNodeValue.toString());
            	int location[] = LocationUtil.getCenterLocation(export);
            	export.setLocation(location[0], location[1]);
            	export.setVisible(true);
            }
		});
		jPopupMenu_table_node.addSeparator();//添加分割线
		jPopupMenu_table_node.add(enableFK);
		enableFK.setIcon(ImageIcons.ok_png);
		enableFK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "enableFK " + currentSelectedNodeValue;
				doAction(sql, false, 0, null);	
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
					int length = jTextArea1.getText().length();
					jTextArea1.append("\n" + sql + ";");
					jTextArea1.setSelectionStart(length+1);
					jTextArea1.setSelectionEnd(length+1+sql.length()+1);
				}
			}
		});
		jPopupMenu_table_node.addSeparator();//添加分割线
		jPopupMenu_table_node.add(disableFK);
		disableFK.setIcon(ImageIcons.disable_png_16);
		disableFK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "disableFK " + currentSelectedNodeValue;
				doAction(sql, false, 0, null);	
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
					int length = jTextArea1.getText().length();
					jTextArea1.append("\n" + sql + ";");
					jTextArea1.setSelectionStart(length+1);
					jTextArea1.setSelectionEnd(length+1+sql.length()+1);
				}
			}
		});
		
		jPopupMenu_table_node.addSeparator();//添加分割线
		jPopupMenu_table_node.add(drop_table);
		drop_table.setIcon(ImageIcons.delete_png);
		drop_table.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 int opt = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "确定要删除表："+currentSelectedNodeValue+" ？"
						 +"\n说明：1、DROP, CREATE, ALTER 是DDL语句，一般情况下内置了COMMIT操作，语句一旦执行成功就不能回滚。"
						 +"\n     2、不同数据库厂商对DDL语句事务支持不太一样，详情可可参考相关数据库资料。","确认对话框", JOptionPane.YES_NO_OPTION);		
		    	 if(opt == JOptionPane.YES_OPTION){
		    		 String sql = "drop table " + currentSelectedNodeValue;
						doAction(sql, false, 0, null);	
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
							int length = jTextArea1.getText().length();
							jTextArea1.append("\n" + sql + ";");
							jTextArea1.setSelectionStart(length+1);
							jTextArea1.setSelectionEnd(length+1+sql.length()+1);
						}
						//取得当前选中节点
						DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
						//获取当前选中节点的父节点
						DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectNode.getParent();
						//刷新树
						refreshJtree(parentNode);
		    	 }
			}
		});
		
		// view树节点右键菜单
		jPopupMenu_view_node.add(copy_view_name);//复制视图名称
		copy_view_name.setIcon(ImageIcons.copy_gif);
		copy_view_name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//复制表名称
	 			Transferable contents = new StringSelection(currentSelectedNodeValue+"");
	 			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
	 			clip.setContents(contents, null);
			}
		});
		jPopupMenu_view_node.addSeparator();//添加分割线
		jPopupMenu_view_node.add(query_view_data);//查看视图数据
		query_view_data.setIcon(ImageIcons.find_png16);
		query_view_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "select * from " + currentSelectedNodeValue;
				doAction(sql, false, 0, null);	
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
					int length = jTextArea1.getText().length();
					jTextArea1.append("\n" + sql + ";");
					jTextArea1.setSelectionStart(length+1);
					jTextArea1.setSelectionEnd(length+1+sql.length()+1);
				}
			}
		});
		jPopupMenu_view_node.addSeparator();//添加分割线
		jPopupMenu_view_node.add(query_view_stru);//查看视图结构
		query_view_stru.setIcon(ImageIcons.find_png16);
		query_view_stru.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "desc " + currentSelectedNodeValue;
				doAction(sql, false, 0, null);	
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
					int length = jTextArea1.getText().length();
					jTextArea1.append("\n" + sql + ";");
					jTextArea1.setSelectionStart(length+1);
					jTextArea1.setSelectionEnd(length+1+sql.length()+1);
				}
			}
		});
		jPopupMenu_view_node.addSeparator();//添加分割线
		jPopupMenu_view_node.add(query_view_sql);//查看视图创建sql
		query_view_sql.setIcon(ImageIcons.sql_png);
		query_view_sql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sql = "generateCreateSqlForView " + currentSelectedNodeValue;
				doAction(sql, false, 0, null);	
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
					int length = jTextArea1.getText().length();
					jTextArea1.append("\n" + sql + ";");
					jTextArea1.setSelectionStart(length+1);
					jTextArea1.setSelectionEnd(length+1+sql.length()+1);
				}
			}
		});
		jPopupMenu_view_node.addSeparator();//添加分割线
		jPopupMenu_view_node.add(drop_view);//删除视图
		drop_view.setIcon(ImageIcons.delete_png);
		drop_view.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 int opt = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "确定要删除视图："+currentSelectedNodeValue+" ？"
						 +"\n说明：1、DROP, CREATE, ALTER 是DDL语句，一般情况下内置了COMMIT操作，语句一旦执行成功就不能回滚。"
						 +"\n     2、不同数据库厂商对DDL语句事务支持不太一样，详情可可参考相关数据库资料。","确认对话框", JOptionPane.YES_NO_OPTION);		
		    	 if(opt == JOptionPane.YES_OPTION){
		    		 String sql = "drop view " + currentSelectedNodeValue;
						doAction(sql, false, 0, null);	
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
							int length = jTextArea1.getText().length();
							jTextArea1.append("\n" + sql + ";");
							jTextArea1.setSelectionStart(length+1);
							jTextArea1.setSelectionEnd(length+1+sql.length()+1);
						}
						//取得当前选中节点
						DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
						//获取当前选中节点的父节点
						DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectNode.getParent();
						//刷新树
						refreshJtree(parentNode);
		    	 }
			}
		});
		//非叶子节点右键菜单---刷新
		jPopupMenu_noneLeaf_node.add(jmenuItem_refresh);
		jmenuItem_refresh.setIcon(ImageIcons.refresh_gif);
		jmenuItem_refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				//取得当前选中节点
				DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
				refreshJtree(selectNode);
			}
		});
		//非叶子节点右键菜单---展开
		jPopupMenu_noneLeaf_node.add(jmenuItem_expand);
		jmenuItem_expand.setIcon(ImageIcons.expand_gif);
		jmenuItem_expand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				expandNode(jTree1,jTree1.getSelectionPath(),true);
			}
		});
		//非叶子节点右键菜单---合拢
		jPopupMenu_noneLeaf_node.add(jmenuItem_collapse);
		jmenuItem_collapse.setIcon(ImageIcons.collapsel_gif);
		jmenuItem_collapse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				expandNode(jTree1,jTree1.getSelectionPath(),false);
			}
		});

        //设置自动换行
        //jTextArea1.setLineWrap(true);//默认不自动换行
        
        //为jTextArea1.find添加事件
        jTextArea1.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindDialog(jTextArea1);
			}   	
        });    
        jScrollPane1.setViewportView(jTextArea1);
        
        /*
         * 为jTextArea1设置键盘监听事件
         * 1、智能提示
         * 2、Ctrl+回车    执行sql
         * 3、Ctrl+“/”  添加删除注释
         * 4、Ctrl+F  弹出查找替换对话框
         * 5、Ctrl+S  弹出另存为对话框
         * 6、Ctrl+T  启用  or 禁用智能提示
         */
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
        	
        	//键盘按下
			public void keyPressed(java.awt.event.KeyEvent evt) {

				if (evt.isControlDown()) {
					if (evt.getKeyCode() == KeyEvent.VK_ENTER) {// ctrl+enter 执行sql
						doAction(null, false, 0, null);	
					}else if (evt.getKeyCode() == KeyEvent.VK_F) {// ctrl+f 执行查找替换													// 
						showFindDialog(jTextArea1);
					} else if (evt.getKeyCode() == KeyEvent.VK_SLASH) {//Ctrl+“/”  添加删除注释
						addDeleteZhuShi();
					} else if (evt.getKeyCode() == KeyEvent.VK_S) {//Ctrl+S  弹出另存为对话框
						saveAsSqlFile();
					}else if (evt.getKeyCode() == KeyEvent.VK_T) {//Ctrl+T  启用  or 禁用智能提示
						enable_or_disableTip();
					}else if (evt.getKeyCode() == KeyEvent.VK_L) {//Ctrl+L  弹出定位行对话框
						showLocationLineDialog(jTextArea1);
					}
				}
			}
          //键盘释放
            public void keyReleased(java.awt.event.KeyEvent evt) {          	
            	//智能提示
            	if(isEnableTip()){      		
            		tips(evt);
            	}
            }
        });   
        /*
         * jTextArea1添加鼠标监听事件
         * 1、jTextArea1中任意处鼠标点击，则隐藏智能提示窗口。
         */
        jTextArea1.addMouseListener(new MouseAdapter(){
        	 public void mouseClicked(MouseEvent e) {
        		 if(jwindow_help!=null){		 
        			 jwindow_help.setVisible(false);
        		 }
        	 }
        });
     /*
      * 知识点：所有包含多个方法的监听器接口都有一个对应的适配器，但只包含一个方法的监听器接口则没有对应的适配器。   
      * 事件适配器是事件监听器的一个空的实现，事件适配器实现了事件监听器接口，并为接口里面的每个方法都提供了实现，这种实现是一种空实现，
      * 即方法体内没有任何代码的实现。
      */
        jButton_do.setToolTipText("快捷键：Ctrl+Enter");
        jButton_do.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	doAction(null, false, 0, null);	
            }
        });
       
        //-----------为菜单栏添加菜单-------------------------
      
        //皮肤设置
        JMenu jMenu_skin_set = new JMenu("皮肤");//皮肤设置
        String defaultSkin = ConfigUtil.getConfInfo().get(Const.SKIN).toString();
       
        //动态生成menuItem
        ButtonGroup btgp = new ButtonGroup();
        List<String> skinNameList = SkinUtil.getSkinNames();
        for(String skinName:skinNameList){
        	JRadioButtonMenuItem skinMenuItem = new JRadioButtonMenuItem(skinName);
        	
        	//设置当前皮肤的选框为选中状态
			if(skinName.equals(defaultSkin)){
				skinMenuItem.setSelected(true);
			}
			//为每个菜单项设置事件
			skinMenuItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {					
					
					//设置新的背景色
					String skinName = e.getActionCommand();
					JOptionPane.showMessageDialog(getInstance(), SkinUtil.setSkin(skinName));
	            	
					//更新至磁盘
	            	ConfigUtil.getConfInfo().put(Const.SKIN, skinName);
	            	ConfigUtil.updateConfInfo();
				}  	
	        });
			//将菜单项加入菜单
			jMenu_skin_set.add(skinMenuItem);
        	//将菜单项加入buttonGroup
        	btgp.add(skinMenuItem);
        }

        //设置---背景色
        JMenu jMenuBgColor = new JMenu("背景色");
        
        //设置默认背景色
        MyColor defaultColor = (MyColor)ConfigUtil.getConfInfo().get(Const.EYE_SAFETY_COLOR);
        jTextArea1.setBackground(defaultColor.getColor());
        
        //动态生成menuItem
        ButtonGroup btgp_color = new ButtonGroup();
        List<String> colorNameList = ColorUtil.getColorNameList();
        for(String colorName:colorNameList){
        	JRadioButtonMenuItem colorMenuItem = new JRadioButtonMenuItem(colorName);
        	
        	//设置当前背景色的选框为选中状态
			if(colorName.equals(defaultColor.getColorChineseName())){
				colorMenuItem.setSelected(true);
			}
			//为每个菜单项设置事件
			colorMenuItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {					
					
					String colorName = e.getActionCommand();
					MyColor myColor = ColorUtil.getColor(colorName);
					Color color = myColor.getColor();
	            	
					//设置文本域背景色
					jTextArea1.setBackground(color) ;
	            	
	            	//更新jtable背景色
	            	List<MyJTable> jtables = getJtables();
            		if(jtables!=null && jtables.size()>0){
            			for(MyJTable jtable:jtables){
            				jtable.setBackground(color) ;
            			}
            		}	
	            	
	            	if(jTree1!=null){  //jTree1可能初始化较晚，故加此判断。  
	            		
	            		//树 背景色
	            		jTree1.setBackground(color);
	            		
	            		//树节点背景色
	            		((DefaultTreeCellRenderer)jTree1.getCellRenderer()).setBackgroundNonSelectionColor(color);
	            	}
	            	//更新至磁盘
	            	ConfigUtil.getConfInfo().put(Const.EYE_SAFETY_COLOR, myColor);
	            	ConfigUtil.updateConfInfo();
				}  	
	        });
			//将菜单项加入菜单
        	jMenuBgColor.add(colorMenuItem);
        	//将菜单项加入buttonGroup
        	btgp_color.add(colorMenuItem);
        }

        //菜单--设置--其他设置
        JMenuItem jMenuItem_other_set = new JMenuItem("其他设置...");
        jMenuItem_other_set.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	SetDialog.getInstance(getInstance(), true).setVisible(true);
            }
        });
       
        //菜单--设置---Jtextarea设置字体
        jMenuItem_JtextArea_font_set = new JMenuItem("编辑器字体...",ImageIcons.txt_gif);
        jMenuItem_JtextArea_font_set.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { 
            	Font oldFont = null;
            	if(ConfigUtil.getConfInfo().get(Const.SQL_FONT)!=null){
            		oldFont = (Font)ConfigUtil.getConfInfo().get(Const.SQL_FONT);
            	}
            	Font newFont = FontSet.showFontSetDialog(instance, oldFont);
            	if(newFont!=null){	
            		
            		//行号字体与Jtextarea字体保持一致。
            		jTextArea1.setFont(newFont);
            		lineNumber.setFont(newFont);
            		
            		//更新至磁盘
            		ConfigUtil.getConfInfo().put(Const.SQL_FONT, jTextArea1.getFont());
            		ConfigUtil.updateConfInfo();
            	}	
            }
        });
        jMenu_font_set.add(jMenuItem_JtextArea_font_set);
        
        //菜单--设置--Jtable字体设置
        jMenuItem_Jtable_font_set = new JMenuItem("列表字体...",ImageIcons.table_gif);
        jMenuItem_Jtable_font_set.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { 
            	Font oldFont = null;
            	if(ConfigUtil.getConfInfo().get(Const.JTABLE_FONT)!=null){
            		oldFont = (Font)ConfigUtil.getConfInfo().get(Const.JTABLE_FONT);
            	}
            	Font newFont = FontSet.showFontSetDialog(instance,oldFont);
            	if(newFont!=null){	
            		List<MyJTable> jtables = getJtables();//更新所有Jtable的字体
            		if(jtables!=null&&jtables.size()>0){
            			for(int i=0;i<jtables.size();i++){
            				jtables.get(i).setFont(newFont);
            			}
            		}	
            		//更新至磁盘
            		ConfigUtil.getConfInfo().put(Const.JTABLE_FONT, newFont);
            		ConfigUtil.updateConfInfo();
            	}	
            }
        });
        jMenu_font_set.add(jMenuItem_Jtable_font_set);

        //菜单--设置---Jtree设置字体
        jMenuItem_Jtree_font_set = new JMenuItem("左侧树字体...",ImageIcons.tree_png16);
        jMenuItem_Jtree_font_set.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) { 
            	Font oldFont = (Font)ConfigUtil.getConfInfo().get(Const.JTREE_FONT);
            	Font newFont = FontSet.showFontSetDialog(instance, oldFont);
            	if(newFont!=null){	
            		ConfigUtil.getConfInfo().put(Const.JTREE_FONT, newFont);
            		ConfigUtil.updateConfInfo();
            		refreshJTreeFont();
            	}	
            }
        });
        jMenu_font_set.add(jMenuItem_Jtree_font_set);
        
        //为“连接”菜单添加 子菜单 和 菜单项
        jMenu_lianjie.add(jMenuItem_qiehuanlianjie);
        jMenuItem_qiehuanlianjie.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				changeConn();
			}
        });

        //菜单项--打开记事本
        jMenuItem1_notepad = new JMenuItem("记事本",ImageIcons.txt_gif);
        jMenu_caozuo.add(jMenuItem1_notepad);
        jMenuItem1_notepad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	MyNotePad notePad = new MyNotePad(getInstance(),null,null,null);
            	notePad.setVisible(true);
            }
        });
       
      //菜单项--打开网络天气预报
        jMenuItem1_weather = new JMenuItem("天气预报",ImageIcons.weather_gif);
        jMenu_caozuo.add(jMenuItem1_weather);
        jMenuItem1_weather.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String path = "http://flash.weather.com.cn/wmaps/index.swf?url1=http%3A%2F%2Fwww%2Eweather%2Ecom%2Ecn%2Fweather%2F&url2=%2Eshtml&from=cn";
            	try {
            		Desktop desk=Desktop.getDesktop(); //用默认浏览器打开url
            		desk.browse(new URI(path)); 
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.toString());
				} catch (URISyntaxException e) {
					JOptionPane.showMessageDialog(null, e.toString());
				}
            }
        });
        //菜单项--打开google在线翻译
        jMenuItem1_translate = new JMenuItem("在线翻译",ImageIcons.ie_png_16);
        jMenuItem1_translate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String path = "http://translate.google.cn";
            	try {
            		Desktop desk=Desktop.getDesktop(); //用默认浏览器打开url
            		desk.browse(new URI(path)); 
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e.toString());
				} catch (URISyntaxException e) {
					JOptionPane.showMessageDialog(null, e.toString());
				}
            }
        });
        jMenu_caozuo.add(jMenuItem1_translate);
       
        //菜单项--退出
        JMenuItem exit = new JMenuItem("退出",ImageIcons.exit_png);
        exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				saveSql();//退出之前，首先保存sql编辑器中的sql脚本
				System.exit(0);
			}	
        });
        jMenu_caozuo.addSeparator();
        jMenu_caozuo.add(exit);
        
        //初始化 导入菜单
        jMenu_import = new JMenu();
        jMenu_import.setText("导入");
        jMenuItem_import_excel = new JMenuItem();
        jMenuItem_import_excel.setText("导入 EXCEL");
        jMenuItem_import_excel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	importExcel();
            }
        });
        jMenuItem_import_xml = new JMenuItem();
        jMenuItem_import_xml.setText("导入 XML");
        jMenuItem_import_xml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importXml();
            }
        });
        jMenuItem_import_csv = new JMenuItem();
        jMenuItem_import_csv.setText("导入 CSV");
        jMenuItem_import_csv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importCSV();
            }
        });
        
        jMenuItem_import_sql = new JMenuItem();
        jMenuItem_import_sql.setText("导入 SQL");
        jMenuItem_import_sql.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	importSql();
            }
        });
        jMenu_import.add(jMenuItem_import_excel);
        jMenu_import.add(jMenuItem_import_xml);
        jMenu_import.add(jMenuItem_import_sql);
        jMenu_import.add(jMenuItem_import_csv);
        
        //初始化 导出菜单
        jMenu_export = new JMenu();
        jMenu_export.setText("导出");
        jMenuItem_export = new JMenuItem("导出...");
        jMenuItem_export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	if(currentSelectedNodeValue == null){
            		currentSelectedNodeValue = "";
				}
            	ExportFrame export = ExportFrame.getInstance(currentSelectedNodeValue.toString());
            	int location[] = LocationUtil.getCenterLocation(export);
            	export.setLocation(location[0], location[1]);
            	export.setVisible(true);
            }
        });
        jMenu_export.add(jMenuItem_export);
       
        //帮助菜单
        jMenu_help.add(connectme);
        
        //查看菜单
        jMenu_view.add(shuoming);
        jMenu_view.add(view_log);
        jMenu_view.add(jMenuItem_view_DBInfo);
        
        jMenuItem_view_DBInfo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				MyNotePad notePad = new MyNotePad(getInstance(),"当前连接数据库信息",DBUtil.getDBProductInfo().toString(),null);
				notePad.setVisible(true);
			}
        });
       
        //菜单--设置
        jMenu_set.add(jMenu_font_set); //字体
        jMenu_set.add(jMenu_skin_set);//皮肤
        jMenu_set.add(jMenuBgColor);//背景色
        jMenu_set.add(jMenuItem_other_set);//其他设置
        
        
        //将菜单放入菜单栏
        jMenuBar1.add(jMenu_caozuo);//将“常用工具”菜单放入菜单栏
        jMenuBar1.add(jMenu_lianjie);//将“连接”菜单放入菜单栏
        jMenuBar1.add(jMenu_export);//将导出菜单放入菜单栏
        jMenuBar1.add(jMenu_import);//将导入菜单放入菜单栏
        jMenuBar1.add(jMenu_set);//将“设置”菜单放入菜单栏
        jMenuBar1.add(jMenu_view);//将“查看”菜单放入菜单栏
        jMenuBar1.add(jMenu_help);//将帮助菜单放入菜单栏
        setJMenuBar(jMenuBar1);
        
        shuoming.setIcon(ImageIcons.detail_gif);
        shuoming.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	StringBuilder memo = new StringBuilder();
            	memo.append("1、快捷键");
            	memo.append("\n");
            	memo.append("    Ctrl+Enter：执行SQL");
            	memo.append("\n");
            	memo.append("    Ctrl+/：添加、删除注释");
            	memo.append("\n");
            	memo.append("    Ctrl+F：弹出查找、替换对话框");
            	memo.append("\n");
            	memo.append("    Ctrl+L：弹出定位行对话框");
            	memo.append("\n");
            	memo.append("    Ctrl+T：启用、暂停自动补全功能(启用自动补全功能后，SQL编辑器中将只支持英文输入，如需中文输入，须暂停自动补全功能)");
            	memo.append("\n");
            	memo.append("    Ctrl+Home：跳转至第一行起始位置");
            	memo.append("\n");
            	memo.append("    Ctrl+End：跳转至最后一行的结束位置");
            	memo.append("\n\n");
            	memo.append("    ALt+上方向键：当前行或选中行上移一行");
            	memo.append("\n");
            	memo.append("    ALt+下方向键：当前行或选中行下移一行");
            	memo.append("\n\n");
            	memo.append("    Shift+下方向键：下选一行");
            	memo.append("\n");
            	memo.append("    Shift+上方向键：上选一行");
            	memo.append("\n");
            	memo.append("    Shift+左方向键：左选一字符");
            	memo.append("\n");
            	memo.append("    Shift+右方向键：右选一字符");
            	memo.append("\n");
            	memo.append("    Shift+Home键：左选至行起始位置");
            	memo.append("\n");
            	memo.append("    Shift+End键：右选至行结束位置");
            	memo.append("\n\n");
            	memo.append("2、执行方式：默认执行选中的sql命令，如果没有选中，则执行所有sql命令，多个sql命令之间用分号分隔。");
            	memo.append("\n\n");
            	memo.append("3、查看表结构：desc tableName ，如 desc pub_users");
            	memo.append("\n\n");
            	memo.append("4、查看表索引：index tableName ，如 index pub_users");
            	memo.append("\n\n");
            	memo.append("5、查看指定表的子表：getchildren tableName ，如 getchildren pub_users");
            	memo.append("\n\n");
            	memo.append("6、生成插入sql：generateInsertSql tableName ，如 generateInsertSql pub_users");
            	memo.append("\n\n");
            	memo.append("7、生成建表sql：generateCreateSqlForTable tableNameName，如 generateCreateSqlForTable PUB_ORGAN。");
            	memo.append("\n\n");
            	memo.append("8、生成创建视图sql：generateCreateSqlForView viewName，如 generateCreateSqlForView V_STRU_ORGAN。");
            	memo.append("\n\n");
            	memo.append("9、禁用指定表外键约束：disableFK tableName1,tableName2");
            	memo.append("\n\n");
            	memo.append("10、启用指定表外键约束：enableFK tableName1,tableName2");
            	memo.append("\n\n");
            	memo.append("11、启用数据库所有表的外键约束：enableAllFK");
            	memo.append("\n\n");
            	memo.append("12、禁用数据库所有表的外键约束：disableAllFK");
            	memo.append("\n\n");
            	memo.append("13、查询结果列表中：(null)表示查询出来的结果是空 null。");
            	memo.append("\n\n");
            	memo.append("14、双击查询结果列表选项卡Tab，可以最大化显示，再次双击还原原来大小。");
            	memo.append("\n\n");
            	memo.append("15、在SQL编辑器中，选择sql语句后，执行【导出】操作，会自动将选择的sql语句复制到弹出的导出对话框中。");
            	memo.append("\n\n");
            	memo.append("16、关于jre的说明：本软件运行依赖于jre1.6及以上版本，查找jre顺序：");
            	memo.append("\n");
            	memo.append("    1.应用程序同级目录下的jre文件夹");
            	memo.append("\n");
            	memo.append("    2.环境变量 jre_home（配置环境变量后，如不生效，则需重启机器）");
            	memo.append("\n");
            	memo.append("    3.注册表");
            	memo.append("\n\n");
            	memo.append("17、程序启动后，会自动在同级目录下创建config和log两个文件夹，config下存储的是数据源、设置等用户配置信息，log下用于存储日志信息。 ");
            	memo.append("\n\n");
            	memo.append("18、config\\db_driver.properties文件中是各种数据库连接常用配置，用户可自行修改。");
            	memo.append("\n\n");
            	memo.append("19、本软件同级目录的lib文件下自带有DB2、Oracle、SQLServer驱动，用户如需连接其他类型数据库，可将相应的驱动包放到此lib目录中即可");
              
            	memo.append("\n\n");
            	memo.append("20、目前一些自定义功能仅支持 DB2、Oracle、SQLServer、MYSQL数据库");
            	
            	MyNotePad td = new MyNotePad(getInstance(), "使用说明",memo.toString(),null);
            	td.setVisible(true);
            }
        });
        view_log.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {  
               MyNotePad td = new MyNotePad(getInstance(),null,null,Const.LOG);
               td.setVisible(true);
            }
        });
        connectme.setIcon(ImageIcons.connect_gif);
        connectme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectmeActionPerformed(evt);
            }
        });        
    
		//工具栏
		jToolBar1 = new JToolBar();
		jToolBar1.setFloatable(false);//设置是否可以浮动
		jToolBar1.setRollover(true);//工具栏按钮，鼠标滑过特效
		
		//切换连接
	    jButton_connect.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
	       	changeConn();
	        }
	    });
		
		//另存为
	    jButton_saveAs.setToolTipText("快捷键：Ctrl+S");
	    jButton_saveAs.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
	       	saveAsSqlFile();
	        }
	    });
	    
	    //注释
        jButton_zhushi.setToolTipText("快捷键：Ctrl+/");
        jButton_zhushi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	addDeleteZhuShi();
            }
        });

	    //清空
	    jButton_clear.addActionListener(new java.awt.event.ActionListener() {
	       public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	jButton_clearActionPerformed(evt);
	            }
	    });

	    //查找替换
        jButton_find.setToolTipText("快捷键：Ctrl+F");
        jButton_find.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	showFindDialog(jTextArea1);
            }
        });
        //定位行
        jButton_gotoline.setToolTipText("快捷键：Ctrl+L");
        jButton_gotoline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	showLocationLineDialog(jTextArea1);
            }
        });
	        //撤销
	        jButton_undo.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	if (jTextArea1.undomang.canUndo()){
	            		jTextArea1.undomang.undo();	
					}
	            }
	        });

	        //恢复
	        jButton_redo.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	if (jTextArea1.undomang.canRedo()){
	            		jTextArea1.undomang.redo();
					}	
	            }
	        });
	        
	        //上移
	        jButton_moveup.setToolTipText("快捷键：Alt+上方向键");
	        jButton_moveup.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	getInstance().jTextArea1.moveUp();
	            }
	        });
	       
	        //下移
	        jButton_movedown.setToolTipText("快捷键：Alt+下方向键");
	        jButton_movedown.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	getInstance().jTextArea1.moveDown();
	            }
	        });
	        
	        //设置行号 
	        jScrollPane1.setRowHeaderView(lineNumber);
	        
	        //是否启用智能提示
	        jButton_tips.setToolTipText("快捷键：Ctrl+T");
	        boolean isSelected = "true".equals(ConfigUtil.getConfInfo().get(Const.IS_ENABLE_SMART_TIPS)+"");
	        if(isSelected){
	        	jButton_tips.setIcon(ImageIcons.select_png24);
	        	//暂停输入法支持
				jTextArea1.enableInputMethods(false);
	        }else{
	        	jButton_tips.setIcon(ImageIcons.unselect_png24);
	        	//启用输入法支持
				jTextArea1.enableInputMethods(true);
	        }
	        jButton_tips.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					enable_or_disableTip();
				}
	        });
	     
	        //事务是否自动提交
	        isSelected = "true".equals(ConfigUtil.getConfInfo().get(Const.IS_AUTO_COMMIT)+"");
	        if(isSelected){
	        	jButton_autoCommit.setIcon(ImageIcons.select_png24);
	        }else{
	        	jButton_autoCommit.setIcon(ImageIcons.unselect_png24);
	        }
	        jButton_autoCommit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					enable_or_disableAutoCommit();
				}
	        });     
	       
	        //分隔panel
	        jSplitPane_top_bottom.setBorder(null);
	        jSplitPane_top_bottom.setContinuousLayout(true);//打开连续布局属性，打开后不会再有虚拟分割线出现，但是由于不断重绘界面组件，可能会影响性能。
	        jSplitPane_top_bottom.setOrientation(JSplitPane.VERTICAL_SPLIT);//设置分隔方式：上下分隔
	        
	        jScrollPane1.setMinimumSize(miniSize);
	        jSplitPane_top_bottom.setTopComponent(jScrollPane1);
	        jSplitPane_top_bottom.setOneTouchExpandable(true);//设置 显示一键展开/收缩图标
	        jSplitPane_top_bottom.setDividerSize(dividerSize);//分隔栏宽度
	        jSplitPane_top_bottom.setDividerLocation(init_top_bottom_split_location);//分隔栏的初始位置

	        //设置选项卡标签位置
	        jTabbedPane1 = new JTabbedPane();
	        jTabbedPane1.setToolTipText("双击最大化显示");
	        jTabbedPane1.setTabPlacement(JTabbedPane.TOP); 
	        
	        //设置选项卡布局策略，当卡片一行显示不开时，则在右侧出现滚动箭头。（默认测试是：一行显示不开始，显示多行）
	        jTabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	        
	        //为选项卡面板添加鼠标双击事件，双击则最大化显示，再次双击则还原。
	        jTabbedPane1.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount()==2){	
						
						//记录当前位置
						int cur_left_right_split_location = jSplitPane_left_right.getDividerLocation();
						int cur_top_bottom_split_location = jSplitPane_top_bottom.getDividerLocation();
						
						//如果当前不是最大化状态（有一个不等于0），则最大化显示。
						if(cur_left_right_split_location !=0 || cur_top_bottom_split_location != 0){					
							jSplitPane_left_right.setDividerLocation(0);
							jSplitPane_top_bottom.setDividerLocation(0);
						}else{
							jSplitPane_left_right.setDividerLocation(pre_left_right_split_location);
							jSplitPane_top_bottom.setDividerLocation(pre_top_bottom_split_location);
						}
						pre_left_right_split_location = cur_left_right_split_location;
						pre_top_bottom_split_location = cur_top_bottom_split_location;
					}
				}		
			});
	       
	        //选项卡面板，放于分隔面板的下方区域
	        jTabbedPane1.setMinimumSize(miniSize);
	        jSplitPane_top_bottom.setBottomComponent(jTabbedPane1);
	        jTabbedPane1.addChangeListener(new ChangeListener(){
				public void stateChanged(ChangeEvent e) {
					FilterDialog.refreshJlist();
				}	
	        });
	        /*
	         * 知识点：
	         * new JScrollPane(jTable1) 等价于
	         * JScrollPane  jScrollPane = new JScrollPane();
	         * jScrollPane.setViewportView(jTable1);
	         */
	 //设置jtextarea、lineNumber字体
	 Font font = (Font)ConfigUtil.getConfInfo().get(Const.SQL_FONT);
	 jTextArea1.setFont(font);
	 lineNumber.setFont(font);
	 
	 //设置左右分隔
	 jSplitPane_left_right.setOrientation(JSplitPane.HORIZONTAL_SPLIT);//设置分隔方式：左右分隔
	 jSplitPane_left_right.setBorder(null);
	 jSplitPane_left_right.setContinuousLayout(true);//打开连续布局属性，打开后不会再有虚拟分割线出现，但是由于不断重绘界面组件，可能会影响性能。
	 jSplitPane_left_right.setOneTouchExpandable(true);//设置 显示一键展开/收缩图标
	 jSplitPane_left_right.setDividerSize(dividerSize);//分隔栏宽度
	 
	 jSplitPane_top_bottom.setMinimumSize(miniSize); //如果不设置最小size为0，会造成jSplitPane_left_right向右拖到一定大小后就不能拖动了。
     jSplitPane_left_right.setRightComponent(jSplitPane_top_bottom);
     
     //初始化Jtable右键菜单--复制到剪贴板
     jtable_right_copy.setIcon(ImageIcons.copy_gif);
     jtable_right_copy.addActionListener(new ActionListener(){
 		public void actionPerformed(ActionEvent arg0) {
 			
 			//将	当前单元格值，放入剪贴板
 			Transferable contents = new StringSelection(currentSelectedCellValue+"");
 			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
 			clip.setContents(contents, null);
 		}   	
     });
     //初始化Jtable右键菜单--换行显示
     linewrapshow.setIcon(ImageIcons.detail_gif);
     linewrapshow.addActionListener(new ActionListener(){
 		public void actionPerformed(ActionEvent arg0) {
 			MyNotePad td = new MyNotePad(getInstance(),  "换行显示", currentSelectedCellValue+"",null);
 			td.setVisible(true);
 		}   	
     });
     jPopupMenu_table.add(jtable_right_copy);
     jPopupMenu_table.addSeparator();
     jPopupMenu_table.add(linewrapshow);

   //点击右下角【生成插入SQL】按钮
     jLabel_sql.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
        	 generateInsertSQL();
         }
         public void mouseEntered(MouseEvent e){//鼠标进入
             setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
          public  void mouseExited(MouseEvent e){//鼠标移出
             setCursor(Cursor.getDefaultCursor());
           }
     });
   //点击右下角【生成UPDATE SQL】按钮
     jLabel_sql_gen_update.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
        	 generateUpdateSQL();
         }
         public void mouseEntered(MouseEvent e){//鼠标进入
             setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
          public  void mouseExited(MouseEvent e){//鼠标移出
             setCursor(Cursor.getDefaultCursor());
           }
     });
     //点击右下角刷新按钮
     jLabel_refresh.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
        	 refresh();
         }
         public void mouseEntered(MouseEvent e){//鼠标进入
             setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
          public  void mouseExited(MouseEvent e){//鼠标移出
             setCursor(Cursor.getDefaultCursor());
           }
     });
     //点击删除按钮
     jLabel_delete.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
        	if(!DBUtil.isSupportedDBType()){
        		JOptionPane.showMessageDialog(MainFrame.getInstance(), "暂不支持当前数据库类型！"+DBUtil.getDBProductInfo().getProductName());
				return;
			}
        	String tableName = getCurrentTableName();
            if(tableName == null){
            	JOptionPane.showMessageDialog(MainFrame.getInstance(), "[删除]操作只在select * 单表查询时有效！");
            	return;
            }
        	 //list中的行号，起始值是0
        	 List<Integer> list = getSelectedRowNumberList();
             if(list == null || list.size()==0){
            	 JOptionPane.showMessageDialog(MainFrame.getInstance(), "请选择要删除的行！");
            	 return;
             }
             //将list中的行号，分别作加一处理，形成list2
             List<Integer> list2 = new ArrayList<Integer>();
             for(int i = 0;i<list.size();i++){
            	 list2.add(list.get(i)+1);
             }
             int opt = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "确定要删除选中的行"+list2+"？","确认对话框", JOptionPane.YES_NO_OPTION);		
	    	 if(opt == JOptionPane.YES_OPTION){
	
	    		 //查询表的主键
	    		List<String> primaryKeyList = DBUtil.getPrimaryKeyList(tableName, info.getUsername());
				if(primaryKeyList == null || primaryKeyList.size() < 1){
					JOptionPane.showMessageDialog(MainFrame.getInstance(),"表【"+tableName+"】不存在主键，无法执行删除操作！");
					return;
				}
				//查询表元数据信息
				ResultSetMetaData rm = DBUtil.getResultSetMetaData(tableName);
				if(rm == null){
					JOptionPane.showMessageDialog(MainFrame.getInstance(),"删除失败！查询表元数据信息出错！");
					return;
				}
				//delete from #tableName# where #pk1#=#value1# and...
				List<String> sqlList = new ArrayList<String>();
	    		String errorInfo = null;
	    		MyJTable jtableCurrent = getCurrentJtable();//当前Jtable
				for(int i = 0;i<list.size();i++){
					int row = list.get(i);
	    			StringBuilder deleteSql = new StringBuilder();
	    			deleteSql.append("delete from ").append(tableName).append(" where ");
	    			try {
						int columnCount = rm.getColumnCount();
						for(int c=1;c <= columnCount;c++){
							String columnName_temp = rm.getColumnName(c);
							if(primaryKeyList.contains(columnName_temp)){
								deleteSql.append(columnName_temp);
								deleteSql.append("=");
								int columnType = rm.getColumnType(c);
								if(DBUtil.isChar(columnType)){
									deleteSql.append("'");
									Object key = jtableCurrent.getValueAt(row, c);
									if(key != null){
										key = key.toString().replace("'", "''");
									}
									deleteSql.append(key);
									deleteSql.append("'");
								}else{
									deleteSql.append(jtableCurrent.getValueAt(row, c));
								}
								deleteSql.append(" and ");
							}
						}
						deleteSql.delete(deleteSql.length()-5, deleteSql.length()-1);
						
						//将所有deletesql放于一个集合中，最后一起放在事务中执行。
						sqlList.add(deleteSql.toString());
					}catch (Exception e) {
						errorInfo = e.getMessage();
					}
					if(errorInfo != null){
						JOptionPane.showMessageDialog(MainFrame.getInstance(),"删除失败！"+errorInfo);
						return;
					}
	            }
				if(sqlList.size() > 0){
					StringBuilder sql_show = new StringBuilder();
					for(int i = 0;i<sqlList.size();i++){
						sql_show.append(sqlList.get(i));
						sql_show.append("\n");
					}
					int isDelete = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "即将执行sql：\n" +sql_show+"确定执行？","确认对话框", JOptionPane.YES_NO_OPTION);				
			    	List<String> resultList = new ArrayList<String>();
					if(isDelete == JOptionPane.YES_OPTION){
						
						//执行sql前的事务处理：设置事务是否自动提交
			          	boolean isAutoCommit = isEnableAutoCommit();
			          	boolean isError = false;
			          	try {
			          		ConnUtil.getInstance().getConn().setAutoCommit(isAutoCommit);
			          	} catch (SQLException e) {
			          		if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
			          			log.error(null, e);
			          		}
			          		isError = true;
			          	}
			          	if(isError){
			          		String mess = null;
			          		if(isAutoCommit){
			          			mess = "设置事务自动提交失败，是否以事务非自动提交方式运行？";
			          		}else{
			          			mess = "设置事务非自动提交失败，是否以事务自动提交方式运行？";
			          		}
			          		int value = JOptionPane.showConfirmDialog(getInstance(), mess,"确认对话框", JOptionPane.YES_NO_OPTION);		
			          		if(value == JOptionPane.NO_OPTION){
			          			return;
			          		}
			          	}
						//执行sql
						//删除顺序从前往后时，前面删除成功1条，后面的行号会发生变化（自动减一）,进而导致后面记录删除时，会出现行号不对的情况，所以从后往前删。
			    		 for(int i = sqlList.size()-1 ;i >= 0 ;i--){
			    			 Map<String,String> returnMap = DBUtil.executeUpdate(sqlList.get(i));
			    			 if(returnMap.get("msg") != null){
			    				 resultList.add(sqlList.get(i)+"###"+returnMap.get("msg"));
			    			 }else{
			    				 resultList.add(sqlList.get(i)+"###执行成功！");
			    				 DefaultTableModel model = (DefaultTableModel)jtableCurrent.getModel();
			    				
			    				//行号list中的元素，与sqlList中元素是一一对应的。
			    				model.removeRow(list.get(i));
			    			 }
							}
			    		 StringBuilder result_show = new StringBuilder();
							for(int i = 0;i<resultList.size();i++){
								String arr[] = resultList.get(i).split("###");
								result_show.append(arr[0]);
								result_show.append(" ");
								result_show.append(arr[1]);
								result_show.append("\n");
							}
				         	//执行sql后的事务处理，如果是非自动提交方式，则回滚事务。
				      		if(!isAutoCommit){
				      			try {
				      				ConnUtil.getInstance().getConn().rollback();
				      			} catch (SQLException e) {
				      				JOptionPane.showMessageDialog(getInstance(), "事务非自动提交方式下，回滚事务失败！"+e.getMessage());
				      			}
				      		}
			    		 JOptionPane.showMessageDialog(MainFrame.getInstance(),"执行结束，"+
			    				 (isAutoCommit?"事务已提交！":"事务已回滚！")+"\n" +result_show);
			    	 }
				}
	    	 }
	    }
         public void mouseEntered(MouseEvent e){//鼠标进入
             setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
          public  void mouseExited(MouseEvent e){//鼠标移出
             setCursor(Cursor.getDefaultCursor());
           }
     });
     //点击右下角增加按钮（增加一行）
     jLabel_add.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            Vector<Object> vector = new Vector<Object>();
            MyJTable currentJtable = getCurrentJtable();
            if(currentJtable == null || getCurrentTableName() == null){
            	JOptionPane.showMessageDialog(MainFrame.getInstance(), "[增加]操作只在select * 单表查询时有效！");
            	return;
            }
            //在模型末尾增加一空行（如指定了排序规则，则按照排序规则的顺序进行添加）
            vector.add(new JCheckBox((currentJtable.getRowCount()+1)+""));
        	((DefaultTableModel)currentJtable.getModel()).addRow(vector);
        	
        	//自动滚动到最后一行
        	Rectangle rect = currentJtable.getCellRect(currentJtable.getRowCount()-1, 0, true);
        	currentJtable.scrollRectToVisible(rect);
         }
         public void mouseEntered(MouseEvent e){//鼠标进入
             setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
          public  void mouseExited(MouseEvent e){//鼠标移出
             setCursor(Cursor.getDefaultCursor());
           }
     });
     //点击右下角保存按钮（保存选中的行）
     jLabel_save.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
        	
        	 //查询所有选中的行-->组装成insert-->插入数据库-->记录结果（成功的行，失败的行）
        	 if(!DBUtil.isSupportedDBType()){
         		JOptionPane.showMessageDialog(MainFrame.getInstance(), "暂不支持当前数据库类型！"+DBUtil.getDBProductInfo().getProductName());
 				return;
 			}
        	 //当前表名称
        	 String tableName = getCurrentTableName();
             if(tableName == null){
             	JOptionPane.showMessageDialog(MainFrame.getInstance(), "[保存]操作只在select * 单表查询时有效！");
             	return;
             }
         	 //选中的行号列表，起始值是0
         	 List<Integer> rowNumberList = getSelectedRowNumberList();
              if(rowNumberList == null || rowNumberList.size()==0){
             	 JOptionPane.showMessageDialog(MainFrame.getInstance(), "请选择要保存的行！");
             	 return;
              }
              //查询表元数据信息
              ResultSetMetaData rm = DBUtil.getResultSetMetaData(tableName);
              if(rm == null){
				JOptionPane.showMessageDialog(MainFrame.getInstance(),"保存失败！查询表元数据信息出错！");
				return;
              }
              //当前Jtable
              MyJTable jtableCurrent = getCurrentJtable();
             
              //执行保存操作前，首先结束jtable的编辑状态，如不结束，正在编辑状态单元格的新值不生效。
              jtableCurrent.editingStopped(null);
              
              //用于存放生成的插入sql
              List<String> insertSqlList = new ArrayList<String>();
             
              //每一个选中行，组装成一个insert sql：insert into #tableName#(#column1#,#column1#...) values(#value1#,#value1#...);
              for(int row=0;row<rowNumberList.size();row++){
            	  int rowNumber = rowNumberList.get(row);
            	  StringBuilder valueSQL = new StringBuilder();
            	  StringBuilder insertSQL = new StringBuilder();
            	  insertSQL.append("insert into ").append(tableName);
            	  insertSQL.append("(");
            	  try {
						int columnCount = rm.getColumnCount();
						for(int c = 1;c <= columnCount; c++){
							
							//字段value值
							int columnType = rm.getColumnType(c);
							Object value = jtableCurrent.getValueAt(rowNumber, c);
							if(value == null || "(null)".equals(value)){
								continue;//为空的字段，跳过
							}
							if(DBUtil.isChar(columnType)){
								valueSQL.append("'");
								value = value.toString().replace("'", "''");
								valueSQL.append(value);
								valueSQL.append("'");
							}else{
								valueSQL.append(value);
							}
							valueSQL.append(",");

							//字段名称
							insertSQL.append(rm.getColumnName(c));
							insertSQL.append(",");
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
					if(insertSQL.toString().endsWith(",")){
						insertSQL.deleteCharAt(insertSQL.length()-1);
						valueSQL.deleteCharAt(valueSQL.length()-1);
					}
					insertSQL.append(") values(");
					insertSQL.append(valueSQL);
					insertSQL.append(")");
					insertSqlList.add(insertSQL.toString());
              }
              List<Integer> sucessLineNumberList = new ArrayList<Integer>();//记录执行成功的行
              List<Integer> failLineNumberList = new ArrayList<Integer>();//记录执行失败的行
              List<String> failErrorMsgList = new ArrayList<String>();//记录执行失败的错误信息
              
            //执行sql前的事务处理：设置事务是否自动提交
           	boolean isAutoCommit = isEnableAutoCommit();
           	boolean isError = false;
           	try {
           		ConnUtil.getInstance().getConn().setAutoCommit(isAutoCommit);
           	} catch (SQLException e) {
           		if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
           			log.error(null, e);
           		}
           		isError = true;
           	}
           	if(isError){
           		String mess = null;
           		if(isAutoCommit){
           			mess = "设置事务自动提交失败，是否以事务非自动提交方式运行？";
           		}else{
           			mess = "设置事务非自动提交失败，是否以事务自动提交方式运行？";
           		}
           		int value = JOptionPane.showConfirmDialog(getInstance(), mess,"确认对话框", JOptionPane.YES_NO_OPTION);		
           		if(value == JOptionPane.NO_OPTION){
           			return;
           		}
           	}
           	//执行sql
             for(int i=0;i<insertSqlList.size();i++){
	    			 Map<String,String> returnMap = DBUtil.executeUpdate(insertSqlList.get(i));
	    			 int rowNumber = rowNumberList.get(i)+1;//行号起始值是0，故此处做加1处理
	    			 if(returnMap.get("msg") != null){
	    				 failLineNumberList.add(rowNumber);
	    				 failErrorMsgList.add(returnMap.get("msg"));
	    			 }else{
	    				 sucessLineNumberList.add(rowNumber);
	    			 }
              }
          	//执行sql后的事务处理：如果是非自动提交方式，则回滚事务。
      		if(!isAutoCommit){
      			try {
      				ConnUtil.getInstance().getConn().rollback();
      			} catch (SQLException e) {
      				JOptionPane.showMessageDialog(getInstance(), "事务非自动提交方式下，回滚事务失败！"+e.getMessage());
      			}
      		}
              if(failLineNumberList.size() == 0){
            	  JOptionPane.showMessageDialog(MainFrame.getInstance(), "保存成功！"+
            			  (isAutoCommit?"事务已提交！":"事务已回滚！")+"成功插入"+sucessLineNumberList.size()+"条记录！");
              }else if(sucessLineNumberList.size()==0){
            	  StringBuilder show_msg = new StringBuilder();
            	  for(int r = 0;r<failLineNumberList.size();r++){
            		  show_msg.append("\n");
            		  show_msg.append(failLineNumberList.get(r));
            		  show_msg.append("行：");
            		  show_msg.append(failErrorMsgList.get(r));
            	  }
            	  JOptionPane.showMessageDialog(MainFrame.getInstance(), "保存失败！"+
            			  (isAutoCommit?"事务已提交！":"事务已回滚！")+show_msg);
              }else{
            	  StringBuilder show_msg = new StringBuilder();
            	  for(int r = 0;r<failLineNumberList.size();r++){
            		  show_msg.append("\n");
            		  show_msg.append(failLineNumberList.get(r));
            		  show_msg.append("行：");
            		  show_msg.append(failErrorMsgList.get(r));
            	  }
            	  JOptionPane.showMessageDialog(MainFrame.getInstance(), "操作完成！"+
            			  (isAutoCommit?"事务已提交！":"事务已回滚！")+"\n保存成功的行号："
            			  +sucessLineNumberList+"\n保存失败的行号："+failLineNumberList+show_msg);
              }
         }
         public void mouseEntered(MouseEvent e){//鼠标进入
             setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
          public  void mouseExited(MouseEvent e){//鼠标移除
             setCursor(Cursor.getDefaultCursor());
           }
     });
     //弹出查询对话框
     jLabel_filter.addMouseListener(new MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
        	 FilterDialog.getInstance(getInstance(), false).setVisible(true);
         }
         public void mouseEntered(MouseEvent e){//鼠标进入
           setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
          }
        public  void mouseExited(MouseEvent e){//鼠标移除
           setCursor(Cursor.getDefaultCursor());
         }
     });
     //将查询结果导出至excel
     jLabel_excel.addMouseListener(new MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
        	 export2excel();
         }
         public void mouseEntered(MouseEvent e){//鼠标进入
             setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
          public  void mouseExited(MouseEvent e){//鼠标移除
             setCursor(Cursor.getDefaultCursor());
           }
     });
     //将查询结果导出至html
     jLabel_html.addMouseListener(new MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
        	 export2Html();
         }
         public void mouseEntered(MouseEvent e){//鼠标进入
             setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
          public  void mouseExited(MouseEvent e){//鼠标移除
             setCursor(Cursor.getDefaultCursor());
           }
     });
   
     //初始化进度条的最大值，即JVM最大可使用的内存。 
    jProgressBar_jvm.setStringPainted(true);
    jProgressBar_jvm.setForeground(ColorUtil.LU_DOU_SHA);
    MemoryUsage mu = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    int max =(int)( mu.getMax()/(1024*1024));
	jProgressBar_jvm.setMaximum(max);
	jProgressBar_jvm.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {//点击则执行垃圾回收
			int userd1 = (int)ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed()/(1024*1024);
			System.gc();
			int userd2 = (int)ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed()/(1024*1024);
			jLabel_time.setText("<html><font color=blue>执行gc，成功回收内存 <b>"+(userd1-userd2)+"</b> M</font></html>");				
		}
		public void mouseEntered(MouseEvent e) {// 鼠标进入
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		public void mouseExited(MouseEvent e) {// 鼠标移除
			setCursor(Cursor.getDefaultCursor());
		}
	});
	
	
		// 将按钮放置工具栏
		jToolBar1.add(jButton_do);// 执行
		jToolBar1.add(jButton_connect);// 切换连接
		jToolBar1.add(jButton_saveAs); // 保存
		jToolBar1.add(jButton_clear);//清空
		jToolBar1.add(jButton_zhushi);//注释
		jToolBar1.add(jButton_find);//查找替换
		jToolBar1.add(jButton_gotoline);//定位行
		jToolBar1.add(jButton_undo);//撤销
		jToolBar1.add(jButton_redo);//恢复
		jToolBar1.add(jButton_moveup);//上移
		jToolBar1.add(jButton_movedown);//下移
		jToolBar1.add(jButton_tips);//智能提示
		jToolBar1.add(jButton_autoCommit);//自动提交事务
        
		// 批量设置工具栏上的按钮
		int count = jToolBar1.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component c = jToolBar1.getComponentAtIndex(i);
			if (c instanceof JButton) {

				JButton button = (JButton) c;

				// 设置边框不显示
				button.setBorderPainted(false);

				// 设置不获得焦点
				button.setFocusable(false);

				// 设置文字相对于图标的位置（上方图标、下方文字）
				button.setVerticalTextPosition(SwingConstants.BOTTOM);
				button.setHorizontalTextPosition(SwingConstants.CENTER);
			}
		}
		
		//底部 左对齐 流布局
		JPanel bottom_leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT ));
		bottom_leftPanel.add(jLabel_time);
		
		//底部 右对齐 流布局
		JPanel bottom_rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT ));
		bottom_rightPanel.add(jLabel_refresh);
		bottom_rightPanel.add(jLabel_add);
		bottom_rightPanel.add(jLabel_delete);
		bottom_rightPanel.add(jLabel_save);
		bottom_rightPanel.add(jLabel_sql);
		bottom_rightPanel.add(jLabel_sql_gen_update);
		bottom_rightPanel.add(jLabel_filter);
		bottom_rightPanel.add(jLabel_html);
		bottom_rightPanel.add(jLabel_excel);
		bottom_rightPanel.add(jProgressBar_jvm);
		
		//bottomPanel指定东西南北布局，在西部区域加“左对齐布局panel”，右部区域加”右对齐布局panel“
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(bottom_leftPanel,BorderLayout.WEST);
		bottomPanel.add(bottom_rightPanel,BorderLayout.EAST);
		
		//frame的ContentPane默认布局是BorderLayout，即东西南北布局
		//北部区域  添加 工具栏jToolBar1
		getContentPane().add(jToolBar1, BorderLayout.NORTH);
		//中部区域 添加左右分隔的分割面板
		getContentPane().add(jSplitPane_left_right, BorderLayout.CENTER);
		//底部区域 添加BorderLayout布局的面板 bottomPanel
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }
	
    /**
     * 菜单--导入excel
     * 导入操作不受手动设置的事务是否自动提交的影响。
     */
    private void importExcel() {

    	int value = JOptionPane.showConfirmDialog(this, 
    			"导入Excel操作不受手动设置的【事务是否自动提交】的影响，导入成功则提交，导入失败则回滚！是否继续？","确认对话框", 
    			JOptionPane.YES_NO_OPTION);		
		if(value == JOptionPane.NO_OPTION){
			return;
		}
		// 弹出选择文件对话框
		FileDialog openDialog = new FileDialog(this, "请选择要导入的Excel文件",FileDialog.LOAD);
		openDialog.setVisible(true);	
		if (openDialog.getDirectory() != null) {// 成功点击了确定按钮		
			String path = openDialog.getDirectory() + openDialog.getFile();
			if (!path.toLowerCase().endsWith(".xls")) {
				JOptionPane.showMessageDialog(this, "您选择的不是有效Excel文件，无法执行导入操作！");
				return;
			}
			try {
				ConnUtil.getInstance().getConn().setAutoCommit(false);//设置事务非自动提交
				File xlsfile = new File(path);
				IDataSet dataSet = new XlsDataSet(xlsfile);
				DatabaseOperation.INSERT.execute(getDBUnitConnection(), dataSet);
				ConnUtil.getInstance().getConn().commit();//如果导入操作未出错，则提交事务
				JOptionPane.showMessageDialog(this,"导入成功！");
			} catch (Exception e) {
				try {
					ConnUtil.getInstance().getConn().rollback();//出错后，事务回滚。
					JOptionPane.showMessageDialog(this, "出错了，数据已回滚！"+e.getMessage());
				} catch (SQLException e1) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e1);
					}
					JOptionPane.showMessageDialog(this, "导入xml出错，并且数据回滚失败！"+e1.getMessage());
				}
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
			}finally{
				try {
					ConnUtil.getInstance().getConn().setAutoCommit(true);
				} catch (SQLException e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
					JOptionPane.showMessageDialog(this,e.getMessage());
				}
			}  
		}	
    }
    /**
     * 导入CSV
     */
    private void importCSV() {

    	int value = JOptionPane.showConfirmDialog(this,"导入CSV操作不受手动设置的【事务是否自动提交】的影响，导入成功则提交，导入失败则回滚！是否继续？","确认对话框",JOptionPane.YES_NO_OPTION);		
		if(value == JOptionPane.NO_OPTION){
			return;
		}
		// 弹出选择文件对话框
		FileDialog openDialog = new FileDialog(this, "请选择要导入的CSV文件",FileDialog.LOAD);
		openDialog.setVisible(true);	
		if (openDialog.getDirectory() != null) {// 成功点击了确定按钮		
			final String path = openDialog.getDirectory() + openDialog.getFile();
			if (!path.toLowerCase().endsWith(".csv")) {
				JOptionPane.showMessageDialog(this, "您选择的不是有效csv文件，无法执行导入操作！");
				return;
			}	
			
			//提示
			jButton_do.setEnabled(false);
			jLabel_time.setText("<html><font color=blue>正在导入，请稍后...</font></html>");
			jLabel_time.setIcon(ImageIcons.wait_gif32);
			
			//新启线程，执行导入sql
			Thread thread = new Thread(){
				@Override
				public void run() {
					final long beginTime = System.currentTimeMillis();//计时开始
					Statement stmt = null;
					final List<SqlResult> resultList = new ArrayList<SqlResult>();
					try {
						ConnUtil.getInstance().getConn().setAutoCommit(false);//设置不自动提交事务，以便出错时事务回滚。
						int oks = 0;
						int fails = 0;
						
						stmt = ConnUtil.getInstance().getConn().createStatement();
						List<String>  sqls = CSVUtil.csv2sql(new File(path));
						for(String sql : sqls){
							SqlResult temp = new SqlResult();
							temp.setSql(sql);
							try {
								int lines = stmt.executeUpdate(sql);
								temp.setSuccess(true);
								temp.setResult("成功！影响记录行数："+lines);
								oks++;
							} catch (SQLException e) {
								temp.setSuccess(false);
								temp.setResult("出错了！"+e.getMessage());
								if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
									log.error(sql, e);
								}
								fails++;
							}
							resultList.add(temp);
						}
						//导入sql完成，执行界面渲染
						final int fails_final = fails;
						final int oks_final = oks;
						try {
							SwingUtilities.invokeAndWait(new Runnable(){
								public void run() {
									long endTime = System.currentTimeMillis();//计时结束
									if (resultList.size() == 0) {
										JOptionPane.showMessageDialog(getInstance(), "当前csv文件为空！");
									}else{	
										String mess = null;
										if(fails_final > 0){
											//如果存在执行失败的sql，则回滚数据。
											try {
												ConnUtil.getInstance().getConn().rollback();
												mess = "<html><body><font color=blue>出错了，事务<b>已回滚</b>，耗时 <b>";
											} catch (SQLException e) {
												if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
													log.error(null, e);
												}
												mess = "<html><body><font color=blue>出错了，事务<b>回滚失败</b>，耗时 <b>";
											}
										}else{
											//如果全部执行成功，则提交事务。
											try {
												ConnUtil.getInstance().getConn().commit();
												mess = "<html><body><font color=blue>导入成功，事务<b>已提交</b>，耗时 <b>";
											} catch (SQLException e) {
												if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
													log.error(null, e);
												}
												mess = "<html><body><font color=blue>导入成功，事务<b>提交失败</b>，耗时 <b>";
											}
										}
										
										//清除缓存
										clear(true);
										
										//统计耗时信息
										jLabel_time.setText(mess+(((Long)(endTime-beginTime)).floatValue())/1000+"</b> 秒。</font></body></html>");		   
										
										//在下方列表展示执行结果：
										Object[] titles = {null,"是否成功","结果信息","SQL"};// 列数
										Object[][] dataset = new Object[resultList.size()][titles.length];//二维数组  [行数，列数]
										for(int i=0;i<resultList.size();i++){
											SqlResult rs = resultList.get(i);
											dataset[i][0] = new JCheckBox((i+1)+"");//第一列是选择列
											dataset[i][1] = rs.isSuccess()?"是":"否";
											dataset[i][2] = rs.getResult();
											dataset[i][3] = rs.getSql();
										}
										MyTableModel myTableModel = new MyTableModel(dataset,titles);//根据默认表格数据模型创建Jtable
										MyJTable jtable = new MyJTable(myTableModel);
										setJtable(jtable,"import_sql");
								        String title = null;
								        if(fails_final > 0){
								        	title = "导入 CSV 文件出错（成功数:"+oks_final+",失败数:"+fails_final+"），数据已回滚，详细信息如下：";
										}else{
											title = "导入 CSV 文件成功（成功数:"+oks_final+",失败数:"+fails_final+"），数据已提交，详细信息如下：";
										}
								        show_componts.put(title, jtable);
								        jTabbedPane1.insertTab(title, ImageIcons.table_gif, new JScrollPane(jtable), null, 0);
									}
								}
							});
						} catch (Exception e) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
								log.error(null, e);
							}
						}
					} catch (SQLException e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e);
						}
						JOptionPane.showMessageDialog(getInstance(), "数据库连接获取Statement出错！"+e.getMessage());
					}finally{
						SwingUtilities.invokeLater(new Runnable(){
							public void run() {
								jButton_do.setEnabled(true);
								jLabel_time.setText(null);
								jLabel_time.setIcon(null);
							};
						});
						try {
							//导入完成后，无论成功失败，都将事务设置为自动提交方式。
							ConnUtil.getInstance().getConn().setAutoCommit(true);
						} catch (SQLException e) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
								log.error(null, e);
							}
							JOptionPane.showMessageDialog(getInstance(), "重置自动提交出错！"+e.getMessage());
						}
						if(stmt!=null){
							try {
								stmt.close();
							} catch (SQLException e) {
								if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
									log.error(null, e);
								}
								JOptionPane.showMessageDialog(getInstance(), "关闭连接句柄出错！"+e.getMessage());
							}
						}
					}
				}
			};
			thread.start();
		}	
    }
    /*
     * 菜单--导入xml
     */
    private void importXml() {

    	int value = JOptionPane.showConfirmDialog(this, 
    			"导入XML操作不受手动设置的【事务是否自动提交】的影响，导入成功则提交，导入失败则回滚！是否继续？","确认对话框", 
    			JOptionPane.YES_NO_OPTION);		
		if(value == JOptionPane.NO_OPTION){
			return;
		}
		// 弹出选择文件对话框
		FileDialog openDialog = new FileDialog(this, "请选择要导入的xml文件",FileDialog.LOAD);
		openDialog.setVisible(true);	
		if (openDialog.getDirectory() != null) {// 成功点击了确定按钮		
			String path = openDialog.getDirectory() + openDialog.getFile();
			if (!path.toLowerCase().endsWith(".xml")) {
				JOptionPane.showMessageDialog(this, "您选择的不是有效Xml文件，无法执行导入操作！");
				return;
			}
			try {
				ConnUtil.getInstance().getConn().setAutoCommit(false);//设置事务非自动提交
				FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
				IDataSet dataSet = flatXmlDataSetBuilder.build(new FileInputStream(path));
				DatabaseOperation.INSERT.execute(getDBUnitConnection(), dataSet);
				ConnUtil.getInstance().getConn().commit();//导入成功，则提交事务
				JOptionPane.showMessageDialog(this,"导入成功！");
			} catch (Exception e) {
				try {
					ConnUtil.getInstance().getConn().rollback();//出错后，事务回滚
					JOptionPane.showMessageDialog(this, "出错了，数据已回滚！"+e.getMessage());
				} catch (SQLException e1) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e1);
					}
					JOptionPane.showMessageDialog(this, "导入xml出错，并且数据回滚失败！"+e1.getMessage());
				}
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
			}finally{
				try {//导入完成后，无论成功失败，都将事务设置为自动提交方式
					ConnUtil.getInstance().getConn().setAutoCommit(true);
				} catch (SQLException e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
					JOptionPane.showMessageDialog(this,e.getMessage());
				}
			} 
		}	
    }
    /*
     * 菜单--导入sql文件
     */
    private void importSql() {

    	int value = JOptionPane.showConfirmDialog(this,"导入SQL操作不受手动设置的【事务是否自动提交】的影响，导入成功则提交，导入失败则回滚！是否继续？","确认对话框",JOptionPane.YES_NO_OPTION);		
		if(value == JOptionPane.NO_OPTION){
			return;
		}
		// 弹出选择文件对话框
		FileDialog openDialog = new FileDialog(this, "请选择要导入的SQL文件",FileDialog.LOAD);
		openDialog.setVisible(true);	
		if (openDialog.getDirectory() != null) {// 成功点击了确定按钮		
			final String path = openDialog.getDirectory() + openDialog.getFile();
			if (!path.toLowerCase().endsWith(".sql")) {
				JOptionPane.showMessageDialog(this, "您选择的不是有效SQL文件，无法执行导入操作！");
				return;
			}	
			
			//提示
			jButton_do.setEnabled(false);
			jLabel_time.setText("<html><font color=blue>正在导入，请稍后...</font></html>");
			jLabel_time.setIcon(ImageIcons.wait_gif32);
			
			//新启线程，执行导入sql
			Thread thread = new Thread(){
				@Override
				public void run() {
					final long beginTime = System.currentTimeMillis();//计时开始
					BufferedReader in = null;
					Statement stmt = null;
					StringBuilder sqls = new StringBuilder();
					final List<SqlResult> resultList = new ArrayList<SqlResult>();
					try {
						ConnUtil.getInstance().getConn().setAutoCommit(false);//设置不自动提交事务，以便出错时事务回滚。
						int oks = 0;
						int fails = 0;
			    		String code = EncodingDetect.getJavaEncode(path);
			    		InputStreamReader brs = null;
			    		FileInputStream fr = new FileInputStream(path);
			    		if(code == null || "".equals(code)){
			    			brs = new InputStreamReader(fr);
			    		}else{
			    			brs = new InputStreamReader(fr,EncodingDetect.getJavaEncode(path));
			    		}
						in = new BufferedReader(brs);
						
						stmt = ConnUtil.getInstance().getConn().createStatement();
						String readline;
						while ((readline = in.readLine()) != null) {
							if (readline.startsWith("--")) {//双中划线开头的行视为注释，跳过。
								continue;
							}
							sqls.append(" " + readline);
							if (readline.endsWith(";")) {
								sqls.deleteCharAt(sqls.length()-1);//删除末尾的分号
								SqlResult temp = new SqlResult();
								temp.setSql(sqls.toString().trim());
								try {
									int lines = stmt.executeUpdate(sqls.toString().trim());
									temp.setSuccess(true);
									temp.setResult("成功！影响记录行数："+lines);
									oks++;
								} catch (SQLException e) {
									temp.setSuccess(false);
									temp.setResult("出错了！"+e.getMessage());
									if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
										log.error(sqls, e);
									}
									fails++;
								}
								resultList.add(temp);
								sqls = new StringBuilder();
							}
						}
						//导入sql完成，执行界面渲染
						final int fails_final = fails;
						final int oks_final = oks;
						try {
							SwingUtilities.invokeAndWait(new Runnable(){
								public void run() {
									long endTime = System.currentTimeMillis();//计时结束
									if (resultList.size() == 0) {
										JOptionPane.showMessageDialog(getInstance(), "当前sql文件为空！");
									}else{	
										String mess = null;
										if(fails_final > 0){
											//如果存在执行失败的sql，则回滚数据。
											try {
												ConnUtil.getInstance().getConn().rollback();
												mess = "<html><body><font color=blue>出错了，事务<b>已回滚</b>，耗时 <b>";
											} catch (SQLException e) {
												if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
													log.error(null, e);
												}
												mess = "<html><body><font color=blue>出错了，事务<b>回滚失败</b>，耗时 <b>";
											}
										}else{
											//如果全部执行成功，则提交事务。
											try {
												ConnUtil.getInstance().getConn().commit();
												mess = "<html><body><font color=blue>导入成功，事务<b>已提交</b>，耗时 <b>";
											} catch (SQLException e) {
												if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
													log.error(null, e);
												}
												mess = "<html><body><font color=blue>导入成功，事务<b>提交失败</b>，耗时 <b>";
											}
										}
										
										//清除缓存
										clear(true);
										
										//统计耗时信息
										jLabel_time.setText(mess+(((Long)(endTime-beginTime)).floatValue())/1000+"</b> 秒。</font></body></html>");		   
										
										//在下方列表展示执行结果：
										Object[] titles = {null,"是否成功","结果信息","SQL"};// 列数
										Object[][] dataset = new Object[resultList.size()][titles.length];//二维数组  [行数，列数]
										for(int i=0;i<resultList.size();i++){
											SqlResult rs = resultList.get(i);
											dataset[i][0] = new JCheckBox((i+1)+"");//第一列是选择列
											dataset[i][1] = rs.isSuccess()?"是":"否";
											dataset[i][2] = rs.getResult();
											dataset[i][3] = rs.getSql();
										}
										MyTableModel myTableModel = new MyTableModel(dataset,titles);//根据默认表格数据模型创建Jtable
										MyJTable jtable = new MyJTable(myTableModel);
										setJtable(jtable,"import_sql");
								        String title = null;
								        if(fails_final > 0){
								        	title = "导入 SQL 文件出错（成功数:"+oks_final+",失败数:"+fails_final+"），数据已回滚，详细信息如下：";
										}else{
											title = "导入 SQL 文件成功（成功数:"+oks_final+",失败数:"+fails_final+"），数据已提交，详细信息如下：";
										}
								        show_componts.put(title, jtable);
								        jTabbedPane1.insertTab(title, ImageIcons.table_gif, new JScrollPane(jtable), null, 0);
									}
								}
							});
						} catch (Exception e) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
								log.error(null, e);
							}
						}
					} catch (IOException e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e);
						}
						JOptionPane.showMessageDialog(getInstance(), "读取文件 "+path+" 失败！");
					} catch (SQLException e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e);
						}
						JOptionPane.showMessageDialog(getInstance(), "数据库连接获取Statement出错！"+e.getMessage());
					}finally{
						SwingUtilities.invokeLater(new Runnable(){
							public void run() {
								jButton_do.setEnabled(true);
								jLabel_time.setText(null);
								jLabel_time.setIcon(null);
							};
						});
						try {
							//导入完成后，无论成功失败，都将事务设置为自动提交方式。
							ConnUtil.getInstance().getConn().setAutoCommit(true);
						} catch (SQLException e) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
								log.error(null, e);
							}
							JOptionPane.showMessageDialog(getInstance(), "重置自动提交出错！"+e.getMessage());
						}
						try {
							in.close();
						} catch (IOException e) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
								log.error(null, e);
							}
							JOptionPane.showMessageDialog(getInstance(), "关闭文件流出错！"+e.getMessage());
						}	
						if(stmt!=null){
							try {
								stmt.close();
							} catch (SQLException e) {
								if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
									log.error(null, e);
								}
								JOptionPane.showMessageDialog(getInstance(), "关闭连接句柄出错！"+e.getMessage());
							}
						}
					}
				}
			};
			thread.start();
		}	
    }
    /*
     * 菜单：联系
     */
    private void connectmeActionPerformed(java.awt.event.ActionEvent evt) {
    	ConnectDialog.getInstance(getInstance(), true).setVisible(true);
    }
    /*
     * 清空编辑器
     */
    private void jButton_clearActionPerformed(java.awt.event.ActionEvent evt) {
 	
    	//清空缓存
    	clear(true);
    	//清空编辑器中的数据
    	jTextArea1.setText("");
    	//jTextArea1.setFocusable(true);//不推荐
    	//jTextArea1.grabFocus();//不推荐
    	jTextArea1.requestFocusInWindow();//执行清空操作后，让光标焦点定位到Jtextarea，推荐使用方式
    }
    /*
     * 切换连接
     */
    private void changeConn() {
    	
    	//保存编辑区的sql文件
    	saveSql();
    	
    	//关闭现有连接，dbunit连接会一同被关闭，但是dbunit连接引用指向不是null。
		ConnUtil.getInstance().closeConnection();
		
		//将dbunit连接引用指向null
		dbUnitconnection = null;
		
		//设置本窗体不可见，并释放相关资源
    	getInstance().setVisible(false);
    	
    	//设置导出窗口不可见
    	if(ExportFrame.hasInstance()){
    		ExportFrame.getInstance(null).dispose();
    	}
    	
    	//移除托盘图标
    	tray.remove(trayIcon);
    	
    	//设置SetValue窗体可见
		SetValue.getInstance().setVisible(true);
    	}
    /*
     * 保存（另存为）命令编辑区的sql脚本
     */
    private void saveAsSqlFile() {

    	FileDialog saveDialog = new FileDialog(this, "保存SQL脚本",FileDialog.SAVE);
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));//如果不指定时区，在有些机器上会出现时间误差。  
		String currentDateTime = sdf.format(new Date());
		
		saveDialog.setFile(currentDateTime+".sql");
		saveDialog.setVisible(true);
		if (saveDialog.getDirectory() == null) {// 点击了取消按钮
			return;
		} else {
			String path = saveDialog.getDirectory() + saveDialog.getFile();
			String saveStr = jTextArea1.getText();
			FileWriter fw = null;
			try {
				if (!path.toLowerCase().endsWith(".sql")) {
					path = path + ".sql";
				}
				fw = new FileWriter(path);
				fw.write(saveStr);
				fw.flush();
			} catch (Exception e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
				JOptionPane.showMessageDialog(this, " 出错:  " + e.getMessage());
			} finally {
				try {
					fw.close();
				} catch (IOException e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
					JOptionPane.showMessageDialog(this, " 出错:  "+ e.getMessage());
				}
			}
			JOptionPane.showMessageDialog(this, " 导出SQL脚本成功! "+path);
		}
    }
    /*
     * 在帮助框中，鼠标双击、回车键、则执行回填数据操作。
     */
    private void huiTianData(){
    	
    	Object selectValue = jwindow_help.jList1.getSelectedValue();
    	if(selectValue != null){
    		String arr[] = ((Object[])selectValue)[1].toString().split(" ");
    		String lastValue = arr[arr.length-1];
    		int cur = jTextArea1.getCaretPosition();
    		
    		//表、视图、关键字 解析并回填
    		if(lastValue.indexOf("[表]")!=-1 || lastValue.indexOf("[视图]")!=-1 || lastValue.indexOf("[关键字]")!=-1){
    			String returnValue = ((Object[])selectValue)[1].toString().split(" \\[")[0];
				jTextArea1.replaceRange(returnValue, cur-currentPre.length(), cur);
				
			//字段 解析并回填
    		}else{
    			if(isOnlyFieldTips){
    				jTextArea1.insert(arr[0], cur);
    			}else{		
    				jTextArea1.replaceRange(arr[0], cur-currentPre.length(), cur);
    			}
    		}
		}
		jwindow_help.setVisible(false);//隐藏帮助框
    }
    /*
     * JtextArea键盘事件
     */
    private void tips(java.awt.event.KeyEvent evt) {
    	if(!DBUtil.isSupportedDBType()){
			return;
		}  
    	String dbType = DBUtil.getDBProductInfo().getProductName();
    	if(jwindow_help == null){
    		jwindow_help = new JWindowTips(getInstance());
    		jwindow_help.setLocationRelativeTo(getInstance());
    		
    		 //为智能提示框添加事件监听，当用户继续输入时，则将提示框隐藏;当用户回车时，将选择项填入sql编辑器中。
    		jwindow_help.jList1.addKeyListener(new KeyAdapter(){
    			
    			int windowKeyCode = 0;	
				public void keyReleased(KeyEvent e) {
					
					//回车，则将当前选择项填充至JtextArea的光标处。
					if(windowKeyCode==KeyEvent.VK_ENTER){
						huiTianData();
					}
				}   
				//KeyTyped 获取不到keycode(提示框上的回车键、上下方向键，不必隐藏window)
    			public void keyTyped(KeyEvent e) {
    				if(windowKeyCode != KeyEvent.VK_ENTER 
    						&& windowKeyCode != KeyEvent.VK_DOWN 
    						&& windowKeyCode != KeyEvent.VK_BACK_SPACE //如果是退格键，则不执行插入操作
    						&& windowKeyCode != KeyEvent.VK_UP){	
    				
    					while(e.isControlDown()||e.isAltDown()){
    			    		return;
    			    	}    	
    					int cur = jTextArea1.getCaretPosition();
    					jTextArea1.insert(e.getKeyChar()+"", cur);
    					jwindow_help.setVisible(false);
    				}
    			}
    			//执行顺序：KeyPressed--KeyTyped--KeyReleased
    			public void keyPressed(KeyEvent e) {	
    				windowKeyCode = e.getKeyCode();
    				if(windowKeyCode == KeyEvent.VK_BACK_SPACE){//退格键				
    					int cur = jTextArea1.getCaretPosition();
    					jTextArea1.replaceRange("", cur-1, cur);
    					jwindow_help.setVisible(false);
    				}
    			}
    		});
    		//鼠标双击，也会将当前选项回填至sql编辑器中
    		jwindow_help.jList1.addMouseListener(new MouseAdapter(){
    			 public void mouseClicked(MouseEvent e) {
    				 if(e.getClickCount()==2){
    					 huiTianData();
    				 }
    			 }
    		});
    	}
    	//当敲击空格键 时，则进行字段提示
    	if(evt.getKeyCode() == KeyEvent.VK_SPACE){
    		isOnlyFieldTips = true;
        	int cur = jTextArea1.getCaretPosition();     
        	String headString = jTextArea1.getText().substring(0,cur); 
        	if(!headString.endsWith(" ")){
        		return;
        	}
        	while(evt.isControlDown()||evt.isAltDown()){
        		return;
        	}    	
        	String trimHead = headString.trim().toUpperCase();
        	if(!trimHead.endsWith(",") && !trimHead.endsWith("AND") && !trimHead.endsWith("SET") 
        			&& !trimHead.endsWith("WHERE") && !trimHead.endsWith("ORDER BY") && !trimHead.endsWith("GROUP BY")
        			&& !trimHead.endsWith("OR")){
        		return;
        	}
			int last_SELECT = trimHead.lastIndexOf("SELECT");
			int last_UPDATE = trimHead.lastIndexOf("UPDATE");
			int last_DELETE = trimHead.lastIndexOf("DELETE");
			int last_GENERATEINSERTSQL = trimHead.lastIndexOf("GENERATEINSERTSQL");
			int maxKey = MathUtil.max(last_SELECT,last_UPDATE,last_DELETE,last_GENERATEINSERTSQL);
			
			//UPDATE Person SET Address = 'Zhongshan 23', City = 'Nanjing' WHERE ID='' and NAME=''
			//delete from PORTAL_CATEGORY where ID='' and NAME=''
			//select * from PORTAL_CATEGORY as a where ID='' and NAME=''
			//generateInsertSql PUB_APPS where PUB_APPS.APP_CODE='-1';
			
			List<String> tabNameList = new ArrayList<String>();
			if(maxKey == last_UPDATE){
				int last_SET = trimHead.lastIndexOf("SET");
				if(last_SET > last_UPDATE){			
					String tname = trimHead.substring(last_UPDATE+"UPDATE".length(),last_SET);//解析 update 和 set 之间的表名 
					tabNameList.add(tname.replace("`", ""));//去除tableName的反引号（MySQL数据库存在此情况，如`iam_service`）
				}
			}else if(maxKey == last_GENERATEINSERTSQL){
				int last_WHERE = trimHead.lastIndexOf("WHERE");
				if(last_WHERE > last_GENERATEINSERTSQL){			
					String tname = trimHead.substring(last_GENERATEINSERTSQL+"GENERATEINSERTSQL".length(),last_WHERE);//解析 generateInsertSql 和 WHERE 之间的表名 
					tabNameList.add(tname.replace("`", ""));
				}
			}else if(maxKey == last_DELETE){
				int last_FROM = trimHead.lastIndexOf("FROM");
				int last_WHERE = trimHead.lastIndexOf("WHERE");
				if(last_WHERE > last_FROM){			
					String tname = trimHead.substring(last_FROM+"FROM".length(),last_WHERE);////解析 from 和 where 之间表名
					tabNameList.add(tname.replace("`", ""));
				}
			}else if(maxKey == last_SELECT){
				//SELECT Company, OrderNumber FROM Orders ORDER BY Company DESC, OrderNumber ASC
				//select * from PORTAL_CATEGORY order by ...
				//SELECT column_name, aggregate_function(column_name) FROM table_name WHERE column_name operator value GROUP BY column_name
				int last_FROM = trimHead.lastIndexOf("FROM");
				int last_WHERE = trimHead.lastIndexOf("WHERE");
				int last_Order_By = trimHead.lastIndexOf("ORDER BY");
				int last_Group_By = trimHead.lastIndexOf("GROUP BY");
				if(last_Order_By > last_FROM || last_WHERE > last_FROM||last_Group_By>last_FROM){
					String tnames = trimHead.substring(last_FROM+"FROM".length(),MathUtil.max(last_Order_By,last_WHERE,last_Group_By));////解析 from 和 where 之间表名
					String tnames_arr[] = tnames.split(",");
					for (int i = 0; i < tnames_arr.length; i++) {			
						tabNameList.add(tnames_arr[i].replace("`", ""));
					}
				}	
			}
			//提示信息格式：  organ_id varchar(20) [pub_organ]	
			if(tabNameList.size()>0){	
				//SQL Server 和 PG，通过调用接口来实现。
				if(dbType.equals("MICROSOFT SQL SERVER") || dbType.equals("POSTGRESQL")){
					List<FieldInfo> clos = DBUtil.getTableColums(tabNameList, "");
					if(clos.size()==0){
						jwindow_help.setVisible(false);
						return;//如果没有匹配的记录，则直接return。
					}
					DefaultListModel defaultListModel = (DefaultListModel)jwindow_help.jList1.getModel();
					defaultListModel.removeAllElements();
					for(FieldInfo col:clos){
						Object[] element = {ImageIcons.column_png,col.getFieldName()+" "+col.getFieldType()+"("+col.getFieldLength()+") ["+col.getTableName()+"]"};
						defaultListModel.addElement(element);
					}
					if(jwindow_help.jList1.getModel().getSize()>0){	
						try {
							Rectangle r1 = jTextArea1.modelToView(jTextArea1.getCaretPosition());
							Point jtextAreaPoint = jTextArea1.getLocationOnScreen();
							jwindow_help.setLocation(jtextAreaPoint.x+r1.x,jtextAreaPoint.y+r1.y+15);
							jwindow_help.setVisible(true);
							jwindow_help.jList1.setSelectedIndex(0);//设置默认选中第一条，方便用户操作
							} catch (BadLocationException e) {
								if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
									log.error(null, e);
								}
								JOptionPane.showMessageDialog(this, e.getMessage());				
							}		           
					}	
				}else if(dbType.contains("MYSQL")){
					String table_schema = null;
					try {
						table_schema = ConnUtil.getInstance().getConn().getCatalog();
					} catch (SQLException e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e);
						}
					}
					StringBuilder sql = new StringBuilder();
					sql.append("select TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,COLUMN_TYPE from information_schema.COLUMNS WHERE TABLE_NAME in(");
					for(int i=0;i<tabNameList.size();i++){
						sql.append("'");
						sql.append(tabNameList.get(i).trim().toUpperCase());
						sql.append("'");
						sql.append(",");
					}
					sql.deleteCharAt(sql.length()-1);
					sql.append(")");	
					if(table_schema != null){
						sql.append(" and TABLE_SCHEMA='"+table_schema+"'");
					}
					List<Map<String, Object>> list =(List<Map<String, Object>>)(DBUtil.executeQuery(sql.toString()).get("result"));
					if(list==null||list.size()==0){
						jwindow_help.setVisible(false);
						return;//如果没有匹配的记录，则直接return。
					}
					DefaultListModel defaultListModel = (DefaultListModel)jwindow_help.jList1.getModel();
					defaultListModel.removeAllElements();
					for (Map<String, Object> map : list) {
						Object[] element = {ImageIcons.column_png,
								map.get("COLUMN_NAME")+" "+map.get("COLUMN_TYPE")+" ["+map.get("TABLE_NAME")+"]"};
						defaultListModel.addElement(element);
					}
					if(jwindow_help.jList1.getModel().getSize()>0){	
						try {
							Rectangle r1 = jTextArea1.modelToView(jTextArea1.getCaretPosition());
							Point jtextAreaPoint = jTextArea1.getLocationOnScreen();
							jwindow_help.setLocation(jtextAreaPoint.x+r1.x,jtextAreaPoint.y+r1.y+15);
							jwindow_help.setVisible(true);
							jwindow_help.jList1.setSelectedIndex(0);//设置默认选中第一条，方便用户操作
							} catch (BadLocationException e) {
								if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
									log.error(null, e);
								}
								JOptionPane.showMessageDialog(this, e.getMessage());				
							}		           
					}
				}else{
					//---------------Oracle/DB2数据库，以查询字典表实现方式--------------
					StringBuilder sql = new StringBuilder();
					if(dbType.contains("ORACLE")){	
						sql.append("select COLUMN_NAME,DATA_TYPE,DATA_LENGTH,TABLE_NAME from USER_TAB_COLUMNS WHERE  TABLE_NAME in(");
						for(int i=0;i<tabNameList.size();i++){
							sql.append("'");
							sql.append(tabNameList.get(i).trim().toUpperCase());
							sql.append("'");
							sql.append(",");
						}
						sql.deleteCharAt(sql.length()-1);
						sql.append(")");			
					}else if(dbType.contains("DB2")){
						sql.append("select NAME AS COLUMN_NAME ,COLTYPE AS DATA_TYPE ,LENGTH AS DATA_LENGTH,TBNAME as TABLE_NAME from  SYSIBM.SYSCOLUMNS WHERE TBNAME in(");
						for(int i=0;i<tabNameList.size();i++){
							sql.append("'");
							sql.append(tabNameList.get(i).trim().toUpperCase());
							sql.append("'");
							sql.append(",");
						}
						sql.deleteCharAt(sql.length()-1);
						sql.append(")");
					}else{
						JOptionPane.showMessageDialog(this, "自动补全功能暂不支持："+dbType);
						return;
					}
					List<Map<String, Object>> list =(List<Map<String, Object>>)(DBUtil.executeQuery(sql.toString()).get("result"));
					if(list==null||list.size()==0){
						jwindow_help.setVisible(false);
						return;//如果没有匹配的记录，则直接return。
					}
					DefaultListModel defaultListModel = (DefaultListModel)jwindow_help.jList1.getModel();
					defaultListModel.removeAllElements();
					for (Map<String, Object> map : list) {
						Object[] element = {ImageIcons.column_png,
								map.get("COLUMN_NAME")+" "+map.get("DATA_TYPE")+"("+map.get("DATA_LENGTH")+") ["+map.get("TABLE_NAME")+"]"};
						defaultListModel.addElement(element);
					}
					if(jwindow_help.jList1.getModel().getSize()>0){	
						try {
							Rectangle r1 = jTextArea1.modelToView(jTextArea1.getCaretPosition());
							Point jtextAreaPoint = jTextArea1.getLocationOnScreen();
							jwindow_help.setLocation(jtextAreaPoint.x+r1.x,jtextAreaPoint.y+r1.y+15);
							jwindow_help.setVisible(true);
							jwindow_help.jList1.setSelectedIndex(0);//设置默认选中第一条，方便用户操作
							} catch (BadLocationException e) {
								if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
									log.error(null, e);
								}
								JOptionPane.showMessageDialog(this, e.getMessage());				
							}		           
					}	
				}
			}	
			return ;
    	}
    	isOnlyFieldTips = false;
    	
    	//如果Ctrl/Alt键处于按下状态，则直接return。//补充：shift键按下时，应该继续提示，因为有些字符（如下划线）需要在shift键按下时才能输入，此时是需要有智能提示的。
    	while(evt.isControlDown()||evt.isAltDown()){
    		return;
    	}    	
    	//如果是Tab键、空格键、shift、home、end键等，则直接return。
		if (evt.getKeyCode() == KeyEvent.VK_TAB 
				|| evt.getKeyCode() == KeyEvent.VK_SPACE
				|| evt.getKeyCode() == KeyEvent.VK_SHIFT
				|| evt.getKeyCode() == KeyEvent.VK_HOME
				|| evt.getKeyCode() == KeyEvent.VK_CONTROL
				|| evt.getKeyCode() == KeyEvent.VK_ENTER
				|| evt.getKeyCode() == KeyEvent.VK_DOWN
				|| evt.getKeyCode() == KeyEvent.VK_UP
				|| evt.getKeyCode() == KeyEvent.VK_LEFT
				|| evt.getKeyCode() == KeyEvent.VK_RIGHT
				|| evt.getKeyCode() == KeyEvent.VK_END) {
			return;
		}	
    	// 获得当前光标在编辑器中的位置   
    	int cur = jTextArea1.getCaretPosition();     
    	// 获得到光标之前的字符串   
    	String headString = jTextArea1.getText().substring(0,cur);   // select * from pub_organ,pub_stru,p
    	
    	//如果光标前数据以空格结尾，则直接return。//应对按backspace键，并且backspace键前有空格的场景。
    	if(headString.endsWith(" ")){
    		return;
    	}
    	//去除左右空格后的，光标之前的数据。
    	String trimHead = headString.trim();
    	
    	//当光标之前无数据时，直接返回。
    	if(trimHead.equals("")){
    		return;
    	}	  
    	//换行符、逗号、等号、空格，取最大值，即离光标最近的，然后取其之间的字符作为查询的前缀。
    	int lastLine = trimHead.lastIndexOf("\n");
    	int lastDouHao = trimHead.lastIndexOf(",");
    	int lastDengHao = trimHead.lastIndexOf("=");
    	int lastKongGe = trimHead.lastIndexOf(" ");
    	int max = MathUtil.max(lastLine,lastDouHao,lastDengHao,lastKongGe);
    	currentPre = trimHead.substring(max+1);
    	if("".equals(currentPre)){//pub_organ,  的场景
    		return;
    	}  	
    	//对于特殊情况的处理
		if(currentPre.contains("'")||currentPre.endsWith("\"")||currentPre.endsWith("%")||currentPre.endsWith("(")||currentPre.endsWith("<")||currentPre.endsWith("{")){
			return;
		}
		//.之后的持续的字段提示
		int lastDian = trimHead.lastIndexOf("."); //select * from pub_organ,pub_stru where pub_organ.organ_id=pub_stru.organ_i
		if(lastDian > max){
			
			//取点之前的表名
			int max2 = MathUtil.max(lastDengHao,lastKongGe);
			String tname = trimHead.substring(max2+1,lastDian).toUpperCase();
			
			//取得.之后的部分字段
			String partField = trimHead.substring(lastDian+1).toUpperCase();
			currentPre = partField;
			
			//获取提示列表的数据模型，并清空里面的数据。
			DefaultListModel defaultListModel = (DefaultListModel)jwindow_help.jList1.getModel();
			defaultListModel.removeAllElements();

			//如果是SQL Server数据库，通过 DatabaseMetaData 来查询。
			if(dbType.equals("MICROSOFT SQL SERVER")){
				
				List<FieldInfo> clos = DBUtil.getTableColums(tname, partField);
				if(clos==null||clos.size()==0){
					jwindow_help.setVisible(false);
					return;//如果没有匹配的记录，则直接return。
				}
				for(FieldInfo col:clos){
					Object[] element = {ImageIcons.column_png,col.getFieldName()+" "+col.getFieldType()+"("+col.getFieldLength()+") ["+col.getTableName()+"]"};
					defaultListModel.addElement(element);
				}
			}else{
				StringBuilder sql = new StringBuilder();
				boolean isAppendColumnName = (partField !=null && !"".equals(partField));
				
				//查询该表下的，以.之后的部分字段开头的字段
				if(dbType.contains("ORACLE")){	
					sql.append("select COLUMN_NAME,DATA_TYPE,DATA_LENGTH from USER_TAB_COLUMNS WHERE  TABLE_NAME='");
					sql.append(tname);
					sql.append("'");
					if(isAppendColumnName){
						sql.append(" and COLUMN_NAME like '");
						sql.append(partField);
						sql.append("%'");
					}
				}else if(dbType.contains("DB2")){
					sql.append("select NAME AS COLUMN_NAME ,COLTYPE AS DATA_TYPE ,LENGTH AS DATA_LENGTH from  SYSIBM.SYSCOLUMNS WHERE TBNAME='");
					sql.append(tname);
					sql.append("'");
					if(isAppendColumnName){
						sql.append("  and NAME like '");
						sql.append(partField);
						sql.append("%'");
					}
				}else if(dbType.contains("MYSQL")){
					sql.append("select COLUMN_NAME,COLUMN_TYPE from INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='");
					sql.append(tname);
					sql.append("'");
					if(isAppendColumnName){
						sql.append("  and COLUMN_NAME like '");
						sql.append(partField);
						sql.append("%'");
					}
				}else{
					JOptionPane.showMessageDialog(this, "无效的数据库类型！"+dbType);
					return;
				}
				List<Map<String, Object>> list =(List<Map<String, Object>>)(DBUtil.executeQuery(sql.toString()).get("result"));
				for (Map<String, Object> map : list) {
					if(dbType.contains("MYSQL")){//mysql 数据库 为select的列通过as指定别名报错。
						Object[] element = {ImageIcons.column_png,map.get("COLUMN_NAME")+" "+map.get("COLUMN_TYPE")+" ["+tname+"]"};
						defaultListModel.addElement(element);
					}else{
						Object[] element = {ImageIcons.column_png,map.get("COLUMN_NAME")+" "+map.get("DATA_TYPE")+"("+map.get("DATA_LENGTH")+") ["+tname+"]"};
						defaultListModel.addElement(element);
					}
				}
			}
			if(defaultListModel.getSize()>0){	
						try {
							Rectangle r1 = jTextArea1.modelToView(jTextArea1.getCaretPosition());
							Point jtextAreaPoint = jTextArea1.getLocationOnScreen();
							jwindow_help.setLocation(jtextAreaPoint.x+r1.x,jtextAreaPoint.y+r1.y+15);
							jwindow_help.setVisible(true);
							jwindow_help.jList1.setSelectedIndex(0);//设置默认选中第一条，方便用户操作
						} catch (BadLocationException e) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
								log.error(null, e);
							}
							JOptionPane.showMessageDialog(this, e.getMessage());				
						}		           
			}	
		}else{//查询匹配的关键字、表名、视图名				
			DefaultListModel defaultListModel = (DefaultListModel)jwindow_help.jList1.getModel();
			defaultListModel.removeAllElements();

			//查询匹配的关键字
			List<String> keywordlist = KeyWordUtil.getKeyWordbeginWith(currentPre);
			if(keywordlist !=null&&keywordlist.size()>0){
				for(int p=0;p<keywordlist.size();p++){
					Object[] element = {ImageIcons.key_gif, keywordlist.get(p)+ " [关键字]"};
					defaultListModel.addElement(element);
				}
			}		
			
			//查询匹配的表名、视图名
			String preTemp = currentPre.toUpperCase();
			
			//如果是SQL Server数据库
			if(dbType.equals("MICROSOFT SQL SERVER") || dbType.equals("POSTGRESQL")){
				List<String> tableAndViewNames = DBUtil.getTableAndViewNamesBeginWith(preTemp);
				for(String names:tableAndViewNames){
					String tempArr[] = names.split(",");
					String objectName = tempArr[0];
					String objectType = tempArr[1].startsWith("T")?"[表]":"[视图]";//TABLE  VIEW
					Object[] element = {objectType.equals("[表]")?ImageIcons.table_gif:ImageIcons.view_gif, objectName+" "+objectType};
					defaultListModel.addElement(element);
				}
			//如果是Oracle/DB2数据库
			}else if(dbType.contains("DB2") || dbType.contains("ORACLE")){
				StringBuffer sql = new StringBuffer();	
					if(dbType.contains("ORACLE")){		
						sql.append("select  OBJECT_NAME,OBJECT_TYPE from sys.user_objects  ");
						sql.append(" where (OBJECT_TYPE='VIEW' or OBJECT_TYPE='TABLE') AND OBJECT_NAME like '");
						sql.append(preTemp);
						sql.append("%'");
						sql.append(" ORDER BY OBJECT_NAME ASC");
					}else if(dbType.contains("DB2")){
						sql.append("select TABNAME AS OBJECT_NAME,TYPE AS OBJECT_TYPE from syscat.tables WHERE OWNER ='");
						sql.append(info.getUsername().toUpperCase());
						sql.append("'");
						sql.append("  and TABNAME like '");
						sql.append(preTemp);
						sql.append("%'");
						sql.append(" ORDER BY OBJECT_NAME ASC");
					}else{
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error("不支持的数据库类型:"+dbType);
						}
						JOptionPane.showMessageDialog(this, "不支持的数据库类型:"+dbType);
					}		
				List<Map<String, Object>> list = (List<Map<String, Object>>)(DBUtil.executeQuery(sql.toString()).get("result"));
				if(list != null && list.size()>0){
					for (Map<String, Object> map : list) {
						String objectName = map.get("OBJECT_NAME")+"";
						String objectType = (map.get("OBJECT_TYPE")+"").startsWith("T")?"[表]":"[视图]";
						Object[] element = {objectType.equals("[表]")?ImageIcons.table_gif:ImageIcons.view_gif, objectName+" "+objectType};
						defaultListModel.addElement(element);
					}
				}	
			//如果是 MYSQL 数据库
			}else if(dbType.contains("MYSQL")){
				String table_schema = null;
				try {
					table_schema = ConnUtil.getInstance().getConn().getCatalog();//仅适用于mysql数据库
				} catch (SQLException e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null,e);
					}
				}
				StringBuilder querysql = new StringBuilder();
				querysql.append("select TABLE_NAME,TABLE_TYPE from INFORMATION_SCHEMA.TABLES where");
				querysql.append(" TABLE_NAME like '"+preTemp+"%'");
				if(table_schema != null){
					querysql.append(" and TABLE_SCHEMA='"+table_schema+"'");
				}
				List<Map<String, Object>> list = (List<Map<String, Object>>)(DBUtil.executeQuery(querysql.toString()).get("result"));
				if(list != null && list.size()>0){
					for (Map<String, Object> map : list) {
						String objectName = map.get("TABLE_NAME")+"";
						String oriType =  (map.get("TABLE_TYPE")+"");
						String objectType = null;
						if(oriType.equals("SYSTEM VIEW")){
							objectType = "[系统视图]";
						}else if(oriType.equals("BASE TABLE")){
							objectType = "[用户表]";
						}else if(oriType.equals("VIEW")){
							objectType = "[用户视图]";
						}else{
							objectType = "[未知类型]";
						}
						Object[] element = {objectType.contains("视图")?ImageIcons.view_gif:ImageIcons.table_gif, objectName+" "+objectType};
						defaultListModel.addElement(element);
					}
				}
			}else{
				JOptionPane.showMessageDialog(this, "无效的数据库类型:"+dbType);
				return;
			}
			//是否有匹配的记录
			if(defaultListModel.getSize()>0){	
				try {
					Rectangle r1 = jTextArea1.modelToView(jTextArea1.getCaretPosition());
					Point jtextAreaPoint = jTextArea1.getLocationOnScreen();
					jwindow_help.setLocation(jtextAreaPoint.x+r1.x,jtextAreaPoint.y+r1.y+16);//+16是为了使window的左上角位置下移，好看。
					jwindow_help.setVisible(true);
					jwindow_help.jList1.setSelectedIndex(0);//设置默认选中第一条
				} catch (BadLocationException e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
					JOptionPane.showMessageDialog(this, e.getMessage());				
				}		           
			}else{		
				jwindow_help.setVisible(false);
			}	
		}
	} 
    /**
     * 右下角【生成插入SQL按钮】
     * 理论上支持所有数据库
     */
    public void generateInsertSQL(){
     
    	//查询当前选项卡对应的数据库表名称
    	String tableName = getCurrentTableName();
        if(tableName == null){
       	JOptionPane.showMessageDialog(MainFrame.getInstance(), "[生成插入SQL]操作只在select * 单表查询时有效！");
        	return;
        }
       //选中行的行号列表《jtable中，第一行行号是0》
       List<Integer> rowNumberList = getSelectedRowNumberList();
        if(rowNumberList == null || rowNumberList.size()==0){
       	 JOptionPane.showMessageDialog(MainFrame.getInstance(), "请选择要生成插入SQL的行！");
       	 return;
        }
    	//查询表元数据信息
		ResultSetMetaData rm = DBUtil.getResultSetMetaData(tableName);
		if(rm == null){
			JOptionPane.showMessageDialog(MainFrame.getInstance(),"生成插入SQL操作失败！查询表元数据信息出错！");
			return;
		}
		try {
			StringBuilder sqls = new StringBuilder();
			StringBuilder insertHeader = new StringBuilder();
			insertHeader.append("INSERT INTO " + tableName + " (");
			int col_number = rm.getColumnCount();
			
			//mysql数据库的特殊处理
			String dbType = DBUtil.getDBProductInfo().getProductName();
			if(dbType.contains("MYSQL")){
				for (int j = 1; j <= col_number - 1; j++) {
					insertHeader.append("`").append(rm.getColumnName(j)).append("`, ");
				}
				insertHeader.append("`").append(rm.getColumnName(col_number)).append("` ) VALUES ( ");
			}else{
				for (int j = 1; j <= col_number - 1; j++) {
					insertHeader.append(rm.getColumnName(j) + ", ");
				}
				insertHeader.append(rm.getColumnName(col_number)+ " ) VALUES ( ");
			}
		
			//获取所有选中行的单元格的值
			MyJTable jtable = getCurrentJtable();
			for(int i=0;i<rowNumberList.size();i++){
				int rowNumber = rowNumberList.get(i);
				StringBuilder insertSql = new StringBuilder();
				insertSql.append(insertHeader);
				
				//jtable中，j=0是选择列
				for(int j=1;j<col_number;j++){
					int type = rm.getColumnType(j);
					Object cellValue = jtable.getValueAt(rowNumber, j);
					if (cellValue == null || "(null)".equals(cellValue)) {
						insertSql.append("null, ");
					}else if(cellValue.toString().startsWith("E'\\\\x") 
							&& cellValue.toString().endsWith("'::bytea")){//postgres bytea类型，16进制展示
						insertSql.append(cellValue + ", ");
					}else {
						if(DBUtil.isChar(type)){
							//对单引号的特殊处理
							cellValue = cellValue.toString().replace("'", "''");
							insertSql.append("'" + cellValue + "', ");
						} else {
							insertSql.append(cellValue + ", ");
						}
					}
				}
				// 对于最后一列的特殊处理
				int type = rm.getColumnType(col_number);
				Object cellValue = jtable.getValueAt(rowNumber, col_number);
				if (cellValue == null || "(null)".equals(cellValue)) {
					insertSql.append(null + " );");
				} else {
					if(DBUtil.isChar(type)){
						cellValue = cellValue.toString().replace("'", "''");
						insertSql.append("'" + cellValue + "' );");
					} else {
						insertSql.append(cellValue + " );");
					}
				}
				//每个选中的行生成一个insert语句
				sqls.append(insertSql);
				// 每个sql进行换行
				sqls.append("\n");	
			}
			//弹出显示窗口
			MyNotePad td = new MyNotePad(getInstance(),"生成插入SQL["+tableName+"]",sqls.toString(),null);
			td.setVisible(true);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(),"出错了！"+e.getMessage());
		}
    }
    /**
     * 右下角【生成UPDATE SQL按钮】
     * 理论上支持所有数据库
     */
    public void generateUpdateSQL(){
     
    	//查询当前选项卡对应的数据库表名称
    	String tableName = getCurrentTableName();
        if(tableName == null){
       	JOptionPane.showMessageDialog(MainFrame.getInstance(), "[生成更新SQL]操作只在select * 单表查询时有效！");
        	return;
        }
       //选中行的行号列表《jtable中，第一行行号是0》
       List<Integer> rowNumberList = getSelectedRowNumberList();
        if(rowNumberList == null || rowNumberList.size()==0){
       	 JOptionPane.showMessageDialog(MainFrame.getInstance(), "请选择要生成更新SQL的行！");
       	 return;
        }
    	//查询表元数据信息
		ResultSetMetaData rm = DBUtil.getResultSetMetaData(tableName);
		if(rm == null){
			JOptionPane.showMessageDialog(MainFrame.getInstance(),"生成插入SQL操作失败！查询表元数据信息出错！");
			return;
		}
		List<String> primaryKeyList = DBUtil.getPrimaryKeyList(tableName,MainFrame.getInstance().getDataSourceInfo().getUsername());
		if(primaryKeyList == null || primaryKeyList.isEmpty()){
			JOptionPane.showMessageDialog(this,"表[" +tableName+"]不存在主键，无法导出UPDATE SQL！");
			return;
		}
		try {
			StringBuilder sqls = new StringBuilder();
			int col_number = rm.getColumnCount();
			
			//mysql数据库的特殊处理
			String dbType = DBUtil.getDBProductInfo().getProductName();
		
			//获取所有选中行的单元格的值
			MyJTable jtable = getCurrentJtable();
			for(int i=0;i<rowNumberList.size();i++){
				//行号
				int rowNumber = rowNumberList.get(i);
				StringBuilder updateSql = new StringBuilder();
				updateSql.append("UPDATE ");
				if (dbType.contains("MYSQL")) {
					updateSql.append("`");
				}
				updateSql.append(tableName);
				if (dbType.contains("MYSQL")) {
					updateSql.append("`");
				}
				updateSql.append(" SET ");
				
				//用于暂存当前行的数据，后面解析主键时会用到。
				Map currentLineValue = new HashMap();
				
				//jtable中，j=0是选择列（checkbox列）
				for(int j=1;j<=col_number;j++){
					// 字段名称
					if (dbType.contains("MYSQL")) {
						updateSql.append("`");
					}
					updateSql.append(rm.getColumnName(j));
					if (dbType.contains("MYSQL")) {
						updateSql.append("`");
					}
					updateSql.append("=");
					// 字段值
					int type = rm.getColumnType(j);
					Object col = jtable.getValueAt(rowNumber, j);
					currentLineValue.put(rm.getColumnName(j), col);
					if (col == null || "(null)".equals(col)) {
						updateSql.append("null,");
					} else {
						if (DBUtil.isChar(type)) {
							//使用双单引号，对单引号进行转义
							col = String.valueOf(col).replace("'", "''");
							updateSql.append("'" + col + "',");
						} else {
							updateSql.append(col + ",");
						}
					}
				}
				//去除末尾逗号
				updateSql.deleteCharAt(updateSql.length() - 1);
				updateSql.append(" WHERE ");
				//取得主键，根据主键update
				for(String pk : primaryKeyList){
					updateSql.append(" ");
					if (dbType.contains("MYSQL")) {
						updateSql.append("`");
					}
					updateSql.append(pk);
					if (dbType.contains("MYSQL")) {
						updateSql.append("`");
					}
					updateSql.append("=");
					
					//取得主键值，使用双单引号，对单引号进行转义
					String pkValue = String.valueOf(currentLineValue.get(pk));
					pkValue = pkValue.replace("'", "''");
					
					//主键值一律当做字符类数据处理
					updateSql.append("'").append(pkValue).append("'");
					updateSql.append(" AND");
				}
				
				if(updateSql.toString().endsWith("AND")){
					//去除末尾AND
					updateSql.deleteCharAt(updateSql.length() - 1);
					updateSql.deleteCharAt(updateSql.length() - 1);
					updateSql.deleteCharAt(updateSql.length() - 1);
				}
				updateSql.append(";");
				sqls.append(updateSql);
				sqls.append("\n");
			}
			//弹出显示窗口
			MyNotePad td = new MyNotePad(getInstance(),"生成更新SQL["+tableName+"]",sqls.toString(),null);
			td.setVisible(true);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(MainFrame.getInstance(),"出错了！"+e.getMessage());
		}
    }
	/*
	 * 导出excel
	 */
	public void export2excel(){
		
		MyJTable jTable1 = getCurrentJtable();
		if(jTable1==null){
			JOptionPane.showMessageDialog(this, "当前不存在可以导出的列表！");
			return;
		}
				
		//获取当前列表的列数 
		int columnCount = jTable1.getColumnCount();
		
		//获取选中行的行号列表
		List<Integer> rowNumberList = getSelectedRowNumberList();
        if(rowNumberList == null || rowNumberList.size()==0){
       	 JOptionPane.showMessageDialog(MainFrame.getInstance(), "请选择要导出的行！");
       	 return;
        }
		//弹出路径选择框
		String path = null;
		FileDialog saveDialog = new FileDialog(this,"保存文件", FileDialog.SAVE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));//如果不指定时区，在有些机器上会出现时间误差。  
		String currentDateTime = sdf.format(new Date());	
		saveDialog.setFile(currentDateTime+".xls");
		saveDialog.setVisible(true);
		
		if(saveDialog.getDirectory()==null){//点击了取消按钮
			return;
		}else{
			path = saveDialog.getDirectory()+saveDialog.getFile();	
			if(!path.toLowerCase().endsWith(".xls")){
				path = path+".xls";
			}
			HSSFWorkbook wb = new HSSFWorkbook();	
			HSSFSheet sheet1 = wb.createSheet("结果");
			HSSFRow row=null;
			row=sheet1.createRow(0);//第一行

			//获取所有列名，输出到第一行
			for(int i=1;i<columnCount;i++){//jtable中i=0是选择列，跳过，故从1开始
				String columnName = jTable1.getColumnName(i);
				HSSFRichTextString srts=new HSSFRichTextString(columnName);
				row.createCell(i-1).setCellValue(srts);//为第一行创建单元格，并为单元格赋值，单元格起始号也是0			
			}
			
			 //获取所有选中行的单元格的是值
			 for(int i = 0;i<rowNumberList.size();i++){
				int rowNumber = rowNumberList.get(i);
				row=sheet1.createRow(i+1);
				for(int j=1;j<columnCount;j++){//jtable中，j=0是选择列
					HSSFRichTextString srts=new HSSFRichTextString(jTable1.getValueAt(rowNumber, j)+"");
					row.createCell(j-1).setCellValue(srts);
				}
			}	
			try {
				File file = new File(path);
				FileOutputStream fos = new FileOutputStream(file);
				wb.write(fos);
				fos.close();
				JOptionPane.showMessageDialog(this,"导出成功=>"+path);
				
				//用本地默认编辑器打开
				Desktop desk=Desktop.getDesktop(); 
				desk.open(file);
			} catch (Exception e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}	
	}
	/**
	 * 查询所有复选框
	 * @return
	 */
	public List<JCheckBox> getAllJCheckBox(){
		MyJTable jTable1 = getCurrentJtable();
		if(jTable1==null){
			return null;
		}
		int rowCount = jTable1.getRowCount();//行数
		if(rowCount<=0){
			return null;
		}
		List<JCheckBox> list = new ArrayList<JCheckBox>();
		for(int i=0;i<rowCount;i++){
			JCheckBox jcheckBox = (JCheckBox)jTable1.getValueAt(i, 0);
			list.add(jcheckBox);
		}
		return list;
	}
	/**
	 * 查询选中状态行的行号列表
	 * @return List<Integer>
	 */
	public List<Integer> getSelectedRowNumberList(){
		
		MyJTable jTable1 = getCurrentJtable();
		if(jTable1==null){
			return null;
		}
		int rowCount = jTable1.getRowCount();//行数
		if(rowCount<=0){
			return null;
		}
		List<Integer> rowNumberlist = new ArrayList<Integer>();
		for(int i=0;i<rowCount;i++){
			JCheckBox jcheckBox = (JCheckBox)jTable1.getValueAt(i, 0);
			if(jcheckBox.isSelected()){
				rowNumberlist.add(i);
			}
		}
		return rowNumberlist;
	}
	/*
	 * 导出至html文件
	 */
	public void export2Html() {
		
		//获取当前选项卡title
		MyJTable jTable1 = getCurrentJtable();
		if(jTable1==null){
			JOptionPane.showMessageDialog(this, "当前不存在可以导出的列表！");
			return;
		}
		//获取选中行的行号列表
		List<Integer> rowNumberList = getSelectedRowNumberList();
        if(rowNumberList == null || rowNumberList.size()==0){
       	 JOptionPane.showMessageDialog(MainFrame.getInstance(), "请选择要导出的行！");
       	 return;
        }
		//获取当前列表的列数
		int columnCount = jTable1.getColumnCount();// 列数

		// 将内容输出至外部文件
		// 弹出路径选择框
		FileDialog saveDialog = new FileDialog(this,"保存文件", FileDialog.SAVE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));//如果不指定时区，在有些机器上会出现时间误差。  
		String currentDateTime = sdf.format(new Date());	
		saveDialog.setFile(currentDateTime+".html");
		saveDialog.setVisible(true);
		String path = null;
		if(saveDialog.getDirectory()==null){//点击了取消按钮
			return;
		}else{
			path = saveDialog.getDirectory()+saveDialog.getFile();	
			if (!path.toLowerCase().endsWith(".html")) {
				path = path  + ".html";
			}
			StringBuffer sb = new StringBuffer();
			sb.append("<html>");
			sb.append("<style>");
			sb.append("td");
			sb.append("{");
			sb.append("font-size:10pt;");
			sb.append("border:1 solid #5b99c8;font-family:Arial;");
			sb.append("}");
			sb.append("th");
			sb.append("{");
			sb.append("font-size:12pt;");
			sb.append("background-color:#8fcae9;");
			sb.append("border:1 solid #5b99c8;font-family:Arial;");
			sb.append("}</style>");
			sb.append("<head>");
			//sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">");//如果声明编码，用IE打开时常出乱码，暂不声明编码。
			sb.append("</head>");
			sb.append("<body bgcolor=\"#eff7ff\">");
			sb.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse;border:1 solid #5b99c8;\" cellpadding=2 cellspacing=0 width='100%'>");

			// 写表头
			sb.append("<tr>");

			// 获取所有列名
			for (int i = 0; i < columnCount; i++) {
				String columnName = null;
				if(i == 0){
					columnName = "";//第一列是选择列，无列名称
				}else{
					columnName = jTable1.getColumnName(i);
				}
				sb.append("<th>");
				sb.append(columnName);
				sb.append("</th>");
			}
			sb.append("</tr>");

			// 写表内容
			// 获取所有选中行的单元格的是值
			for(int i = 0;i<rowNumberList.size();i++) {
				int row = rowNumberList.get(i);
				sb.append("<tr>");
				for (int j = 0; j < columnCount; j++) {
					Object cellValue = jTable1.getValueAt(row, j);
					sb.append("<td>");
					if(j==0){
						sb.append(i+1);//序号列，从1开始编号
					}else{
						sb.append(cellValue);
					}
					sb.append("</td>");
				}
				sb.append("</tr>");
			}
			sb.append("<table></body>");
			sb.append("</html>");
			try {
				File file = new File(path);
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(sb.toString());
				fileWriter.flush();
				fileWriter.close();
				JOptionPane.showMessageDialog(this, "导出成功=>" + path);
				
				//用本地默认编辑器打开
				Desktop desk=Desktop.getDesktop(); 
				desk.open(file);
				
			} catch (IOException e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			
		}
	}
	/**
	 * 返回当前选项卡的title
	 * @return
	 */
	public String getCurrentTabTitle(){
		
		//如果jTabbedPane1中选项卡个数为0，则直接返回。
		int tabCount = jTabbedPane1.getTabCount();
		if (tabCount < 1) {
			return null;
		}
		//获得当前选项卡索引
		int selectedTabIndex = jTabbedPane1.getSelectedIndex();
		if(selectedTabIndex==-1){
			return null;
		}
		//获得当前选项卡title
		String currentTabTitle = jTabbedPane1.getTitleAt(selectedTabIndex);
		return currentTabTitle;
	}
	/**
	 * 取得当前选项卡面板上的Jtable
	 */
	public MyJTable getCurrentJtable() {

		
		//获得当前选项卡title
		String currentTabTitle = getCurrentTabTitle();
		
		//根据当前tabTitle，去show_componts集合map中查询对应Jtable。
		Object obj = show_componts.get(currentTabTitle);
		if(obj instanceof MyJTable){
			return (MyJTable)obj;
		}else{
			return null;
		}
	
	}
	/*
	 * 取得所有选项卡面板上的Jtable
	 */
	public List<MyJTable> getJtables() {

		List<MyJTable>  tableList = new ArrayList<MyJTable>();
		if(jTabbedPane1 != null){
			int tabCount = jTabbedPane1.getTabCount();
			if (tabCount >0) {
				for(int i=0;i<tabCount;i++){
					String currentTabTitle = jTabbedPane1.getTitleAt(i);
					Object obj = show_componts.get(currentTabTitle);
					if(obj instanceof MyJTable){
						tableList.add((MyJTable)obj);
					}
				}		
			}
		}
		return tableList;
	}
	/*
	 * 单个Jlabel展示执行结果
	 */
	private void showJlableResult(String tabTitle,String resultInfo,String sql){
	
		clear(true);
    	
    	//设置提示标签的字体大小和颜色 	
		JLabel label = new JLabel();
		label.setForeground(Color.BLUE);
		label.setFont(SysFontAndFace.font14);
		label.setText(getResultInfo(sql, resultInfo));
		jTabbedPane1.add(tabTitle,new JScrollPane(label));
    	
    	//用于解决选中sql执行，点击【执行】按钮执行时，JtextArea失去焦点并且选中sql处于非选中状态的问题。
    	jTextArea1.requestFocusInWindow();
	}
	/**
	 * 点击面板右下角的刷新按钮，则重新执行当前选项卡对应的sql，并只更新当前选项卡。
	 */
	private void refresh(){
		int selectIndex = jTabbedPane1.getSelectedIndex();
		//Returns the currently selected index for this tabbedpane. Returns -1 if there is no currently selected tab.
		if(selectIndex == -1){
			JOptionPane.showMessageDialog(this, "你还没有执行SQL，请先执行SQL！");
			return;
		}
		String currentTabTitle = jTabbedPane1.getTitleAt(selectIndex);
		String sql = sqlMap.get(currentTabTitle);//导入sql操作时，此处sql是null
		if(sql == null){
			JOptionPane.showMessageDialog(this, "找不到当前选项卡对应的SQL，无法重新执行！");
			return;
		}
		int value = JOptionPane.showConfirmDialog(this, "确认重新执行SQL：\n"+sql+" ?","确认对话框", JOptionPane.YES_NO_OPTION);		
		if(value == JOptionPane.YES_OPTION){
			doAction(sql, true,selectIndex,currentTabTitle);
		}
	}
    /**
     * 执行sql命令
     * in_sql等于null，则执行sql编辑器中的sql
     * in_sql 不等于null，并且isRefresh 等于false时，则只执行传递过来的in_sql，并更新所有选项卡
     * in_sql 不等于null，并且isRefresh 等于true时，则只执行传递过来的in_sql，并只更新当前选项卡
     * @param int selectIndex, 要刷新的选项卡的索引
     * @param String currentTabTitle 要刷新的选项卡的标题
     */
    private void doAction(final String in_sql,final boolean isRefresh,final int selectIndex,final String currentTabTitle){
		
		//提示信息
    	jLabel_time.setText("<html><font color=blue>正在执行，请稍后...</font></html>");
    	jLabel_time.setIcon(ImageIcons.wait_gif32);
    	jButton_do.setEnabled(false);
    	
    	//新启线程，执行sql
    	Thread thread = new Thread(){
    		@Override
    		public void run() {
    			execute(in_sql,isRefresh,selectIndex,currentTabTitle);
    		}
    	};
    	thread.start();
	}
    /**
     * 共doAction在线程中调用
     * @param in_sql
     * @param isRefresh
     * @param selectIndex
     * @param currentTabTitle
     */
    Object lock = new byte[0];
    private  void execute(String in_sql,boolean isRefresh,int selectIndex,String currentTabTitle){
    	//此处加同步锁，是为了防止一次查询未结束时，用户再次执行时，数据出现混乱
    	synchronized(lock){
    		String dbType = DBUtil.getDBProductInfo().getProductName();
        	
        	//首先执行入参传递进来的sql
        	String sql = in_sql;
        	if(sql == null || "".equals(sql.trim())){//如果入参为空，则执行选中的sql，
        		sql = jTextArea1.getSelectedText();
        		if(sql == null || "".equals(sql.trim())){//如果入参为空，并且选中的sql也为空，则执行sql编辑器中的所有sql	
        			sql = jTextArea1.getText();
        			if(sql==null||"".equals(sql.trim())){//如果sql编辑器中也不存在sql		
        				final String sql_final = sql;
        				SwingUtilities.invokeLater(new Runnable(){
        					public void run() {
        						jButton_do.setEnabled(true);
        						showJlableResult("执行结果","执行<b><font color ='red'>失败</font></b>，当前编辑器中无数据，请输入您要执行的sql语句！",sql_final);
        					}
        				});
        				return;
        			}
        		}
        	}
    		sql = sql.trim();//去除起始、结尾的空格	
    		
    		//--sql注释
    		//select * from pub_apps;;;;
    		//select * from 
    		//pub_organ;
    		//select * from pub_stru
    		List<String> executeSqlList = new ArrayList<String>();
    		String sqls[] = sql.split("\n");//以换行符进行分隔
    		
    		//只有一行的场景
    		if(sqls.length == 1){
    			if(sqls[0].startsWith("--")||sqls[0].startsWith("//")){
    				final String sql_final = sql;
    				SwingUtilities.invokeLater(new Runnable(){
    					public void run() {
    						jButton_do.setEnabled(true);
    						showJlableResult("执行结果","执行<b><font color ='red'>失败</font></b>，当前编辑器中sql已被注释，请去除注释符后重新执行！",sql_final);
    					}
    				});
    				return;
    			}
    			//去除sql结尾的分号
    			while(sqls[0].endsWith(";")){		
    				sqls[0] = sqls[0].substring(0, sqls[0].length()-1);
    			}
    			executeSqlList.add(sqls[0].trim());
    			
    			//多行的场景
    		}else{		
    			StringBuilder sql_b = new StringBuilder();
    			String readLine = null;
    			for(int temp=0;temp<sqls.length;temp++){
    				readLine = sqls[temp].trim();
    				if(readLine.startsWith("--") || readLine.startsWith("//")){//双中划线开头，双斜线开头的 行视为注释，跳过。
    					continue;
    				}
    				sql_b.append(" "+readLine);
    				if(readLine.endsWith(";")){//一条完整sql语句结束
    					while(sql_b.toString().endsWith(";")){
    						sql_b.deleteCharAt(sql_b.length()-1);//删除末尾的分号					
    					}
    					executeSqlList.add(sql_b.toString().trim());
    					if(temp+1 != sqls.length){					
    						sql_b = new StringBuilder();
    					}
    				}else if(temp+1 == sqls.length && !readLine.endsWith(";")){//对于最后一行的特殊处理，最后一行有可能没有分号。
    					executeSqlList.add(sql_b.toString().trim());
    				}
    			}
    		}	
    		//多行sql都被注释的场景
    		if(executeSqlList.size() == 0){
    			final String sql_final = sql;
    			SwingUtilities.invokeLater(new Runnable(){
    				public void run() {
    					jButton_do.setEnabled(true);
    					showJlableResult("执行结果","执行<b><font color ='red'>失败</font></b>，当前编辑器中sql已被注释，请去除注释符后重新执行！",sql_final);
    				}
    			});
    			return;
    		}
        	
        	//设置事务是否自动提交
        	boolean isAutoCommit = isEnableAutoCommit();
        	try {
        		ConnUtil.getInstance().getConn().setAutoCommit(isAutoCommit);
        	} catch (SQLException e) {
        		if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
        			log.error(null, e);
        		}
        		String mess = null;
        		if(isAutoCommit){
        			mess = "设置事务自动提交失败，是否以事务非自动提交方式运行？";
        		}else{
        			mess = "设置事务非自动提交失败，是否以事务自动提交方式运行？";
        		}
        		int value = JOptionPane.showConfirmDialog(getInstance(), mess,"确认对话框", JOptionPane.YES_NO_OPTION);		
        		if(value == JOptionPane.NO_OPTION){
        			jLabel_time.setText(null);
        			jLabel_time.setIcon(null);
        			jButton_do.setEnabled(true);
        			return;
        		}
        	}
        	
        	//记录开始时间
        	long beginTime = System.currentTimeMillis();
        	
        	if(!isRefresh){	
        		try {
    				SwingUtilities.invokeAndWait(new Runnable(){
    					public void run() {
    						//如果不是刷新，则执行sql之前，首先清空上次执行保存的数据信息
    						clear(false);
    						//用于解决选中sql执行，点击【执行】按钮执行时，JtextArea失去焦点并且选中sql处于非选中状态的问题。
    						jTextArea1.requestFocusInWindow();
    					}
    				});
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
        	}

    		//分别执行每条sql
    		for(int a=0;a<executeSqlList.size();a++){
    			
    			String key = null;
    			if(isRefresh){	
    				key = currentTabTitle;
    			
    				//组织 key，以每句sql的首个单词，加下划线和选择
    			}else{
    				int index1 = executeSqlList.get(a).indexOf(" ");//indexOf(String str)   返回第一次出现的指定子字符串在此字符串中的索引。
    				if(index1 != -1){
    					key = executeSqlList.get(a).substring(0, index1) +"_"+(a+1);	
    				}else{
    					key = executeSqlList.get(a)+"_"+(a+1);;//不包含空格的sql，一般是无效sql
    				}
    				//记录执行过的SQL
    				sqlMap.put(key, executeSqlList.get(a));
    			}
    			
    			//如果是执行明确的查询类操作
    			String temp = executeSqlList.get(a).toLowerCase();
    			if(temp.startsWith("select")||(temp.startsWith("desc") && !temp.startsWith("describe"))||temp.startsWith("getchildren")
    					||temp.startsWith("generatecreatesql")||temp.startsWith("index")||temp.startsWith("generateinsertsql")
    					||temp.startsWith("enable")||temp.startsWith("disable")){
    				
    				List<Map<String, Object>> list = null;
    				if(temp.startsWith("desc")){//查看表结构
    					
    					if(!DBUtil.isSupportedDBType()){
    						result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，该功能暂不支持当前数据库类型！"));
    						show_componts.put(key, new JLabel());
    						
    						//设置完展现内容、展现组件，即可continue，进行下一个sql语句的执行。
    						continue;
    					}
    					
    					String arr[] = executeSqlList.get(a).trim().split(" ");// desc pub_organ
    					String tableName = arr[arr.length-1];
    					
    					if(dbType.equals("MYSQL")){
    						String table_schema = null;
    						try {
    							table_schema = ConnUtil.getInstance().getConn().getCatalog();
    						} catch (SQLException e1) {
    							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
    								log.error(null, e1);
    							}
    						}
    						//查询所有列信息
    						String sql1 = "select TABLE_NAME,COLUMN_NAME,COLUMN_TYPE,COLUMN_DEFAULT,IS_NULLABLE from information_schema.COLUMNS  where TABLE_SCHEMA='"+table_schema+"' and TABLE_NAME='"+tableName+"'";
    						Map<String,Object> map1 = DBUtil.executeQuery(sql1);
    						if(map1.get("msg")!=null){
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+map1.get("msg").toString()));
    							show_componts.put(key, new JLabel());
    							continue;
    						}else{
    							//列信息 list1，在list1的基础上，分别添加主键、外键信息
    							List<Map<String, Object>> list1 = (List<Map<String, Object>>)map1.get("result");
    							if(list1==null || list1.size() == 0){
    								result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，对象名"+tableName+"无效，未获取到其相关信息！"));
    								show_componts.put(key, new JLabel());
    								continue;
    							}
    							
    							//1、添加主键信息
    							List<String> pk_list = DBUtil.getPrimaryKeyList_mysql(tableName);
    						
    							//如果该表存在主键
    							if(pk_list != null && pk_list.size() > 0){
    								for(Map<String, Object> record:list1){
    									String column_name = record.get("COLUMN_NAME")+"";
    									if(pk_list.contains(column_name)){
    										record.put("是否主键", "是");
    									}else{
    										record.put("是否主键", "否");
    									}
    								}
    							}else{
    								for(Map<String, Object> record:list1){
    									record.put("是否主键", "否");
    								}
    							}
    							//2、添加外键信息
    							String sql2 = "select CONSTRAINT_NAME,COLUMN_NAME,REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME from information_schema.KEY_COLUMN_USAGE where TABLE_SCHEMA='"+table_schema+"' and TABLE_NAME='"+tableName+"' and CONSTRAINT_NAME in(select CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where TABLE_SCHEMA='"+table_schema+"' and table_name='"+tableName+"' and CONSTRAINT_TYPE='FOREIGN KEY')";
    							Map<String,Object> map2 = DBUtil.executeQuery(sql2);
    							if(map2.get("msg")!=null){
    								result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+map2.get("msg").toString()));
    								show_componts.put(key, new JLabel());
    								continue;
    							}else{
    								List<Map<String, Object>> list2 = (List<Map<String, Object>>)map2.get("result");
    								
    								//如果该表存在外键
    								if(list2.size() > 0){
    								for(Map<String, Object> record:list1){
    									String column_name = record.get("COLUMN_NAME")+"";
    									for(Map<String, Object> record2 : list2){
    										if(column_name.equals(record2.get("COLUMN_NAME"))){
    											record.put("是否外键", "是");
    											record.put("外键关联表", record2.get("REFERENCED_TABLE_NAME"));
    											record.put("外键关联表字段", record2.get("REFERENCED_COLUMN_NAME"));
    											break;
    										}else{
    											record.put("是否外键", "否");
    											record.put("外键关联表", "");
    											record.put("外键关联表字段","");
    										}
    									}
    								}
    								}else{
    									for(Map<String, Object> record:list1){
    										record.put("是否外键", "否");
    										record.put("外键关联表", "");
    										record.put("外键关联表字段","");
    									}
    								}
    							}
    							
    							//3、封装完毕
    							list = list1;
    						}
    					//DB2、Oracle数据库的场景
    					}else if(dbType.contains("DB2") || dbType.contains("ORACLE")){
    						Map<String,Object> getMap = DBUtil.descTableInfo(tableName);
    						if(getMap.get("msg")!=null){
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+getMap.get("msg").toString()));
    							show_componts.put(key, new JLabel());
    							continue;
    						}
    						
    						//构建dataSet格式的数据集
    						List<FieldInfo> fieldInfoList = (List<FieldInfo>)getMap.get("result"); 
    						list = new LinkedList<Map<String,Object>>();
    						for(int k=0;k<fieldInfoList.size();k++){
    							Map<String,Object> fieldMap = new LinkedHashMap<String, Object>();
    							fieldMap.put("表名称", fieldInfoList.get(k).getTableName());
    							fieldMap.put("字段名称", fieldInfoList.get(k).getFieldName());
    							fieldMap.put("字段数据类型", fieldInfoList.get(k).getFieldType());
    							fieldMap.put("字段长度", fieldInfoList.get(k).getFieldLength());
    							fieldMap.put("默认值", fieldInfoList.get(k).getDefaultValue());
    							fieldMap.put("是否可以为空", fieldInfoList.get(k).getCanBeNull()?"是":"否");
    							fieldMap.put("约束类型", fieldInfoList.get(k).getConstraintType());
    							fieldMap.put("约束状态", fieldInfoList.get(k).getConstraintStatus());
    							fieldMap.put("外键关联表", fieldInfoList.get(k).getParentTableName());
    							fieldMap.put("外键关联表字段", fieldInfoList.get(k).getParentTableFieldName());
    							list.add(fieldMap);
    						}	
    					}else if(dbType.contains("POSTGRESQL") || dbType.contains("MICROSOFT SQL SERVER")){
    						list = new LinkedList<Map<String,Object>>();
    						List<FieldInfo> cols = DBUtil.getTableColums(tableName, "");
    						List<String> pks =DBUtil.getPrimaryKeyList(tableName);
    						List<FieldInfo> fks = DBUtil.getForeignKeys(tableName);
    						for(FieldInfo col : cols){
    							Map<String,Object> fieldMap = new LinkedHashMap<String, Object>();
    							fieldMap.put("表名称", col.getTableName());
    							fieldMap.put("字段名称",col.getFieldName());
    							fieldMap.put("字段数据类型", col.getFieldType());
    							fieldMap.put("字段长度", col.getFieldLength());
    							fieldMap.put("默认值", col.getDefaultValue());
    							fieldMap.put("是否可以为空", col.getCanBeNull()?"是":"否");
    							
    							//是否主键
    							if(pks.contains(col.getFieldName())){
    								fieldMap.put("是否主键", "是");
    							}else{
    								fieldMap.put("是否主键", "否");
    							}
    							//是否外键
    							FieldInfo tempField = null;
    							for(FieldInfo field:fks){
    								if(field.getFieldName().equals(col.getFieldName())){
    									tempField = field;
    									break;
    								}
    							}
    							if(tempField != null){
    								fieldMap.put("是否外键", "是");
    								fieldMap.put("外键状态", tempField.getConstraintStatus());
    								fieldMap.put("外键关联表", tempField.getParentTableName());
    								fieldMap.put("外键关联表字段", tempField.getParentTableFieldName());		
    							}else{
    								fieldMap.put("是否外键", "否");
    								fieldMap.put("外键状态", "");
    								fieldMap.put("外键关联表", "");
    								fieldMap.put("外键关联表字段", "");		
    							}
    							fieldMap.put("字段说明", col.getRemarks());		
    							list.add(fieldMap);
    						}
    						if(list.size() == 0){
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，未获取到 "+tableName+" 相关信息！"));
    							show_componts.put(key, new JLabel());
    							continue;
    						}
    					}					
    				}else if(temp.startsWith("getchildren")){//查询指定表被哪些表所引用了。
    					
    					if(!DBUtil.isSupportedDBType()){
    						result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，该功能暂不支持当前数据库类型！"));
    						show_componts.put(key, new JLabel());
    						continue;
    					}
    					
    					String arr[] = executeSqlList.get(a).trim().split(" ");// getchildren pub_organ
    					String tableName = arr[arr.length-1].trim();
    					
    					//首先检查表是否存在
						if(DBUtil.executeQuery("select * from "+tableName+" where 1=2").get("msg")!=null){
							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+tableName+" 表不存在或无效！"));
							show_componts.put(key, new JLabel());
							continue;
						}

    					if(dbType.equals("MICROSOFT SQL SERVER") || dbType.equals("POSTGRESQL")){
    						List<FieldInfo> ChildList = DBUtil.getChildTables(tableName);
    						if(ChildList.size() == 0){
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，当前表不存在子表！"));
    							show_componts.put(key, new JLabel());
    							continue;			
    						}else{
    							list = new LinkedList<Map<String,Object>>();
    							for(FieldInfo field:ChildList){
    								Map<String, Object> record = new LinkedHashMap<String, Object>();
    								record.put("主表名称", field.getParentTableName());
    								record.put("主表字段", field.getParentTableFieldName());
    								record.put("子表名称", field.getTableName());
    								record.put("子表字段", field.getFieldName());
    								list.add(record);
    							}
    						}
    					}else{		
    						if(dbType.contains("ORACLE")){
    							String getchildrenSQL = "select parent.table_name as 主表名称 ,parent.constraint_name as 主表约束,child.table_name as 子表名称,child.constraint_name as 子表约束 " +
    									"from user_constraints parent,user_constraints child " +
    									"where parent.constraint_name=child.r_constraint_name and child.constraint_type='R' and parent.table_name='"+tableName+"'";		
    							Map<String,Object> getMap = DBUtil.executeQuery(getchildrenSQL);
    							
    							//查询出错的场景
    							if(getMap.get("msg")!=null){
    								result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+getMap.get("msg").toString()));
    								show_componts.put(key, new JLabel());
    								continue;
    							}else{
    								List<Map<String, Object>> tempList = (List<Map<String, Object>>)(getMap.get("result"));
    								if(tempList!=null && tempList.size()>0){				
    									list = new LinkedList<Map<String,Object>>();
    									for(int k=0;k<tempList.size();k++){
    										Map<String, Object> map = tempList.get(k);
    										Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
    										resultMap.put("主表名称", map.get("主表名称"));
    										String getColumnSQL = "select COLUMN_NAME from user_cons_columns where constraint_name='"+map.get("主表约束")+"' and table_name='"+map.get("主表名称")+"'";
    										resultMap.put("主表字段", ((List<Map<String, Object>>)DBUtil.executeQuery(getColumnSQL).get("result")).get(0).get("COLUMN_NAME"));
    										resultMap.put("子表名称", map.get("子表名称"));
    										getColumnSQL = "select COLUMN_NAME from user_cons_columns where constraint_name='"+map.get("子表约束")+"' and table_name='"+map.get("子表名称")+"'";
    										resultMap.put("子表字段", ((List<Map<String, Object>>)DBUtil.executeQuery(getColumnSQL).get("result")).get(0).get("COLUMN_NAME"));
    										list.add(resultMap);
    									}					
    								}
    							}	
    						}else if(dbType.contains("DB2")){			
    							String getchildrenSQL = "select  REFTABNAME as 主表名称,PK_COLNAMES AS 主表字段,TABNAME as 子表名称,FK_COLNAMES AS 子表字段  " +
    							"from syscat.references WHERE  REFTABNAME='"+tableName+"'";
    							Map<String,Object> getMap = DBUtil.executeQuery(getchildrenSQL);
    							
    							//查询出错的场景
    							if(getMap.get("msg")!=null){
    								result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+getMap.get("msg").toString()));
    								show_componts.put(key, new JLabel());
    								continue;
    							}else{					
    								list = (List<Map<String, Object>>)getMap.get("result");
    							}
    						}else if(dbType.contains("MYSQL")){
    							String table_schema = null;
    							try {
    								table_schema = ConnUtil.getInstance().getConn().getCatalog();
    							} catch (SQLException e) {
    								if ("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG) + "")) {
    									log.error("出错了！", e);
    								}
    							}
    							String getChildSQL = "select REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME,TABLE_NAME,COLUMN_NAME" 
    									+" from INFORMATION_SCHEMA.KEY_COLUMN_USAGE where TABLE_SCHEMA='"+table_schema+"' and REFERENCED_TABLE_NAME='"+tableName+"' "
    									+" and CONSTRAINT_NAME in(select CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where TABLE_SCHEMA='"+table_schema
    									+"' and REFERENCED_TABLE_NAME='"+tableName+"' and CONSTRAINT_TYPE='FOREIGN KEY');";
    							Map<String,Object> getMap = DBUtil.executeQuery(getChildSQL);
    							
    							//查询出错的场景
    							if(getMap.get("msg")!=null){
    								result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+getMap.get("msg").toString()));
    								show_componts.put(key, new JLabel());
    								continue;
    							}else{	
    								List<Map<String, Object>> oldList = (List<Map<String, Object>>)getMap.get("result");
    								if(oldList != null && oldList.size()>0){//mysql select列指定别名不生效，在此通过程序来指定。
    									list = new ArrayList<Map<String,Object>>();
    									for(Map<String, Object> map:oldList){
    										Map<String, Object> newMap = new LinkedHashMap<String, Object>();
    										newMap.put("主表名称", map.get("REFERENCED_TABLE_NAME"));
    										newMap.put("主表字段", map.get("REFERENCED_COLUMN_NAME"));
    										newMap.put("子表名称", map.get("TABLE_NAME"));
    										newMap.put("子表字段", map.get("COLUMN_NAME"));
    										list.add(newMap);
    									}
    								}
    							}
    						}else{
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，暂不支持的数据库类型："+dbType+"！"));
    							show_componts.put(key, new JLabel());
    							continue;
    						}
    						//处理指定表不存在子表的场景
    						if(list==null||list.size()<1){
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+tableName+" 不存在子表！"));
    							show_componts.put(key, new JLabel());
    							continue;
    						}	
    					}
    				//生成建表sql
    				}else if(temp.startsWith("generatecreatesqlfortable")){			

    					if(!DBUtil.isSupportedDBType()){
    						result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，该功能暂不支持当前数据库类型！"));
    						show_componts.put(key, new JLabel());
    						continue;
    					}
    					// generatecreatesql v_pub_organ
    					String tableName = executeSqlList.get(a).replace(" ", "").substring("generatecreatesqlfortable".length());
    					Map<String, String> createSqlMap = DBUtil.getCreateSql(tableName);
    					if(createSqlMap.get("msg") != null){
    						result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+createSqlMap.get("msg")));
    						show_componts.put(key, new JLabel());
    						continue;
    					}else{
    						//如果成功生成了建表sql，则使用TextArea进行展现	
    						result.put(key, createSqlMap.get("sql"));
    						show_componts.put(key,new MyJextArea(true));
    						continue;
    					}
    				}
    				//生成创建视图sql
    				else if(temp.startsWith("generatecreatesqlforview")){	
    					
    					if(!DBUtil.isSupportedDBType()){
    						result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，该功能暂不支持当前数据库类型！"));
    						show_componts.put(key, new JLabel());
    						continue;
    					}
    					String arr[] = executeSqlList.get(a).trim().split(" ");// generatecreatesql v_pub_organ
    					String tableName = arr[arr.length-1].trim().toUpperCase();

    					//DB2/Oralce的场景
    					if(dbType.contains("ORACLE") || dbType.contains("DB2")){
    						
    						if(dbType.contains("ORACLE")){
    							sql="select VIEW_NAME as 视图名称,TEXT as 创建SQL from user_views where VIEW_NAME='"+tableName+"'";
    						}else if(dbType.contains("DB2")){		
    							sql="select VIEWNAME as 视图名称,TEXT as  创建SQL from syscat.VIEWS where VIEWNAME='"+tableName+"'";
    						}else {
    							JOptionPane.showMessageDialog(null, "无效的数据库类型！"+dbType);
    							System.exit(0);
    						}
    						Map<String,Object> getMapView = DBUtil.executeQuery(sql);
    						if(getMapView.get("msg")!=null){
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+getMapView.get("msg").toString()));
    							show_componts.put(key, new JLabel());
    							continue;
    						}else{						
    							list = (List<Map<String, Object>>)getMapView.get("result");
    							
    							//不存在viewName的场景
    							if(list==null||list.size()==0){
    								result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，视图"+tableName+"无效！"));
    								show_componts.put(key, new JLabel());
    								continue;
    							}else{
    								//使用TextArea进行展现
    								result.put(key, list.get(0).get("创建SQL").toString());
    								MyJextArea text = new MyJextArea(true);
    								show_componts.put(key,text);
    								continue;
    							}
    						}
    					//SQLServer的场景
    					}else if(dbType.contains("MICROSOFT SQL SERVER")){
    						String view_sql_sqlserver = "select text as 创建SQL from syscomments where id=(select id from sysobjects where xtype='V' and name='"+tableName+"')";
    						Map<String,Object> view_sql_map = DBUtil.executeQuery(view_sql_sqlserver);
    						if(view_sql_map.get("msg") != null){
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+view_sql_map.get("msg")));
    							show_componts.put(key, new JLabel());
    							continue;			
    						}else{		
    							//使用TextArea进行展现
    							list = (List<Map<String, Object>>)view_sql_map.get("result");
    							if(list.size() == 0){
    								result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，视图名 "+tableName+" 无效！"));
    								show_componts.put(key, new JLabel());
    								continue;		
    							}else{				
    								result.put(key, list.get(0).get("创建SQL").toString());
    								MyJextArea text = new MyJextArea(true);
    								show_componts.put(key,text);
    								continue;
    							}
    						}
    					//如果是MYSQL数据库	
    					}else if(dbType.contains("MYSQL")){
    						String currentConnectDBName = null;
    						String tempsql = "select VIEW_DEFINITION from information_schema.views where TABLE_NAME='"+tableName+"'";
    						try {
    							currentConnectDBName = ConnUtil.getInstance().getConn().getCatalog();//仅适用于mysql数据库
    						} catch (SQLException e) {
    							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
    								log.error(null, e);
    							}
    						}
    						if(currentConnectDBName != null){
    							tempsql = tempsql+" and TABLE_SCHEMA='"+currentConnectDBName+"'";
    						}
    						list = (List<Map<String, Object>>)DBUtil.executeQuery(tempsql).get("result");
    						if(list.size() == 0){
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，视图名 "+tableName+" 无效！"));
    							show_componts.put(key, new JLabel());
    							continue;		
    						}else{				
    							result.put(key, list.get(0).get("VIEW_DEFINITION").toString());
    							MyJextArea text = new MyJextArea(true);
    							show_componts.put(key,text);
    							continue;
    						}
    					}else{
    						JOptionPane.showMessageDialog(getInstance(), "无效的数据库类型！"+dbType);
    						System.exit(1);
    					}
    				}
    				//查询表索引信息
    				else if(temp.startsWith("index")){
    					
    					if(!DBUtil.isSupportedDBType()){
    						result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，该功能暂不支持当前数据库类型！"));
    						show_componts.put(key, new JLabel());
    						continue;
    					}
    					String arr[] = executeSqlList.get(a).trim().split(" ");// index pub_organ
    					String tableName = arr[arr.length-1].trim().toUpperCase();

    					if(dbType.equals("MICROSOFT SQL SERVER")){
    						List<IndexBean> indexList = DBUtil.getIndexForSqlServer(tableName);
    						if(indexList.size() == 0){			
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，当前表不存在索引信息！"));
    							show_componts.put(key, new JLabel());
    							continue;
    						}else{
    							list = new LinkedList<Map<String,Object>>();/////表名、索引名称、关联列（列1：desc，列2：asc）
    							for(IndexBean indexBean:indexList){
    								Map<String,Object> rec = new LinkedHashMap<String, Object>();
    								rec.put("表名称", tableName);
    								rec.put("索引名称", indexBean.getIndexName());
    								rec.put("索引类型", indexBean.isUniqueIndex()?"唯一索引":"非唯一索引");
    								List<String> colNameList = indexBean.getColNameList();
    								
    								StringBuilder reCol = new StringBuilder();
    								reCol.append("(");
    								for(int m=0;m<colNameList.size();m++){
    									reCol.append(colNameList.get(m));
    									reCol.append(":");
    									reCol.append(indexBean.getColASC_DESC().get(m));
    									reCol.append(",");
    								}
    								reCol.deleteCharAt(reCol.length()-1);
    								reCol.append(")");
    								rec.put("关联列",reCol);
    								list.add(rec);
    							}
    						}
    					}else if(dbType.contains("DB2") || dbType.contains("ORACLE")){
    						Map<String,Object> returnMap = DBUtil.getIndex(tableName);
    						if(returnMap.get("msg") != null){			
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+returnMap.get("msg")));
    							show_componts.put(key, new JLabel());
    							continue;
    						}else{
    							list = (LinkedList<Map<String,Object>>)returnMap.get("result");
    						}			
    					}else if(dbType.contains("MYSQL")){
    						String table_schema = null;
    						try {
    							//mysql中getCatalog当做数据库名称来处理。
    							table_schema = ConnUtil.getInstance().getConn().getCatalog();
    						} catch (SQLException e1) {
    							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
    								log.debug(null,e1);
    							}
    						}
    						StringBuilder querysql = new StringBuilder("select * from information_schema.STATISTICS");
    						querysql.append(" where TABLE_NAME='"+tableName+"'");
    						if(table_schema != null){
    							querysql.append(" and TABLE_SCHEMA='"+table_schema+"'");
    						}
    						list = (List<Map<String, Object>>) DBUtil.executeQuery(querysql.toString()).get("result");
    						
    						//数据库表无索引的场景
    						if(list.size()==0){
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+tableName+" 表未建索引！"));
    							show_componts.put(key, new JLabel());
    							continue;
    						}
    					}else{
    						JOptionPane.showMessageDialog(getInstance(), "暂不支持的数据库类型！"+dbType);
    					}	
    				}else if(temp.startsWith("generateinsertsql")){//生成插入sql
    			
    					if(!DBUtil.isSupportedDBType()){
    						result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，该功能暂不支持当前数据库类型！"));
    						show_componts.put(key, new JLabel());
    						continue;
    					}
    					// generateInsertSql  pub_organ where ...
    					String deleteHeaderSql = executeSqlList.get(a).trim().substring("generateInsertSql".length()).trim();
    					String tableName = deleteHeaderSql.split(" ")[0];
    					list = new LinkedList<Map<String,Object>>();		
    					PreparedStatement ps = null;
    					ResultSet rs = null;	
    					int rows = 0;
    					try{
    						ps = ConnUtil.getInstance().getConn().prepareStatement("select * from "+deleteHeaderSql);//支持where过滤条件
    						rs = ps.executeQuery();
    						ResultSetMetaData  rm = rs.getMetaData();
    						int col_number = rm.getColumnCount();
    								StringBuffer insertInit = new StringBuffer();
    								
    								//mysql的特殊处理
    								if(dbType.contains("MYSQL")){
    									insertInit.append("INSERT INTO `" + tableName + "` (");
        								for (int i = 1; i <= col_number-1; i++) {
        									insertInit.append("`").append(rm.getColumnName(i)).append("`,");
        								}
        								insertInit.append("`").append(rm.getColumnName(col_number)).append("`) VALUES (");
    								}else{
    									insertInit.append("INSERT INTO " + tableName + " (");
    									for (int i = 1; i <= col_number-1; i++) {
    										insertInit.append(rm.getColumnName(i) + ", ");
    									}
    									insertInit.append(rm.getColumnName(col_number) + " ) VALUES ( ");
    								}
    								while (rs.next()) {
    									rows++;
    									StringBuffer insertSql = new StringBuffer();
    									insertSql.append(insertInit);
    									for (int i = 1; i <= col_number - 1; i++) {
    										int type = rm.getColumnType(i);
    										String col=rs.getString(i);
    										if(col == null || "(null)".equals(col)) {
    											insertSql.append(null + ", ");
    										} else {
    											if(DBUtil.isChar(type)){
    												col = col.replace("'", "''");
    												insertSql.append("'" + col + "', ");
    											}else{
    												insertSql.append(col + ", ");
    											}
    										}
    									}	
    									//对于最后一列的特殊处理
    									int type = rm.getColumnType(col_number);
    									String col=rs.getString(col_number);
    									if(col == null || "(null)".equals(col)) {
    										insertSql.append(null + " );");
    									} else {
    										if(DBUtil.isChar(type)){
    											col = col.replace("'", "''");
    											insertSql.append("'" + col + "' );");
    										}else{
    											insertSql.append(col + " );");
    										}
    									}
    							Map<String, Object> record = new LinkedHashMap<String, Object>();
    							record.put(tableName, insertSql);
    							list.add(record);
    					}		
    					if(list.size()==0){//表中无数据的场景
    						result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+tableName+" 表中无符合条件记录，无法生成相应的插入SQL！"));
    						show_componts.put(key, new JLabel());
    						continue;
    					}
    					}catch (Exception e) {
    						result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+e.getMessage()));
    						show_componts.put(key, new JLabel());
    						continue;
    					}finally{
    						DBUtil.closeResultSetAndPreparedStatement(rs, ps);
    					}
    				//启用 或 禁用数据表约束
    				}else if(temp.startsWith("disable") || temp.startsWith("enable")){

    					if(!DBUtil.isSupportedDBType()){
    						result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，该功能暂不支持当前数据库类型！"));
    						show_componts.put(key, new JLabel());
    						continue;
    					}
    					if(dbType.contains("MYSQL")){//mysql数据库不支持启用、禁用单个表的外键，只能全部启用或者禁用
    					
    						String fk_sql = null;
    						if(temp.startsWith("disable")){
    							fk_sql = "SET FOREIGN_KEY_CHECKS='OFF'";
    						}else{
    							fk_sql = "SET FOREIGN_KEY_CHECKS='ON'";
    						}
    						Map<String, String> getMap = DBUtil.executeUpdate(fk_sql);
    						if(getMap.get("msg")!=null){
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+getMap.get("msg").toString()));
    							show_componts.put(key, new JLabel());
    							continue;
    						}else{
    							result.put(key, getResultInfo(fk_sql, "执行<b><font color ='green'>成功</font></b>，影响记录行数  "+getMap.get("result")));
    							show_componts.put(key, new JLabel());	
    							continue;
    						}
    					
    					}else{
    						if(temp.startsWith("disablefk") || temp.startsWith("enablefk")){//禁用、启用指定表的外键约束
    							List<SqlResult> sqlResultList = null;
    							if(temp.startsWith("disablefk")){
    								String tableNames[] = executeSqlList.get(a).substring("disablefk".length()).trim().split(",");
    								sqlResultList = DBUtil.disableORenbaleFK(false, tableNames);
    							}else{
    								String tableNames[] = executeSqlList.get(a).substring("enablefk".length()).trim().split(",");
    								sqlResultList = DBUtil.disableORenbaleFK(true, tableNames);
    							}		
    							list = new ArrayList<Map<String,Object>>();
    							if(sqlResultList.size() == 0){
    								result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，表名无效，或者指定表不存在外键约束！"));
    								show_componts.put(key, new JLabel());
    								continue;
    							}else{	
    								for(SqlResult r : sqlResultList){
    									Map<String,Object> map = new LinkedHashMap<String, Object>();
    									map.put("是否成功", r.isSuccess()?"成功":"失败");
    									map.put("执行SQL", r.getSql());
    									map.put("执行结果", r.getResult());
    									list.add(map);
    								}
    							}
    						}else if(temp.startsWith("disableallfk") || temp.startsWith("enableallfk")){
    							list = new ArrayList<Map<String,Object>>();
    							List<SqlResult> sqlResultList = null;
    							if(temp.startsWith("disableallfk")){
    								sqlResultList = DBUtil.disableORenableAllConstraint(false);
    							}else{
    								sqlResultList = DBUtil.disableORenableAllConstraint(true);
    							}
    							if(sqlResultList.size() == 0){
    								result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，当前数据库中不存在外键约束！"));
    								show_componts.put(key, new JLabel());
    								continue;
    							}else{					
    								for(SqlResult r : sqlResultList){
    									Map<String,Object> map = new LinkedHashMap<String, Object>();
    									map.put("是否成功", r.isSuccess()?"成功":"失败");
    									map.put("执行SQL", r.getSql());
    									map.put("执行结果", r.getResult());
    									list.add(map);
    								}		
    							}
    						}else{
    							//无效的sql语句
    							result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，无效的SQL语句！"));
    							show_componts.put(key, new JLabel());
    							continue;
    						}
    					}
    				}
    				//执行普通select查询
    				else{
    					Map<String,Object> getMap = DBUtil.executeQuery(executeSqlList.get(a));
    					
    					//查询出错
    					if(getMap.get("msg")!=null){
    						result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+getMap.get("msg").toString()));
    						show_componts.put(key, new JLabel());
    						continue;
    					
    						//查询没出错
    					}else{			
    						list = (List<Map<String, Object>>)getMap.get("result");
    						
    						//查询结果为空（即无符合条件的记录的场景）
    						if(list.size()==0){
    							Map<String, Object> colNameList = DBUtil.getColumnNameList(executeSqlList.get(a));
    							List<String> strList = (List<String>)colNameList.get("result");
    							
    							// 一维数组：title
    							Object[] titles = new String[strList.size()+1];// 列数
    							titles[0] = null;
    							for(int i=0;i<strList.size();i++){
    								titles[i+1] = strList.get(i);
    							}
    							
    							//查询结果为空时，绘制Jtable，只有表头
    							MyTableModel defaultTableModel = new MyTableModel(null,titles);//根据默认表格数据模型创建Jtable
    							MyJTable jtable = new MyJTable(defaultTableModel);
    							jtable.setModel(defaultTableModel);
    							setJtable(jtable,key);
    							
    					        result.put(key, "无符合条件记录！");
    							show_componts.put(key, jtable);
    					        continue;
    						}
    					}
    				}	
    				// 一维数组：title
    				Object[] titles = new Object[list.get(0).size()+1];// 列数
    				Map<String, Object> record1 = list.get(0);
    				Iterator<Entry<String, Object>> itor1 = record1.entrySet().iterator();
    				int i = 0;
    				titles[0] = null;
    				while (itor1.hasNext()) {
    					Entry<String, Object> e = itor1.next();
    					titles[i+1] = e.getKey().toString();
    					i++;
    				}
    				// 二维数组，内容
    				Object[][] dataset = new Object[list.size()][list.get(0).size()+1];
    				for (int l = 0; l < list.size(); l++) {
    					
    					Map<String, Object> record2 = list.get(l);
    					Iterator<Entry<String, Object>> itor2 = record2.entrySet().iterator();
    					int n = 0;
    					dataset[l][0] = new JCheckBox((l+1)+"");//第一列是选择列，以复选框的形式进行展现
    					while (itor2.hasNext()) {
    						Entry<String, Object> e = itor2.next();
    						Object value = e.getValue();
//    						if(value==null){
//    							value = "(null)";//当取出来的值是null时，则以(null)形式进行展现。
//    						}
    						dataset[l][n+1] = value;
    						n++;
    					}
    				}
    				//查询结果不为空时，绘制Jtable。
    				MyTableModel myTableModel = new MyTableModel(dataset,titles);//根据默认表格数据模型创建Jtable
    				MyJTable jtable = new MyJTable(myTableModel);	
    				setJtable(jtable,key);
    		        result.put(key, "查询返回结果记录数："+list.size());
    				show_componts.put(key, jtable);
    				continue;
    			}
    			else{
    				//不确定查询类还是更新类		
    				String selectORupdateSQL = executeSqlList.get(a).trim();
    				Map<String,Object> getMap = DBUtil.execute(selectORupdateSQL);
    				Object msg = getMap.get("msg");
    				
    				//出错的场景
    				if(msg != null){	
    					result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='red'>失败</font></b>，"+msg));
    					show_componts.put(key, new JLabel());
    					continue;
    					
    					//成功执行的场景
    				}else{
    					boolean isResultSet = Boolean.valueOf(getMap.get("isResultSet")+"");
    					if(isResultSet){
    						
    						List<Map<String, Object>> list = (List<Map<String, Object>>)getMap.get("result");
    						
    						if(list.size()==0){//如果无符合条件记录
    							Map<String, Object> colNameList = DBUtil.getColumnNameList(executeSqlList.get(a));
    							List<String> strList = (List<String>)colNameList.get("result");
    							
    							// 一维数组：title
    							Object[] titles = new String[strList.size()+1];// 列数
    							titles[0] = null;
    							for(int i=0;i<strList.size();i++){
    								titles[i+1] = strList.get(i);
    							}
    							
    							//查询结果为空时，绘制Jtable，只有表头
    							MyTableModel defaultTableModel = new MyTableModel(null,titles);//根据默认表格数据模型创建Jtable
    							MyJTable jtable = new MyJTable(defaultTableModel);
    							setJtable(jtable,key);
    					       
    							result.put(key, "无符合条件记录！");
    							show_componts.put(key, jtable);
    					        continue;
    						}else{
    							//查询结果返回记录数大于0的场景
    							// 一维数组：title
    							Object[] titles = new String[list.get(0).size()+1];// 列数
    							Map<String, Object> record1 = list.get(0);
    							Iterator<Entry<String, Object>> itor1 = record1.entrySet().iterator();
    							int i = 0;
    							titles[0] = null;
    							while (itor1.hasNext()) {
    								Entry<String, Object> e = itor1.next();
    								titles[i+1] = e.getKey().toString();
    								i++;
    							}
    							// 二维数组，内容
    							Object[][] dataset = new Object[list.size()][list.get(0).size()+1];
    							for (int l = 0; l < list.size(); l++) {
    								
    								Map<String, Object> record2 = list.get(l);
    								Iterator<Entry<String, Object>> itor2 = record2.entrySet().iterator();
    								int n = 0;
    								dataset[l][0] = new JCheckBox((l+1)+"");//第一列是选择列
    								while (itor2.hasNext()) {
    									Entry<String, Object> e = itor2.next();
    									Object value = e.getValue();
    									
    									//此处若不加此转换，Jtable 对于Date类型进行render时，有时会报错（DateFormat格式化异常）。
    									if(value instanceof Date){
    										value = value.toString();
    									}
    									dataset[l][n+1] = value;
    									n++;
    								}
    							}
    							//查询结果不为空时，绘制Jtable。
    							MyTableModel myTableModel = new MyTableModel(dataset,titles);//根据默认表格数据模型创建Jtable
    							MyJTable jtable = new MyJTable(myTableModel);	
    							setJtable(jtable,key);
    							
    					        result.put(key, "查询返回结果记录数："+list.size());
    							show_componts.put(key, jtable);
    							continue;
    						}	
    					}else{	
    						result.put(key, getResultInfo(executeSqlList.get(a), "执行<b><font color ='green'>成功</font></b>，影响记录行数  "+getMap.get("result")));
    						show_componts.put(key, new JLabel());	
    						continue;
    					}
    				}
    			}
    		}
    		try {
    			final boolean isRefresh_final = isRefresh;
    			final String currentTabTitle_final = currentTabTitle;
    			final int selectIndex_final = selectIndex;
    			final boolean isAutoCommit_final = isAutoCommit;
    			final long beginTime_final = beginTime;
    			SwingUtilities.invokeAndWait(new Runnable(){
    				public void run() {
    					//如果是刷新，则只更新当前选项卡
    					if(isRefresh_final){
    						showResult(currentTabTitle_final,show_componts.get(currentTabTitle_final),true,selectIndex_final);
    					}else{
    						//否则将每个sql的执行结果按照执行顺序分别在JtabbedPane中显示
    						int index = 0;
    						Set<String> set=show_componts.keySet();
    						Iterator<String> iter=set.iterator();
    						while(iter.hasNext()){	
    							String key = iter.next();
    							Object value =  show_componts.get(key);
    							showResult(key,value,false,index);
    							index++;
    						}
    					}
    					//处理事务，如果是非自动提交方式，则回滚事务。
    					if(!isAutoCommit_final){
    						try {
    							ConnUtil.getInstance().getConn().rollback();
    						} catch (SQLException e) {
    							JOptionPane.showMessageDialog(getInstance(), "事务非自动提交方式下，回滚事务失败！");
    						}
    					}	
    					//统计耗时信息
    					long endTime = System.currentTimeMillis();
    					String tip = null; 
    					if(isAutoCommit_final){
    						tip = "<html><body><font color=blue>"+(isRefresh_final?"重新执行当前SQL结束":"执行结束")+"，事务<b>已提交</b>，耗时 <b>";
    					}else{
    						tip = "<html><body><font color=blue>"+(isRefresh_final?"重新执行当前SQL结束":"执行结束")+"，事务<b>已回滚</b>，耗时 <b>";
    					}
    					jButton_do.setEnabled(true);
    					jLabel_time.setIcon(null);
    					jLabel_time.setText(tip+(((Long)(endTime-beginTime_final)).floatValue())/1000+"</b> 秒。</font></body></html>");
    				}
    			});
    		} catch (Exception e1) {
    			e1.printStackTrace();
    		}
    		
    	}
    	
    }
    /**
     * 显示查询结果
     * @param key
     * @param value
     * @param isRefresh 是否刷新
     * @param selectIndex 如果是刷新，则只更新当前索引选项卡的内容
     */
    private void showResult(String key,Object value,boolean isRefresh,int selectIndex){
    
    	//如果value值是Jtable	
		if(value instanceof MyJTable) {
			if(isRefresh){
				jTabbedPane1.remove(selectIndex);
				jTabbedPane1.insertTab(key, ImageIcons.table_gif, new JScrollPane((MyJTable)value), sqlMap.get(key), selectIndex);
				jTabbedPane1.setSelectedIndex(selectIndex);
				//jTabbedPane1.setComponentAt(selectIndex, new JScrollPane((MyJTable)value));//该方式无法设置图标和提示信息
			}else{
				jTabbedPane1.insertTab(key, ImageIcons.table_gif, new JScrollPane((MyJTable)value), sqlMap.get(key), selectIndex);
				//jTabbedPane1.add(key,new JScrollPane((MyJTable)value));
			}
		
		//对于查询出错、更新操作，是不需要构件Jtable的，此时只需在tab页中放置一个lable即可
		}else if(value instanceof JLabel){
		
			//设置提示标签的字体大小和颜色 	
			JLabel label = (JLabel)value;
			label.setForeground(Color.BLUE);
			label.setFont(SysFontAndFace.font14);
			label.setText(result.get(key));
			if(isRefresh){
				jTabbedPane1.remove(selectIndex);
				jTabbedPane1.insertTab(key, ImageIcons.txt_gif, new JScrollPane((JLabel)value), sqlMap.get(key), selectIndex);
				jTabbedPane1.setSelectedIndex(selectIndex);
			}else{	
				jTabbedPane1.insertTab(key, ImageIcons.txt_gif, new JScrollPane((JLabel)value), sqlMap.get(key), selectIndex);
//				jTabbedPane1.add(key,new JScrollPane(label));
			}
			
		//生成建表sql、生成创建视图sql时，用MyJextArea展现
		}else if(value instanceof MyJextArea){
			
			final MyJextArea area = (MyJextArea)value;
			area.setBackground(((MyColor)ConfigUtil.getConfInfo().get(Const.EYE_SAFETY_COLOR)).getColor());
			area.setFont((Font)ConfigUtil.getConfInfo().get(Const.SQL_FONT));
			area.setEditable(false);
			area.setText(result.get(key));
			
			 //CTRL+F 弹出查找替换对话框
			area.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyPressed(java.awt.event.KeyEvent evt) {
					if (evt.isControlDown()) {
						if (evt.getKeyCode() == KeyEvent.VK_F) {// ctrl+f 执行查找替换													// 
							showFindDialog(area);
						}}
				}
			});
			//为右键菜单【查找替换】添加弹出对话框事件。
			area.find.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					showFindDialog(area);
				}   	
	        });    
			if(isRefresh){
				jTabbedPane1.remove(selectIndex);
				jTabbedPane1.insertTab(key, ImageIcons.txt_gif, new JScrollPane((MyJextArea)value), sqlMap.get(key), selectIndex);
				jTabbedPane1.setSelectedIndex(selectIndex);
			}else{	
				jTabbedPane1.insertTab(key, ImageIcons.txt_gif, new JScrollPane((MyJextArea)value), sqlMap.get(key), selectIndex);
				//jTabbedPane1.add(key,new JScrollPane(area));
			}
		}else{
			JOptionPane.showMessageDialog(getInstance(), "出错了！未知类型：value="+value);
		}
    }
    /**
     * 表头的右键菜单
     * @param jtableHeader
     * @param e
     */
    private void showJtableHeaderPopMenu(JTableHeader jtableHeader,MouseEvent e){
		int c = jtableHeader.columnAtPoint(e.getPoint());
		if(c == 0){
			return ;//如果是在第一列右击，则直接return。
		}
		TableColumn tc = jtableHeader.getColumnModel().getColumn(c);
		currentSelectedCellValue = tc.getHeaderValue()+"";
		jPopupMenu_table.show(e.getComponent(), e.getX(), e.getY());
	
    }
    /**
     * 表体的右键菜单
     * @param finalJtable
     * @param e
     */
    private void showJtablePopMenu(MyJTable finalJtable,MouseEvent e){
    	
		int c = finalJtable.columnAtPoint(e.getPoint());
		if(c == 0){
			return ;//如果是在第一列右击，则直接return。
		}
		int r = finalJtable.rowAtPoint(e.getPoint());
		currentSelectedCellValue = finalJtable.getValueAt(r, c);	
		
		//右键 则选中所点击处的单元格。
		finalJtable.setColumnSelectionInterval(c, c);
		finalJtable.setRowSelectionInterval(r, r);
	    jPopupMenu_table.show(e.getComponent(), e.getX(), e.getY());
    }
    /**
     * 组装执行结果信息
     */
    private String getResultInfo(String sql,String msg){
    	StringBuffer info = new StringBuffer("<html> <body>");
    	info.append("<b>执行结果</b>：");
    	info.append(msg);
		info.append("<br>");
		info.append("<br>");
		info.append("<b>执行SQL</b>：");
		info.append(sql);
		info.append("</body> </html>");
		return info.toString();
    }
    /**
     * 设置列宽根据内容自适应
     * @param myTable
     */
	public void fitTableColumns(MyJTable myTable){
	
	  JTableHeader header = myTable.getTableHeader();
	  header.setFont(SysFontAndFace.font14);//设置标题列字体
	     int rowCount = myTable.getRowCount();
	     Enumeration<TableColumn> columns = myTable.getColumnModel().getColumns();
	     while(columns.hasMoreElements()){
	         TableColumn column = (TableColumn)columns.nextElement();
	         int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
	         int width = (int)myTable.getTableHeader().getDefaultRenderer()
	                 .getTableCellRendererComponent(myTable, column.getIdentifier()
	                         , false, false, -1, col).getPreferredSize().getWidth();
	         for(int row = 0; row<rowCount; row++){
	        	 TableCellRenderer cellRender = myTable.getCellRenderer(row, col);
	        	 Object value = myTable.getValueAt(row, col);
	        	 Component renderComponent = cellRender.getTableCellRendererComponent(myTable,value, false, false, row, col);
	        	 int preferedWidth = (int)renderComponent.getPreferredSize().getWidth();
	             width = Math.max(width, preferedWidth);
	         }
	         header.setResizingColumn(column); // 此行很重要
	         column.setWidth(width+myTable.getIntercellSpacing().width+10);//+10是为了使列中的数据和列边框之间有段距离，好看。
	     }
	}
	/**
	 * 设置Jtable（目前共5处调用）
	 * 查询结果列表通用设置
	 * 1、导入SQL 
	 * 2、普通select查询，结果为空，只有表头
	 * 3、已知的查询类型，并且结果不为空（select，generate、index、desc等）
	 * 4、未知的查询类型，结果为空，只有表头（如mysql特有的show...）
	 * 5、未知的查询类型，结果不为空
	 */
	public void setJtable(MyJTable in_jtable,String key){
		final MyJTable jtable = in_jtable;
		final MyTableModel myTableModel = (MyTableModel)in_jtable.getModel();
		jtable.setDefaultEditor(Object.class,objectDefaultCellEditor);//为表格指定默认编辑器
		jtable.setDefaultEditor(Number.class,objectDefaultCellEditor);//number及其下属类型，此处也需要指定，如果仅指定Object类型，Number类型会不生效。
		jtable.setDefaultEditor(Boolean.class,objectDefaultCellEditor);//Boolean如不指定，则默认编辑器是checkbox
		
		//设置第一列展现形式、编辑形式是复选框
		TableColumn  firstColumn = jtable.getColumnModel().getColumn(0);
		firstColumn.setCellRenderer(jtableCellJcheckRender);
		firstColumn.setCellEditor(jtableCellJcheckEditor);
		
		//设置Jtable背景色
		jtable.setBackground(((MyColor)ConfigUtil.getConfInfo().get(Const.EYE_SAFETY_COLOR)).getColor());
		
		//为Jtable添加右键菜单
		final MyJTable finalJtable = jtable;
		finalJtable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					showJtablePopMenu(finalJtable, e);
				}
			}});
		//为 JTableHeader第一列设置为jcheckbox，实现全选、取消全选功能
		MyJTableHeaderCellJCheckBoxRender jtableHeaderCellJcheckRender =  new MyJTableHeaderCellJCheckBoxRender();
		firstColumn.setHeaderRenderer(jtableHeaderCellJcheckRender);
		
		
		//为 JTableHeader 添加右键菜单
		final JTableHeader finalJtableHeader = jtable.getTableHeader();	
		finalJtableHeader.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				//如果是右击
				if (e.getButton() == 3) {
					showJtableHeaderPopMenu(finalJtableHeader, e);
				}else{
					//点击列头的复选框，实现《全选、取消全选》
					int c = finalJtableHeader.columnAtPoint(e.getPoint());
					if(c == 0){
						//暂停排序（点击第一列的列头，不执行排序）
						finalJtable.setRowSorter(null);	
						TableColumn tc = finalJtableHeader.getColumnModel().getColumn(c);
						JCheckBox checkbox = (JCheckBox)tc.getHeaderRenderer();
						boolean isSelected = checkbox.isSelected();
						checkbox.setSelected(!isSelected);
						List<JCheckBox> list = getAllJCheckBox();
						if(list != null){
							for(int i=0;i<list.size();i++){
								list.get(i).setSelected(!isSelected);
							}
						}
					}else{
						//重启排序
						RowSorter<MyTableModel> sorter = jtableSorterMap.get(getCurrentTabTitle());
						finalJtable.setRowSorter(sorter);
					}
				}
			}
		});
		jtable.setColumnSelectionAllowed(false);//设置：点击某个单元格，则选中该单元格所在行。
		//jtable.setCellSelectionEnabled(true);//true 鼠标单击时，选中所单击的单元格
		jtable.setAutoResizeMode(MyJTable.AUTO_RESIZE_OFF); //开启横向滚动条
		
		//设置Jtable字体
		if(ConfigUtil.getConfInfo().get(Const.JTABLE_FONT)!=null){	
			jtable.setFont((Font)ConfigUtil.getConfInfo().get(Const.JTABLE_FONT));
		}
        fitTableColumns(jtable);//设置列宽,根据内容自动调整
        jtable.setRowHeight(Integer.parseInt(ConfigUtil.getConfInfo().get(Const.JTABLE_LINE_HEIGHT)+""));//设置行高	 
       
        //点击表格列头进行排序，（jdk6开始支持）      
        TableRowSorter<MyTableModel> tableRowSorter = new TableRowSorter<MyTableModel>((MyTableModel)jtable.getModel());
        jtable.setRowSorter(tableRowSorter);	
        jtableSorterMap.put(key, (RowSorter<MyTableModel>) finalJtable.getRowSorter());
        
      //检测表格value值变化，只针对于单表select * 查询
		jtable.getModel().addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent e) {
				if(!DBUtil.isSupportedDBType()){
					return;
				}
				if(!jtable.isCellValueChanged()){
					return;
				}
				//当前表名称
				String tableName = getCurrentTableName();
				//查询表的主键
    			List<String> primaryKeyList = DBUtil.getPrimaryKeyList(tableName, info.getUsername());
    			
    			//有些表是没有主键的，没有主键的表暂时不支持更新操作
    			if(primaryKeyList == null || primaryKeyList.size()==0){
    				return;
    			}
				int row = e.getFirstRow();//当前行
				int column = e.getColumn();//当前列
				
				//查询表元数据信息
				ResultSetMetaData rm = DBUtil.getResultSetMetaData(tableName);
				if(rm == null){
					return;
				}
				//---------检查是否新增记录，新增记录不进行检查--------------
				int columnCount = 0;
				try {
					columnCount = rm.getColumnCount();
					StringBuilder selectSQL = new StringBuilder("select count(*) as count from ");
					selectSQL.append(tableName);
					selectSQL.append(" where ");
					for(int i=1;i<=columnCount;i++){
						String columnName_temp = rm.getColumnName(i);
						if(primaryKeyList.contains(columnName_temp)){
							selectSQL.append(columnName_temp);
							selectSQL.append("=");
							int columnType = rm.getColumnType(i);
							Object value = myTableModel.getValueAt(row, i);
							System.out.println(value+" -- "+(value instanceof java.sql.Date));
							if(value == null || "(null)".equals(value)){
								selectSQL.append("null");
							}else{
								if(DBUtil.isChar(columnType)){
									selectSQL.append("'");
									value = value.toString().replace("'", "''");
									selectSQL.append(value);
									selectSQL.append("'");
								}else{
									selectSQL.append(value);
								}
							}
							selectSQL.append(" and ");
						}
					}
					selectSQL.delete(selectSQL.length()-5, selectSQL.length()-1);
					
					Map<String, Object> result = DBUtil.executeQuery(selectSQL.toString());
					if(result.get("msg") != null){
						return;
					}
					List<Map> rs = (List<Map>)result.get("result");
					Object obj = rs.get(0).get("count");//有些数据库会自动将小写转换成大写（如DB2），所以此处分别用count/COUNT获取
					if(obj == null){
						 obj = rs.get(0).get("COUNT");
						 if(obj == null){
							 return;
						 }
					}
					if(Integer.parseInt(obj.toString()) == 0){//等于0表示是新增记录
						return ;
					}
					
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				//rm.getColumnCount() 出异常的情况
				if(columnCount < 1){
					return ;
				}
				//弹出提示窗口
				int value = JOptionPane.showConfirmDialog(MainFrame.getInstance(), 
						"单元格["+(row+1)+"行"+(column)+"列]值发生变化:\n原值："//行列起始值是0，故加1
						+jtable.oldValue
						+"\n新值："
						+jtable.newValue+"\n是否保存？","确认对话框", JOptionPane.YES_NO_OPTION);		
	    		if(value == JOptionPane.YES_OPTION){
	    			
	    			//当前列名称
	    			String columnName = myTableModel.getColumnName(column);
					if(primaryKeyList.contains(columnName)){
						JOptionPane.showMessageDialog(MainFrame.getInstance(),"当前列是主键列，值不允许更改！");
						return;
					}
					//update #tableName# set #columnName#=#newValue# where #pk1#=#pkValue1# and ...
	    			StringBuilder updateSql = new StringBuilder();
	    			updateSql.append("update ").append(tableName).append(" set ").append(columnName).append("=");
	    			

					try {
						for(int i=1;i<=columnCount;i++){
							String columnName_temp = rm.getColumnName(i);
							if(columnName_temp.equalsIgnoreCase(columnName)){
								int columnType = rm.getColumnType(i);
								Object newValue = jtable.newValue;
								if(newValue == null || "(null)".equals(newValue)){
									updateSql.append("null");
								}else{
									if(DBUtil.isChar(columnType)){
										updateSql.append("'");
										newValue = newValue.toString().replace("'", "''");
										updateSql.append(newValue);
										updateSql.append("'");
									}else{
										updateSql.append(newValue);
									}
								}
								break;
							}
						}
						updateSql.append(" where ");
						for(int i=1;i<=columnCount;i++){
							String columnName_temp = rm.getColumnName(i);
							if(primaryKeyList.contains(columnName_temp)){
								updateSql.append(columnName_temp);
								updateSql.append("=");
								int columnType = rm.getColumnType(i);
								if(DBUtil.isChar(columnType)){
									updateSql.append("'");
									updateSql.append(myTableModel.getValueAt(row, i));
									updateSql.append("'");
								}else{
									updateSql.append(myTableModel.getValueAt(row, i));
								}
								updateSql.append(" and ");
							}
						}
						updateSql.delete(updateSql.length()-5, updateSql.length()-1);
						
						int opt = JOptionPane.showConfirmDialog(MainFrame.getInstance(), "即将执行sql：\n" +updateSql+"\n确定执行？","确认对话框", JOptionPane.YES_NO_OPTION);		
			    		if(opt == JOptionPane.YES_OPTION){
			    			
			    			//执行sql前的事务处理：设置事务是否自动提交
			             	boolean isAutoCommit = isEnableAutoCommit();
			             	boolean isError = false;
			             	try {
			             		ConnUtil.getInstance().getConn().setAutoCommit(isAutoCommit);
			             	} catch (SQLException ee) {
			             		if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
			             			log.error(null, ee);
			             		}
			             		isError = true;
			             	}
			             	if(isError){
			             		String mess = null;
			             		if(isAutoCommit){
			             			mess = "设置事务自动提交失败，是否以事务非自动提交方式运行？";
			             		}else{
			             			mess = "设置事务非自动提交失败，是否以事务自动提交方式运行？";
			             		}
			             		int isDo = JOptionPane.showConfirmDialog(getInstance(), mess,"确认对话框", JOptionPane.YES_NO_OPTION);		
			             		if(isDo == JOptionPane.NO_OPTION){
			             			return;
			             		}
			             	}
			             	//执行sql
			    			Map returnMap = DBUtil.executeUpdate(updateSql.toString());
			    		  	
			    			//执行sql后的事务处理，如果是非自动提交方式，则回滚事务。
			          		if(!isAutoCommit){
			          			try {
			          				ConnUtil.getInstance().getConn().rollback();
			          			} catch (SQLException ee) {
			          				JOptionPane.showMessageDialog(getInstance(), "事务非自动提交方式下，回滚事务失败！"+ee.getMessage());
			          			}
			          		}
			    			if(returnMap.get("msg") != null){
			    				JOptionPane.showMessageDialog(MainFrame.getInstance(), "更新失败！"+
			    						(isAutoCommit?"事务已提交！":"事务已回滚！")+"\n"+returnMap.get("msg"));
			    			}else{
			    				JOptionPane.showMessageDialog(MainFrame.getInstance(), "更新成功！"+
			    						(isAutoCommit?"事务已提交！":"事务已回滚！")+"影响记录行数  "+returnMap.get("result"));
			    			}
			    		}
					} catch (SQLException e1) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error("更新单元格value值出错！", e1);
						}
					}
	    		}
			}
		});
	}
	
	/*
	 * 如果有back_sql，则默认读取并显示。
	 */
	public void refreshSQL(){
        try {
        	File file = new File(Const.BACK_SQL_PATH);
        	if(file.exists()){  		
        		ObjectInputStream oips = new ObjectInputStream(new FileInputStream(Const.BACK_SQL_PATH));
        		String back_sql = oips.readObject().toString();
        		jTextArea1.setText(back_sql);
        		oips.close();
        	}
		} catch (Exception e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(null, e);
			}
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
	/*
	 * 刷新窗体标题
	 */
	public void refreshTitle() {	
		getInstance().setTitle("当前连接："+info.getDataSourceName());	
	}
	/*
	 * 刷新导航树
	 */
	public void refreshJtree(DefaultMutableTreeNode treeNode){

		String dbType = DBUtil.getDBProductInfo().getProductName();
		
		//如果不指定节点，或者指定的节点是根节点，则重建整棵树。	
		if(treeNode == null || treeNode.isRoot()){	
			if(jTree1!=null){	
				jTree1.removeAll();//卸载原有组件
			}
	        //定义初始节点
	    	DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(info.getDataSourceName());
	    	DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode("表");
	    	DefaultMutableTreeNode viewNode = new DefaultMutableTreeNode("视图");
	    	
	    	if(dbType.contains("MYSQL")){
				String table_schema = null;
				try {
					table_schema = ConnUtil.getInstance().getConn().getCatalog();//仅适用于mysql数据库
				} catch (SQLException e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null,e);
					}
				}
				StringBuilder querysql = new StringBuilder();
				querysql.append("select TABLE_NAME,TABLE_TYPE from INFORMATION_SCHEMA.TABLES");
				if(table_schema != null){
					querysql.append(" where TABLE_SCHEMA='"+table_schema+"'");
				}
				List<Map<String, Object>> list = (List<Map<String, Object>>)(DBUtil.executeQuery(querysql.toString()).get("result"));
				if(list != null && list.size()>0){
					for (Map<String, Object> map : list) {
						if((map.get("TABLE_TYPE")+"").contains("TABLE")){
							tableNode.add(new DefaultMutableTreeNode(map.get("TABLE_NAME")));
						}else if((map.get("TABLE_TYPE")+"").contains("VIEW")){
							viewNode.add(new DefaultMutableTreeNode(map.get("TABLE_NAME")));
						}
					}
				}	
			}else if(dbType.contains("ORACLE")){
				String sql = "select TABLE_NAME from sys.user_tables order by TABLE_NAME asc";
				List<Map<String, Object>> list = (List<Map<String, Object>>)(DBUtil.executeQuery(sql).get("result"));
				if(list != null && list.size()>0){
					for (Map<String, Object> map : list) {
						tableNode.add(new DefaultMutableTreeNode(map.get("TABLE_NAME")));
					}
				}
				sql = "select VIEW_NAME from sys.user_views order by VIEW_NAME asc";
				list = (List<Map<String, Object>>)(DBUtil.executeQuery(sql).get("result"));
				if(list != null && list.size()>0){
					for (Map<String, Object> map : list) {
						viewNode.add(new DefaultMutableTreeNode(map.get("VIEW_NAME")));
					}
				}
			}else if(dbType.contains("DB2")){	
				String querysql = "select TABNAME,TYPE from syscat.tables where OWNER='"+info.getUsername().toUpperCase()+"'  and (TYPE='V' or TYPE='T') order by TABNAME asc";	
				List<Map<String, Object>> list = (List<Map<String, Object>>)(DBUtil.executeQuery(querysql.toString()).get("result"));
				if(list != null && list.size()>0){
					for (Map<String, Object> map : list) {
						if((map.get("TYPE")+"").equals(("T"))){
							tableNode.add(new DefaultMutableTreeNode(map.get("TABNAME")));
						}else if((map.get("TYPE")+"").equals("V")){
							viewNode.add(new DefaultMutableTreeNode(map.get("TABNAME")));
						}
					}
				}	
			}else{//除却以上数据库的场景
				ResultSet rs_tables = null;
		    	ResultSet rs_views = null;
		    	try {
		    		//解析表
					DatabaseMetaData dbmeta = ConnUtil.getInstance().getConn().getMetaData();
					String[] types_table = new String[]{ "TABLE" };;
					rs_tables = dbmeta.getTables(null, null, "%", types_table);
		            while (rs_tables.next())    
		            {   
		                String tableName = rs_tables.getString("TABLE_NAME");    
		                tableNode.add(new DefaultMutableTreeNode(tableName));
		            }    
		            //解析视图
		            String []types_view = {"VIEW"};
		            rs_views = dbmeta.getTables(null, null, "%", types_view);  
		            while (rs_views.next())    
		            {    
		                String viewName = rs_views.getString("TABLE_NAME");    
		                viewNode.add(new DefaultMutableTreeNode(viewName));
		            }    	
				} catch (SQLException e1) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e1);
					}
				}finally{
					if(rs_tables!=null){
						try {
							rs_tables.close();
						} catch (SQLException e1) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
								log.error(null, e1);
							}
							}
					}
					if(rs_views!=null){
						try {
							rs_views.close();
						} catch (SQLException e1) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e1);
						}}
					}
				}	
			}
	    	rootNode.add(tableNode);
	    	rootNode.add(viewNode);
	    	jTree1 = new JTree(rootNode);
			
	    	//如果是苹果风格、windows风格，则设置Jtree节点图标、字体
	        String skin = ConfigUtil.getConfInfo().get(Const.SKIN).toString();
	        if(skin.startsWith("苹果风格")||skin.equals("Windows风格")){       	    	
	        	DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer();
	        	cellRenderer.setLeafIcon(ImageIcons.leaf_gif); //叶子节点图标
	        	cellRenderer.setOpenIcon(ImageIcons.folderopen_gif); //展开时的图标
	        	cellRenderer.setClosedIcon(ImageIcons.folder_gif); //关闭时的图标
	        	cellRenderer.setTextSelectionColor(Color.white);//节点选中时，字体的颜色
	        	
	        	//节点选中时，节点的背景色
	        	if(skin.equals("Windows风格")){
		        	cellRenderer.setBackgroundSelectionColor(Color.blue);
	        	}else{        		
	        		cellRenderer.setBackgroundSelectionColor(cellRenderer.getBorderSelectionColor());
	        	}
	        	jTree1.setCellRenderer(cellRenderer);
	        }
	        //设置Tree字体
	        refreshJTreeFont();
	    	
            
            //设置是否显示根节点的展开、折叠图标,默认是false。
	    	jTree1.setShowsRootHandles(true);
	    	jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);//设置只能单选
	    	jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
	            public void mouseClicked(java.awt.event.MouseEvent evt) {
	            	
	            	if(evt.getClickCount()==2){	//鼠标双击执行查询操作
	            		
	               		//获取选中节点
	    				DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
	    				
	    				//更新当前选中节点的value值
	    				if(selectNode != null){
	    					currentSelectedNodeValue = selectNode.getUserObject();
	    					
	    					//如果是叶子节点
	        				if(selectNode.isLeaf()){
	        					
	        					//获取该选中节点的父节点
	            				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectNode.getParent();
	            				if("表".equals(parentNode.getUserObject()+"") || "视图".equals(parentNode.getUserObject()+"")){   	

	            					String sql = DBUtil.getSimpleQueryLimitSql(currentSelectedNodeValue.toString());
	            					doAction(sql, false, 0, null);	
	            					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL)+"")){			
	            						int length = jTextArea1.getText().length();
	            						jTextArea1.append("\n" + sql + ";");
	            						jTextArea1.setSelectionStart(length+1);
	            						jTextArea1.setSelectionEnd(length+1+sql.length()+1);
	            					}	
	            				}	
	        				}
	    				}	
	            	}else if(evt.getButton()==MouseEvent.BUTTON3){	//鼠标右击弹出右键菜单
	            		
	            		//右击时，设置所右击的节点为选中状态。
	            		TreePath tp = jTree1.getPathForLocation(evt.getX(), evt.getY());
	            		jTree1.setSelectionPath(tp);		
	            		
	            		//获取选中节点
	    				DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
	    				
	    				//更新当前选中节点
	    				currentSelectedNodeValue = selectNode.getUserObject();
	    				
	    				if(selectNode != null){
	    					
	    					//如果是叶子节点
	        				if(selectNode.isLeaf()){
	        					
	        					//获取该选中节点的父节点
	            				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)selectNode.getParent();
	            				if("表".equals(parentNode.getUserObject()+"")){//如果父节点是table    					
	            					jPopupMenu_table_node.show(evt.getComponent(), evt.getX(), evt.getY());		
	            				}else if("视图".equals(parentNode.getUserObject()+"")){//如果父节点是view
	            					jPopupMenu_view_node.show(evt.getComponent(), evt.getX(), evt.getY());
	            				}else{
	            					//数据库中不存在表或者视图的场景，不弹出右键菜单。
	            				}			
	        				//如果是非叶子节点
	        				}else{
	        					jPopupMenu_noneLeaf_node.show(evt.getComponent(), evt.getX(), evt.getY());			
	        				}		
	    				}	
					}else if(evt.getButton()==MouseEvent.BUTTON1){//左键
						
						//获取选中节点
	    				DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
	    				//更新当前选中节点
	    				currentSelectedNodeValue = selectNode.getUserObject();
					}
	            }
	        });
	    	
	    	//为树添加键盘事件  使用 KeyAdaptor
	    	jTree1.addKeyListener(new KeyAdapter(){
	    		public void keyPressed(KeyEvent evt) {
	    			//F5 刷新当前节点
	                if (evt.getKeyCode() == KeyEvent.VK_F5) {  
	                	//取得当前选中节点
	    				DefaultMutableTreeNode selectNode = (DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent();
	    				refreshJtree(selectNode);
	                }   
				}
	    	});
	    	
	        
	        //设置树背景色
	        jTree1.setBackground(((MyColor)ConfigUtil.getConfInfo().get(Const.EYE_SAFETY_COLOR)).getColor());
	        DefaultTreeCellRenderer c = (DefaultTreeCellRenderer)jTree1.getCellRenderer();
	        c.setBackgroundNonSelectionColor(((MyColor)ConfigUtil.getConfInfo().get(Const.EYE_SAFETY_COLOR)).getColor());  
	        JScrollPane jScrollPane_jtree = new JScrollPane(jTree1);
	        jScrollPane_jtree.setMinimumSize(miniSize); //如果不设置最小size为0，会造成jSplitPane_left_right拖到一定大小后就不能拖动了。
	        jSplitPane_left_right.setLeftComponent(jScrollPane_jtree);
	        jSplitPane_left_right.setDividerLocation(jSplitPane_left_right.getDividerLocation());//刷新Jtree时，保持分隔面板左侧宽度不变。
		
		}else{
			
			//部分更新
			treeNode.removeAllChildren();//首先清空原有孩子节点	
			String nodeValue = treeNode.getUserObject().toString();//获取当前选中节点的value值
			
			if(dbType.contains("MYSQL")){
				String table_schema = null;
				try {
					table_schema = ConnUtil.getInstance().getConn().getCatalog();//仅适用于mysql数据库
				} catch (SQLException e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null,e);
					}
				}
				StringBuilder querysql = new StringBuilder();
				querysql.append("select TABLE_NAME,TABLE_TYPE from INFORMATION_SCHEMA.TABLES");
				if(table_schema != null){
					querysql.append(" where TABLE_SCHEMA='"+table_schema+"'");
				}
				if(nodeValue.equals("表")){
					querysql.append(" and TABLE_TYPE like '%TABLE%'");
				}else if(nodeValue.equals("视图")){
					querysql.append(" and TABLE_TYPE like '%VIEW%'");
				}else{
					JOptionPane.showMessageDialog(null, "未知的节点类型！"+currentSelectedNodeValue);
					return;
				}
				List<Map<String, Object>> list = (List<Map<String, Object>>)(DBUtil.executeQuery(querysql.toString()).get("result"));
				if(list != null && list.size()>0){
					for (Map<String, Object> map : list) {
						treeNode.add(new DefaultMutableTreeNode(map.get("TABLE_NAME")));
					}
				}	
			}else if(dbType.contains("ORACLE")){
				String sql = null;
				if(nodeValue.equals("表")){
					 sql = "select TABLE_NAME as OBJECT_NAME from sys.user_tables order by TABLE_NAME asc";
				}else if(nodeValue.equals("视图")){
					sql = "select VIEW_NAME AS OBJECT_NAME from sys.user_views order by VIEW_NAME asc";
				}else{
					JOptionPane.showMessageDialog(null, "未知的节点类型！"+currentSelectedNodeValue);
					return;
				}
				List<Map<String, Object>> list = (List<Map<String, Object>>)(DBUtil.executeQuery(sql).get("result"));
				if(list != null && list.size()>0){
					for (Map<String, Object> map : list) {
						treeNode.add(new DefaultMutableTreeNode(map.get("OBJECT_NAME")));
					}
				}	
			}else if(dbType.contains("DB2")){	
				String sql = null;
				if(nodeValue.equals("表")){
					 sql = "select TABNAME,TYPE from syscat.tables where OWNER='"+info.getUsername().toUpperCase()+"'  and TYPE='T' order by TABNAME asc";
				}else if(nodeValue.equals("视图")){
					 sql = "select TABNAME,TYPE from syscat.tables where OWNER='"+info.getUsername().toUpperCase()+"'  and TYPE='V' order by TABNAME asc";
				}else{
					JOptionPane.showMessageDialog(null, "未知的节点类型！"+currentSelectedNodeValue);
					return;
				}
				List<Map<String, Object>> list = (List<Map<String, Object>>)(DBUtil.executeQuery(sql).get("result"));
				if(list != null && list.size()>0){
					for (Map<String, Object> map : list) {
						treeNode.add(new DefaultMutableTreeNode(map.get("TABNAME")));
					}
				}	
			}else{//除却以上数据库的场景
				DatabaseMetaData dbmeta = null;
				ResultSet rs_tables = null;
				ResultSet rs_views = null;
				try {
					dbmeta = ConnUtil.getInstance().getConn().getMetaData();
					if(nodeValue.equals("表")){
						String[] types_table =  { "TABLE" };    
						rs_tables = dbmeta.getTables(null, null, "%", types_table);    
			            while (rs_tables.next())    
			            {    
			                String tableName = rs_tables.getString("TABLE_NAME");    
			                treeNode.add(new DefaultMutableTreeNode(tableName));
			            }  
					}else if(nodeValue.equals("视图")){
			            String []types_view = {"VIEW"};
			            rs_views = dbmeta.getTables(null, null, "%", types_view);   
			            while (rs_views.next())    
			            {    
			                String viewName = rs_views.getString("TABLE_NAME");    
			                treeNode.add(new DefaultMutableTreeNode(viewName));
			            }    	
					}else{
						JOptionPane.showMessageDialog(null, "未知的节点类型！"+currentSelectedNodeValue);
						return;
					}
				} catch (SQLException e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
				}finally{
					if(rs_tables!=null){
						try {
							rs_tables.close();
						} catch (SQLException e1) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e1);
						}}
					}
					if(rs_views!=null){
						try {
							rs_views.close();
						} catch (SQLException e1) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e1);
						}}
					}
				}
			}
			//根据入参treeNode，构建treepath
			TreePath selectPath = new TreePath(treeNode.getPath());
			
			//需要手工reload，否则界面上仍然显示刷新之前的数据。
			DefaultTreeModel model = (DefaultTreeModel)jTree1.getModel();
		
			model.reload();
			//jTree1.updateUI();//该行也可以实现刷新界面数据的功能，但是会还原一些样式（如背景色等），故此处不采用。	
		
			//部分刷新时，展开刷新后的节点
			expandNode(jTree1, selectPath, true);
		}	
	}
	/*
	 * 展开、合拢指定节点下的所有节点
	 */
	public void expandNode(JTree tree, TreePath parent, boolean expand) {
       
		//getLastPathComponent：返回此路径的最后一个组件。对于 DefaultTreeModel 返回的路径，它将返回一个 TreeNode 实例
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() > 0) {
            for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
                TreeNode childNode = (TreeNode) e.nextElement();
                
                //pathByAddingChild：返回包含此对象的所有元素加上 child 的新路径。child 将是新创建的 TreePath 的最后一个元素。
                TreePath path = parent.pathByAddingChild(childNode);
                expandNode(tree, path, expand);
            }
        }    	
        if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
    }
	/*
	 * 获得DBunit的连接
	 */
	public IDatabaseConnection getDBUnitConnection(){
		if(dbUnitconnection==null){
			try {
				dbUnitconnection = new DatabaseConnection(ConnUtil.getInstance().getConn(),info.getUsername().toUpperCase());
			} catch (Exception e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
				JOptionPane.showMessageDialog(getInstance(), e.getMessage());
			}
		}
		return dbUnitconnection;
	}
	
	/*
	 * 保存sql编辑器中的sql语句
	 */
	public void saveSql(){
		ObjectOutputStream oops = null;
		try {
			oops = new ObjectOutputStream(new FileOutputStream(Const.BACK_SQL_PATH));
			oops.writeObject(jTextArea1.getText());
			oops.flush();
		} catch (IOException e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(null, e);
			}
		}finally{
			if(oops!=null){		
				try {
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
	 * ctrl+正斜杠键“/” 执行增加注释，或取消注释操作
	 */
	private void addDeleteZhuShi(){
		
		//1. 获得选中文本
		String selection = jTextArea1.getSelectedText();

		//2. 没有选中文本的场景
		if (selection == null) {
			
			//获得当前光标位置
			int cur = jTextArea1.getCaretPosition();
			
			//2.1 如果光标在文本域的起始位置
			if(cur == 0){
				//判断当前光标所在行是否以“--”开头，如果以注释符开头，则删除注释符，如果不以注释符开头，则插入注释符。
				String text = jTextArea1.getText();
				if(text.startsWith("-- ")){
					jTextArea1.replaceRange("", 0, 3);
				}else{								
					jTextArea1.insert("-- ", 0);
				}
			//2.2  如果光标不在起始位置
			}else{
				// 获得光标之前所有文本信息
				String preText = null;
				try {
					preText = jTextArea1.getText(0, cur);
				} catch (BadLocationException e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
					return;
				}
				//2.2.1  如果光标之前包含有换行符
				if (preText.contains("\n")) {
					
					//获得光标所在行的前2个起始字符
					String pre3char = null;
					try {
						pre3char = jTextArea1.getText(preText.lastIndexOf("\n") + 1, 3);
					} catch (BadLocationException e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e);
						}
						return;
					}
					//如果光标所在行以注释符开头
					if("-- ".equals(pre3char)){
						jTextArea1.replaceRange("", preText.lastIndexOf("\n") + 1, preText.lastIndexOf("\n") + 1 + 3);
					
						//否则，在该位置之后的第一行之前插入注释符。
					}else{	
						jTextArea1.insert("-- ", preText.lastIndexOf("\n") + 1);
					}
				//2.2.2  如果选中文本之前不存在换行符的场景，即当前光标在第一行的场景
				} else {
					String text = jTextArea1.getText();
					
					//删除文本域起始注释符
					if(text.startsWith("-- ")){
						jTextArea1.replaceRange("", 0, 3);
					}else{					
						//直接在文本域起始位置插入注释符
						jTextArea1.insert("-- ", 0);
					}
				}
			}
		}	
		//3. 有选中文本的场景
		else {
			// 对选中文本按行“\n”分割
			String[] lines = selection.split("\n");
			
			//3.1  选中文本不包含换行符的场景，即只选择一行的场景
			if(lines.length == 1){
				
				int selectionStart = jTextArea1.getSelectionStart();

				// 获得光标之前最后一个 \n 的位置,在该位置之后的第一行之前插入注释符。
				String preText = null;
				if(selectionStart == 0){//文本域首字符被选中的场景
					preText = "";
				}else{			
					try {
						preText = jTextArea1.getText(0, selectionStart);//获得选中文本之前的数据
					} catch (BadLocationException e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e);
						}
						return;
					}
				}
				//3.1.1 选中文本前的文本包含换行符的场景，即所选文本不是第一行的场景。
				if (preText.contains("\n")) {
					
					//获得当前行的前2个起始字符
					String pre3char = null;
					try {
						pre3char = jTextArea1.getText(preText.lastIndexOf("\n") + 1, 3);
					} catch (BadLocationException e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e);
						}
					}
					//如果光标所在行以注释符开头
					if("-- ".equals(pre3char)){
						jTextArea1.replaceRange("", preText.lastIndexOf("\n") + 1, preText.lastIndexOf("\n") + 1 + 3);
					}else{					
						jTextArea1.insert("-- ", preText.lastIndexOf("\n") + 1);
					}
				//3.1.2  如果选中文本之前不存在换行符的场景，即当前是第一行的场景
				} else {
					String text = jTextArea1.getText();
					
					//删除文本域起始注释符
					if(text.startsWith("-- ")){
						jTextArea1.replaceRange("", 0, 3);
					}else{	
						//文本域起始位置插入注释符
						jTextArea1.insert("-- ", 0);
					}
				}
				
			//3.2   选中文本包含换行符的场景
			}else{
				//1、首先处理选中文本的第一行，如果第一行以注释符开头，则都执行删除注释操作，否则，都执行添加注释操作。
				boolean isStartsWithZhuShi = false;
				int selectionStart = jTextArea1.getSelectionStart();
				int selectionEnd = jTextArea1.getSelectionEnd();
				
				// 获得光标之前最后一个 \n 的位置,在该位置之后的第一行之前插入注释符。
				String preText = null;
				if(selectionStart == 0){//文本域首字符被选中的场景
					preText = "";
				}else{			
					try {
						preText = jTextArea1.getText(0, selectionStart);
					} catch (BadLocationException e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e);
						}
						return;
					}
				}
				if (preText.contains("\n")) {
					
					//获得选中文本第一行的前3个起始字符
					String pre3char = null;
					try {
						pre3char = jTextArea1.getText(preText.lastIndexOf("\n") + 1, 3);
					} catch (BadLocationException e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e);
						}
						return;
					}
					//如果光标所在行以注释符开头
					if("-- ".equals(pre3char)){
						isStartsWithZhuShi = true;
					}
					if(isStartsWithZhuShi){//处理第一行，删除注释操作
						
						jTextArea1.replaceRange("", preText.lastIndexOf("\n") + 1, preText.lastIndexOf("\n") + 1 + 3);
						
					}else{//处理第一行，添加注释操作
						jTextArea1.insert("-- ", preText.lastIndexOf("\n") + 1);
					}
					
					// 如果选中文本之前不存在换行符的场景，即当前是第一行的场景
				}else{
					String text = jTextArea1.getText();
					
					//删除文本域起始注释符
					if(text.startsWith("-- ")){
						isStartsWithZhuShi = true;
					}
					if(isStartsWithZhuShi){//处理选中文本第一行，删除注释操作
						
						jTextArea1.replaceRange("", 0, 3);
						
					}else{//处理选中文本第一行，添加注释操作
						jTextArea1.insert("-- ", 0);
					}
				}					
				
				//2、然后处理其他行
				StringBuilder builder = new StringBuilder();
				if(isStartsWithZhuShi){
					if(lines[0].startsWith("-- ")){
						lines[0] = lines[0].substring(3);
					}
					builder.append(lines[0]);
					for (int i = 1; i < lines.length; i++){
						builder.append("\n");
						if(lines[i].startsWith("-- ")){
							lines[i] = lines[i].substring(3);
						}
						builder.append(lines[i]);
					}
					selectionStart = jTextArea1.getSelectionStart();
					selectionEnd = jTextArea1.getSelectionEnd();
					jTextArea1.replaceRange(builder.toString(), selectionStart, selectionEnd);
					
					//重新选中选择的文本
					selectionEnd = selectionStart + builder.length();
					jTextArea1.setSelectionStart(selectionStart);
					jTextArea1.setSelectionEnd(selectionEnd);			
				
				}else{				
					builder.append(lines[0]);
					for (int i = 1; i < lines.length; i++){
						builder.append("\n");
						builder.append("-- ").append(lines[i]);
					}
					selectionStart = jTextArea1.getSelectionStart();
					selectionEnd = jTextArea1.getSelectionEnd();
					jTextArea1.replaceRange(builder.toString(), selectionStart, selectionEnd);
					
					//重新选中选择的文本
					selectionEnd = selectionStart + builder.length();
					jTextArea1.setSelectionStart(selectionStart);
					jTextArea1.setSelectionEnd(selectionEnd);			
				}
			}
			}
	}
	/**
	 * 弹出查找替换对话框
	 * @param area
	 */
	private void showFindDialog(MyJextArea area){
		area.showFindDialog(getInstance());
	}
	/**
	 * 弹出定位行对话框
	 * @param area
	 */
	private void showLocationLineDialog(MyJextArea area){
		area.showLocationLineDialog(getInstance());
	}
	/**
	 * 如果当前选项卡是对单表的select * from tableName 查询，则返回当前表名称，否则返回null
	 * @return String
	 */
	public String getCurrentTableName(){
		
		int selectIndex = jTabbedPane1.getSelectedIndex();
		if(selectIndex == -1){
			return null;
		}
		String currentTabTitle = jTabbedPane1.getTitleAt(selectIndex);
		String sql = sqlMap.get(currentTabTitle);//导入sql操作时，此处sql是null
		if(sql == null){
			return null;
		}
		sql = sql.replace(" ", "").toLowerCase();
		if(!sql.startsWith("select*from")){
			return null;
		}
		int whereIndex = sql.indexOf("where");
		int limitIndex = sql.indexOf("limit");
		String tableName = null;
		if(whereIndex != -1){
			tableName = sql.substring("select*from".length(), whereIndex);
		}else{
			if(limitIndex != -1){
				tableName = sql.substring("select*from".length(), limitIndex);
			}else{
				int orderbyIndex = sql.indexOf("orderby");
				if(orderbyIndex != -1){
					tableName = sql.substring("select*from".length(), orderbyIndex);
				}else{
					tableName = sql.substring("select*from".length());
				}
			}
		}
		if(tableName.indexOf(",") > -1){
			return null;
		}
		return tableName;
	}
	/**
	 * 启用 或 禁用 sql编辑器的智能提示
	 */
	private void enable_or_disableTip(){
		if(isEnableTip()){
			jButton_tips.setIcon(ImageIcons.unselect_png24);
			ConfigUtil.getConfInfo().put(Const.IS_ENABLE_SMART_TIPS, false);
			//启用输入法支持
			jTextArea1.enableInputMethods(true);
		}else{
			jButton_tips.setIcon(ImageIcons.select_png24);
			ConfigUtil.getConfInfo().put(Const.IS_ENABLE_SMART_TIPS, true);
			//由于开启自动补全时，中文输入时会有问题，所以禁用输入法支持
			jTextArea1.enableInputMethods(false);
		}	
		ConfigUtil.updateConfInfo();
	}
	/**
	 * 智能提示是否处于启用状态
	 * @return boolean
	 */
	private boolean isEnableTip(){
		Icon icon = jButton_tips.getIcon();
		return icon.equals(ImageIcons.select_png24);
	}
	/**
	 * 事务自动提交是否处于选中状态
	 * @return boolean
	 */
	private boolean isEnableAutoCommit(){
		Icon icon = jButton_autoCommit.getIcon();
		return icon.equals(ImageIcons.select_png24);
	}
	/**
	 * 启用 或 禁用 事务自动提交
	 */
	private void enable_or_disableAutoCommit(){
		if(isEnableAutoCommit()){
			jButton_autoCommit.setIcon(ImageIcons.unselect_png24);
			ConfigUtil.getConfInfo().put(Const.IS_AUTO_COMMIT, false);
		}else{
			jButton_autoCommit.setIcon(ImageIcons.select_png24);
			ConfigUtil.getConfInfo().put(Const.IS_AUTO_COMMIT, true);
		}	
		ConfigUtil.updateConfInfo();
	}
	/**
	 * 刷新Jtree字体
	 * jTree1.setFont/cellRender.setFont同时使用才有效
	 */
	private void refreshJTreeFont(){
		Font font = (Font)ConfigUtil.getConfInfo().get(Const.JTREE_FONT);
		jTree1.setFont(font);
		TreeCellRenderer cellRender = jTree1.getCellRenderer();
		if(cellRender instanceof DefaultTreeCellRenderer){
			DefaultTreeCellRenderer dCellRender = (DefaultTreeCellRenderer)jTree1.getCellRenderer();
			dCellRender.setFont(font); 
		}else if(cellRender instanceof SubstanceDefaultTreeCellRenderer){
			SubstanceDefaultTreeCellRenderer sCellRender = (SubstanceDefaultTreeCellRenderer)jTree1.getCellRenderer();
			sCellRender.setFont(font);
		}else{
			log.error("未知的cellRender："+cellRender.toString());
		}
	}
	/**
	 * 重置缓存信息
	 * @param isClearTimeInfo
	 */
	private void clear(boolean isClearTimeInfo){
		sqlMap.clear();
		result.clear();
    	show_componts.clear();
    	jTabbedPane1.removeAll();
    	jtableSorterMap.clear();
    	if(isClearTimeInfo){
    		jLabel_time.setText(null);
    		jLabel_time.setIcon(null);
    	}
	}
}