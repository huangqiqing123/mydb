package test.tool.gui.dbtool.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import test.tool.gui.common.MyUUID;
import test.tool.gui.dbtool.beans.DataSourceInfo;
import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.frame.MainFrame;
import test.tool.gui.dbtool.frame.MyNotePad;
import test.tool.gui.dbtool.image.ImageIcons;
import test.tool.gui.dbtool.mycomponent.MyJTextField;
import test.tool.gui.dbtool.mycomponent.MyJextArea;
import test.tool.gui.dbtool.util.ConfigUtil;
import test.tool.gui.dbtool.util.ConnUtil;
import test.tool.gui.dbtool.util.DBDriverUtil;
import test.tool.gui.dbtool.util.DataSourceMapUtil;
import test.tool.gui.dbtool.util.ExceptionUtil;
import test.tool.gui.dbtool.util.JarLoadUtil;
import test.tool.gui.dbtool.util.LocationUtil;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

/*
 * 依赖包：
 *	dbunit-2.4.8.jar、swing-layout-1.0.3.jar、poi-3.2-FINAL-20081019.jar、
 *	slf4j-api-1.6.1.jar/commons-logging.jar
 */
public class SetValue extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(SetValue.class);
    private javax.swing.JComboBox jComboBox1_dbtype;
    private javax.swing.JLabel jLabel_dbtype;
    private javax.swing.JLabel jLabel_driver;
    private javax.swing.JLabel jLabel_pass;
    private javax.swing.JLabel jLabel_url;
    private javax.swing.JLabel jLabel_username;
    private javax.swing.JLabel jLabel_tips;
    private javax.swing.JList jList1_help;
    private javax.swing.JPasswordField jPasswordField_password;
    private MyJextArea jTextArea_url;
    private MyJTextField jTextField_driverClass;
	private MyJTextField jTextField_username;
	private JButton ok;
	private JButton cancel;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
    private JPopupMenu jPopupMenu_tool = new JPopupMenu();//Jlist中右键菜单
    private JMenuItem jMenuItemReName = new JMenuItem("重命名");
    private JMenuItem jMenuItemConnect = new JMenuItem("连接");
    private JMenuItem jMenuItem_delete = new JMenuItem("删除");
    private JMenuItem jMenuItemShowPassword = new JMenuItem("查看密码");

    //用于记录连接失败时的详细错误信息
    private String errorInfo = null;
	/*
	 * 获取SetValue窗体的一个实例（单例模式）。
	 */
	private static SetValue instance = null;
	public static SetValue getInstance(){
		if(instance==null){
			instance = new SetValue();
			
			// 使窗口居中显示
			int location[] = LocationUtil.getCenterLocation(instance);
			instance.setLocation(location[0], location[1]);
			instance.setResizable(false);

			// 设置标题栏图标
			instance.setIconImage(ImageIcons.ico_png.getImage());
		}
		return instance;
	}
	/*
	 * 构造函数
	 */
	public SetValue() {
		initComponents();
		
		//为窗口添加监听事件
    	this.addWindowListener(new WindowAdapter(){

    		//windowClosing事件：当用户点击窗口右上角的关闭按钮时触发。
			public void windowClosing(WindowEvent arg0) {
				ConnUtil.getInstance().closeConnection();
			}});
	}
	/*
	 * 初始化窗体组件
	 */
	private void initComponents() {
		
		ok = new JButton("确定",ImageIcons.ok_png);
		cancel = new JButton("关闭",ImageIcons.exit_png);	
		jLabel_dbtype = new javax.swing.JLabel("数据库类型：");
        jLabel_driver = new javax.swing.JLabel("  驱动类：");
        jLabel_url = new javax.swing.JLabel(" 连接 URL：");
        jLabel_username = new javax.swing.JLabel("   用户名：");
        jLabel_pass = new javax.swing.JLabel("    密 码：");
        jComboBox1_dbtype = new javax.swing.JComboBox();
        jTextField_driverClass = new MyJTextField();
        jTextField_username = new MyJTextField();
        jPasswordField_password = new javax.swing.JPasswordField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1_help = new javax.swing.JList();
        jLabel_tips = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_url = new MyJextArea(false);
        jTextArea_url.setLineWrap(true);//自动换行

	    jScrollPane1.setViewportView(jList1_help);
        jScrollPane2.setViewportView(jTextArea_url);
	    jList1_help.setDragEnabled(true);//设置可拖动   
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("通用数据库查询分析器");//窗口标题

		//提示信息标签，右键菜单
		jLabel_tips.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == 3){
					if(errorInfo != null && !"".equals(errorInfo)){
						JPopupMenu jPopupMenu = new JPopupMenu();
					    JMenuItem jMenuItem_detail = new JMenuItem("查看错误详情");
					    jMenuItem_detail.setIcon(ImageIcons.detail_gif);
					    jMenuItem_detail.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e) {
								MyNotePad notePad = new MyNotePad(getInstance(),"查看错误详情",errorInfo,null);
								notePad.setVisible(true);
							} 	
					    });
					    jPopupMenu.add(jMenuItem_detail);
					    jPopupMenu.show(jLabel_tips, e.getX(), e.getY());
					}
					if("密码".equals(jLabel_tips.getToolTipText())){
						JPopupMenu jPopupMenu = new JPopupMenu();
					    JMenuItem jMenuItem_copy = new JMenuItem("复制");
					    jMenuItem_copy.setIcon(ImageIcons.copy_gif);
					    jMenuItem_copy.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e) {
								Transferable contents = new StringSelection(jLabel_tips.getText().replace("<html>","").replace("</html>", ""));
					 			Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
					 			clip.setContents(contents, null);
							} 	
					    });
					    jPopupMenu.add(jMenuItem_copy);
					    JMenuItem jMenuItem_hiddle = new JMenuItem("隐藏");
					    jMenuItem_hiddle.setIcon(ImageIcons.disable_png_16);
					    jMenuItem_hiddle.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e) {
								jLabel_tips.setToolTipText("");
								jLabel_tips.setText(null);
								jLabel_tips.setIcon(null);
								errorInfo = null;
							} 	
					    });
					    jPopupMenu.add(jMenuItem_hiddle);
					    jPopupMenu.show(jLabel_tips, e.getX(), e.getY());
					}
				}
			}		
		});
		
		//确定按钮事件
		ok.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				okMouseClicked();
			}
		});

		//取消按钮事件
		cancel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ConnUtil.getInstance().closeConnection();
				getInstance().dispose();
			}
		});
		//数据库类型从配置文件properties中读取，数据库类型变动时，驱动程序、url同步跟着变化。
		Object [] dbtypes = DBDriverUtil.getDBTypes().toArray();
        jComboBox1_dbtype.setModel(new DefaultComboBoxModel(dbtypes));
        jComboBox1_dbtype.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				String dbtype = e.getItem().toString();
				jTextField_driverClass.setText(DBDriverUtil.getDriverClass(dbtype));
				jTextArea_url.setText(DBDriverUtil.getURL(dbtype));
			}   	
        });
        //如果不存在当前连接，则为驱动类、url设置默认值，默认显示dbtype_list中的第一个数据库类型对应的驱动
        if(DataSourceMapUtil.getCurrentCon() == null){      	
        	jTextField_driverClass.setText(DBDriverUtil.getDriverClass(dbtypes[0].toString()));
        	jTextArea_url.setText(DBDriverUtil.getURL(dbtypes[0].toString()));
        }
        
        jList1_help.setModel(new DefaultListModel());
        jList1_help.setAutoscrolls(true);
        jList1_help.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//设置选择方式是 单选
        
        //为Jlist中的元素设置图标
        jList1_help.setCellRenderer(new DefaultListCellRenderer(){
			private static final long serialVersionUID = 1L;
			
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if(ConfigUtil.getConfInfo().get(Const.JTABLE_FONT)!=null){	
					Font font = (Font)ConfigUtil.getConfInfo().get(Const.JTABLE_FONT);
					setFont(font);
				}
				setIcon(ImageIcons.key_gif);
				setText(value.toString());
				
				if (isSelected) {
					setBackground(list.getSelectionBackground());
					setForeground(list.getSelectionForeground());
				} else {
					// 设置选取与取消选取的前景与背景颜色.
					setBackground(list.getBackground());
					setForeground(list.getForeground());
				}
				return this;
			}
        });
        jList1_help.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                jList1_helpValueChanged(evt);
            }
        });
        //JList右键菜单《重命名、连接、删除》
        jMenuItemReName.setIcon(ImageIcons.rename_gif);
        jMenuItemConnect.setIcon(ImageIcons.ok_png);
        jMenuItem_delete.setIcon(ImageIcons.delete_png);
        jMenuItemShowPassword.setIcon(ImageIcons.find_png16);
        jPopupMenu_tool.add(jMenuItemReName);
        jPopupMenu_tool.addSeparator();
        jPopupMenu_tool.add(jMenuItemConnect);
        jPopupMenu_tool.addSeparator();
        jPopupMenu_tool.add(jMenuItem_delete);
        jPopupMenu_tool.addSeparator();
        jPopupMenu_tool.add(jMenuItemShowPassword);
        
        //连接重命名
        jMenuItemReName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	reNameConnect(evt);
            }
        });
        //连接
        jMenuItemConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	okMouseClicked();
            }
        });
        //删除连接
        jMenuItem_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {      	

            	int returnValue = JOptionPane.showConfirmDialog( null, "确认删除所选连接？" , "确认对话框", JOptionPane.YES_NO_OPTION );
            	if(returnValue==JOptionPane.YES_OPTION){
	
            		//删除所选连接
            		DataSourceInfo ds = (DataSourceInfo)jList1_help.getSelectedValue();
            		DataSourceMapUtil.delete(ds.getSn());		
            		
            		//刷新Jlist中的数据
            		refreshJlist(DataSourceMapUtil.getDataSourceTreeMap());
            		jLabel_tips.setToolTipText("删除成功");
            		jLabel_tips.setForeground(Color.BLUE);
            		jLabel_tips.setText("<html>删除成功！</html>");
            		jLabel_tips.setIcon(null);
    				
            	}  
            }
        });
        //查看密码
        jMenuItemShowPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {      	
        		DataSourceInfo ds = (DataSourceInfo)jList1_help.getSelectedValue();
        		jLabel_tips.setToolTipText("密码");
        		jLabel_tips.setForeground(Color.BLUE);
        		jLabel_tips.setText("<html>"+ds.getPwd()+"</html>");
        		jLabel_tips.setIcon(null);
        		
            }
        });
        //为数据源列表添加事件监听
        jList1_help.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent e) {
               
        		//点击则设置点击的选项置为选中状态。
        		int index = jList1_help.locationToIndex(e.getPoint());   
        		jList1_help.setSelectedIndex(index);   
        		
        	//当单击鼠标右键，并且JList中处于选中状态的记录有且仅有一条时，则弹出右键菜单。
        	if(e.getButton() == 3 && jList1_help.getSelectedValues().length==1){
        		jPopupMenu_tool.show(jList1_help,e.getX(),e.getY());
        	}
        	//如果是鼠标双击，并且当前处于选中状态的记录只有一条时，则直接连接该数据源
        	else if(e.getClickCount() == 2){
        		okMouseClicked();
        	}  	
          }
        });
        
		//设置数据库连接的默认值信息,如果数据库连接配置文件已存在，则读取连接信息，并设置为默认值。
		File file = new File(Const.DATA_SOURCE_PATH);
		if(file.exists()){	
			try {
				
				TreeMap<String, DataSourceInfo> treemap = DataSourceMapUtil.getDataSourceTreeMap();
				if(treemap!=null&&treemap.size()>0){
					
					//设置JList中需要展现的数据
					refreshJlist(treemap);			
				}	
			} catch (Exception e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
				jLabel_tips.setToolTipText("");
				jLabel_tips.setForeground(Color.RED);
				jLabel_tips.setText("<html>"+"出错了！"+e+"</html>");	
				jLabel_tips.setIcon(null);
			}
		}
		//设置与JTable单元格字体保持一致的组件
		if(ConfigUtil.getConfInfo().get(Const.JTABLE_FONT)!=null){	
			Font font = (Font)ConfigUtil.getConfInfo().get(Const.JTABLE_FONT);
			jComboBox1_dbtype.setFont(font);
			jLabel_dbtype.setFont(font);
			jLabel_driver.setFont(font);
			jLabel_pass.setFont(font);
			jLabel_tips.setFont(font);
			jLabel_url.setFont(font);
			jLabel_username.setFont(font);
			jMenuItem_delete.setFont(font);
			jMenuItemConnect.setFont(font);
			jMenuItemReName.setFont(font);
			jPasswordField_password.setFont(font);
			jPopupMenu_tool.setFont(font);
			jTextArea_url.setFont(font);
			jTextField_driverClass.setFont(font);
			jTextField_username.setFont(font);
			
			//以下组件字体设置成微软雅黑不好看，故而注释。
//			ok.setFont(font);
//			cancel.setFont(font);
		}
		//---------------------------布局信息 begin----------------------------------------------
		 GroupLayout layout = new GroupLayout(getContentPane());
		 layout.setHorizontalGroup(
		 	layout.createParallelGroup(Alignment.LEADING)
		 		.addGroup(layout.createSequentialGroup()
		 			.addGroup(layout.createParallelGroup(Alignment.LEADING)
		 				.addGroup(layout.createSequentialGroup()
		 					.addContainerGap()
		 					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
		 						.addComponent(jLabel_driver, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
		 						.addComponent(jLabel_dbtype, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		 						.addComponent(jLabel_url, Alignment.LEADING)
		 						.addComponent(jLabel_pass, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
		 						.addComponent(jLabel_username, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
		 					.addPreferredGap(ComponentPlacement.RELATED)
		 					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
		 						.addComponent(jComboBox1_dbtype, Alignment.LEADING, 0, 292, Short.MAX_VALUE)
		 						.addComponent(jTextField_driverClass, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
		 						.addComponent(jTextField_username, GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
		 						.addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
		 						.addComponent(jPasswordField_password, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
		 					.addGap(18)
		 					.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE))
		 				.addGroup(layout.createSequentialGroup()
		 					.addGap(196)
		 					.addComponent(ok)
		 					.addPreferredGap(ComponentPlacement.RELATED)
		 					.addComponent(cancel))
		 				.addGroup(layout.createSequentialGroup()
		 					.addContainerGap()
		 					.addComponent(jLabel_tips, GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)))
		 			.addContainerGap())
		 );
		 layout.setVerticalGroup(
		 	layout.createParallelGroup(Alignment.TRAILING)
		 		.addGroup(layout.createSequentialGroup()
		 			.addGap(19)
		 			.addGroup(layout.createParallelGroup(Alignment.LEADING)
		 				.addGroup(layout.createSequentialGroup()
		 					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
		 						.addComponent(jLabel_dbtype, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
		 						.addComponent(jComboBox1_dbtype, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
		 					.addPreferredGap(ComponentPlacement.RELATED)
		 					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
		 						.addComponent(jLabel_driver, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
		 						.addComponent(jTextField_driverClass, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
		 					.addGroup(layout.createParallelGroup(Alignment.LEADING)
		 						.addGroup(layout.createSequentialGroup()
		 							.addGap(32)
		 							.addComponent(jLabel_url, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
		 						.addGroup(layout.createSequentialGroup()
		 							.addPreferredGap(ComponentPlacement.RELATED)
		 							.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)))
		 					.addPreferredGap(ComponentPlacement.RELATED)
		 					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
		 						.addComponent(jTextField_username, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
		 						.addComponent(jLabel_username, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
		 					.addPreferredGap(ComponentPlacement.RELATED)
		 					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
		 						.addComponent(jLabel_pass, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
		 						.addComponent(jPasswordField_password, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
		 					.addPreferredGap(ComponentPlacement.UNRELATED)
		 					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
		 						.addComponent(ok)
		 						.addComponent(cancel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
		 				.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE))
		 			.addPreferredGap(ComponentPlacement.RELATED)
		 			.addComponent(jLabel_tips)
		 			.addGap(28))
		 );
	        getContentPane().setLayout(layout);

	        pack();
	    }// </editor-fold>
	//---------------------------布局信息 end----------------------------------------------
	/*
	 * 刷新JList
	 */
	private  void refreshJlist(final TreeMap<String,DataSourceInfo> treeMap){
		
		String currentCon = null;
		//遍历treeMap中的所有数据库连接信息，并将其在Jlist中展现
		final DefaultListModel defaultListModel = (DefaultListModel) jList1_help.getModel();
		defaultListModel.removeAllElements();
		
		//如果存在当前连接，则将当前连接设置为选中状态。
		 Iterator<String> treeItor = treeMap.keySet().iterator();   
	        while(treeItor.hasNext())   
	        {   
	            String key = treeItor.next(); 
	            DataSourceInfo ds = treeMap.get(key);
				
	            defaultListModel.addElement(ds);
				if(ds.getSn().endsWith(Const.CURRENT)){
					currentCon = ds.getSn();
				}	
	        }   

    		jList1_help.setModel(defaultListModel);	
    		
    		//设置当前连接处于选中状态。
    		if(currentCon!=null){
    			jList1_help.setSelectedValue(treeMap.get(currentCon), true);
    		}
	}
	/*
	 * 数据源名称--重命名
	 */
	private void reNameConnect(ActionEvent evt) {
			
		DataSourceInfo ds = (DataSourceInfo)jList1_help.getSelectedValue();	
		String newDataSourceName = JOptionPane.showInputDialog(this,"请输入新的数据源名称！",ds.getDataSourceName());
		if(newDataSourceName==null||"".equals(newDataSourceName.trim())){
			return;//取消重命名操作
		}	
		while(newDataSourceName.indexOf(",")>=0||newDataSourceName.indexOf(":")>=0){
			newDataSourceName = JOptionPane.showInputDialog(this,"数据源名称中不能包含有逗号和冒号，请重新输入！");
			if(newDataSourceName==null||"".equals(newDataSourceName.trim())){
				return;//取消重命名操作
			}	
		}
		final TreeMap<String, DataSourceInfo> treemap = DataSourceMapUtil.getDataSourceTreeMap();
		treemap.remove(ds.getSn());//先删除、后插入
		
		//将重命名后的ds置为当前ds，将旧的的当前ds置为非当前ds。
		DataSourceInfo oldCurrent = DataSourceMapUtil.getCurrentCon(treemap);
		if(oldCurrent!=null){
			treemap.remove(oldCurrent.getSn());
			oldCurrent.setSn(oldCurrent.getDataSourceName()+"_"+MyUUID.getRandomUUID());//数据源名称+随机数
			treemap.put(oldCurrent.getSn(), oldCurrent);
		}	
		ds.setDataSourceName(newDataSourceName);
		ds.setSn(ds.getDataSourceName()+"_"+Const.CURRENT);
		treemap.put(ds.getSn(), ds);
		
		//刷新Jlist中的数据
		refreshJlist(treemap);

		//更新 数据源文件中的数据。
		try {
			DataSourceMapUtil.saveDs2Disk(treemap);	
			jLabel_tips.setToolTipText("");
			jLabel_tips.setForeground(Color.BLUE);
			jLabel_tips.setText("<html>重命名成功！</html>");
			jLabel_tips.setIcon(null);
		} catch (Exception e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(null, e);
			}
			final String er = e.getLocalizedMessage();
			jLabel_tips.setToolTipText("");
			jLabel_tips.setForeground(Color.RED);
			jLabel_tips.setText("<html>"+"出错了！"+er+"</html>");	
			jLabel_tips.setIcon(null);
		}
	}
	
	/*
	 * 点击确定按钮：
	 * 获取面板中的数据--根据这些数据建立到数据库的连接--连接成功。
	 */
	private void okMouseClicked() {
		jLabel_tips.setToolTipText("");
		jLabel_tips.setForeground(Color.BLUE);
		jLabel_tips.setText("<html>正在连接，请稍后...</html>");
		jLabel_tips.setIcon(ImageIcons.wait_gif32);
		
		//新开启线程，负责连接数据库。
		Thread thread = new Thread(){
			TreeMap<String,DataSourceInfo> treemap = null;
			public void run() {
				
				// 验证能否连接至数据库
				DataSourceInfo ds = new DataSourceInfo();
				ds.setDbtype(jComboBox1_dbtype.getSelectedItem().toString());
				ds.setUsername(jTextField_username.getText().trim());
				ds.setPwd(new String(jPasswordField_password.getPassword()).trim());
				ds.setDriverClass(jTextField_driverClass.getText().trim());
				ds.setUrl(jTextArea_url.getText().trim());

				//根据界面输入的信息，创建到相应数据库的连接。
				try {
					URLClassLoader loader = JarLoadUtil.getURLClassLoader();
					Class<?> clazz = loader.loadClass(ds.getDriverClass());
					Driver driver = (Driver)clazz.newInstance();		
					
					Properties p = new Properties();
					p.put("user", ds.getUsername());
					p.put("password", ds.getPwd());		
					
					Connection con = driver.connect(ds.getUrl(), p);
					ConnUtil.getInstance().setConn(con);
				} catch (Exception e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
					final String getLocalizedMessage = e.getLocalizedMessage();
					SwingUtilities.invokeLater(new Runnable(){
						public void run() {
							
							//连接失败时，设置提示信息
							//设置jlabel字体颜色为红色
							jLabel_tips.setForeground(Color.RED);
							jLabel_tips.setToolTipText("");
							
							//添加<html>标签后，Jlabel中展现数据时会自动换行，否则不会。
							jLabel_tips.setText("<html>"+getLocalizedMessage+"</html>");	
							jLabel_tips.setIcon(ImageIcons.empty_png_24);
						}
					});
					//记录错误详情
					errorInfo = ExceptionUtil.getExceptionInfo(e);
					return;
				} 
				//如果获取数据库连接成功
				if (ConnUtil.getInstance().getConn() != null) {
					SwingUtilities.invokeLater(new Runnable(){
						public void run() {
							//清除提示信息
							jLabel_tips.setToolTipText("");
							jLabel_tips.setText(null);
							jLabel_tips.setIcon(null);
							errorInfo = null;
						}
					});
				
					// 将连接配置信息存储至datasource.data文件中，并设置为当前（current）连接配置。
					treemap = DataSourceMapUtil.getDataSourceTreeMap();
					if (treemap == null) {
						treemap = new TreeMap<String, DataSourceInfo>();
					} else {
						// 首先将以前的默认值置为非默认值，然后将当前的连接置为新的默认值。如果以前默认值和当前连接相同，则不在重置。
						DataSourceInfo old = DataSourceMapUtil.getCurrentCon(treemap);// <key:sn,value:ds>
						if (old != null) {	
							if (! old.equals(ds)) {
								treemap.remove(old.getSn());//先删除
								old.setSn(old.getDataSourceName()+"_"+MyUUID.getRandomUUID());//数据源名称+随机数
								treemap.put(old.getSn(), old);//再插入
							}
						}
					}
					// 检查当前连接在treemap中是否已经存在，如果已存在，则先将已存在的删除，然后再插入。
					DataSourceInfo returnValue = DataSourceMapUtil.isExisted(ds);
					if (returnValue != null) {
						treemap.remove(returnValue.getSn());
						ds.setDataSourceName(returnValue.getDataSourceName());// DataSourceName不变
					} else {
						// 如果不存在，则说明是第一次使用此连接登录，提示用户设置该连接的名称。
						String newDataSourceName = JOptionPane.showInputDialog(getInstance(),
								"您是第一次使用当前连接，请为当前连接设置数据源名称:\n(说明：如不指定，系统将采用默认名称)");
						if (newDataSourceName == null || "".equals(newDataSourceName.trim())) {
							newDataSourceName = "未命名数据源";
						}
						while(newDataSourceName.indexOf(",")>=0||newDataSourceName.indexOf(":")>=0){
							newDataSourceName = JOptionPane.showInputDialog(getInstance(),"数据源名称中不能包含有逗号和冒号，请重新输入！");
							if (newDataSourceName == null || "".equals(newDataSourceName.trim())) {
								newDataSourceName = "未命名数据源";
							}
						}
						ds.setDataSourceName(newDataSourceName);
					}
					ds.setSn(ds.getDataSourceName()+"_"+Const.CURRENT);
					treemap.put(ds.getSn(), ds);

					// 保存数据源信息至磁盘
					DataSourceMapUtil.saveDs2Disk(treemap);
					
					final DataSourceInfo info = DataSourceMapUtil.getCurrentCon(treemap);

					SwingUtilities.invokeLater(new Runnable(){
						public void run() {
						
							// 刷新help_list
							refreshJlist(treemap);
							
							//隐藏本窗体，显示主窗体
							MainFrame.getInstance(info).setVisible(true);
							//getInstance().setVisible(false);
							getInstance().dispose();
						}
					});
				}
			}
		};
		thread.start();
	}
	/*
	 * Jlist中的值发生变化时，同步更新左侧文本框中的数据
	 */
	private void jList1_helpValueChanged(ListSelectionEvent evt) {
		Object selectedValue = jList1_help.getSelectedValue();
		if(selectedValue==null){
			return;
		}	
		DataSourceInfo ds = (DataSourceInfo)selectedValue;			
		jTextField_username.setText(ds.getUsername());
		jPasswordField_password.setText(ds.getPwd());
		jComboBox1_dbtype.setSelectedItem(ds.getDbtype());
		jTextField_driverClass.setText(ds.getDriverClass());
		jTextArea_url.setText(ds.getUrl());
		jLabel_tips.setText(null);
		jLabel_tips.setIcon(null);
		}
}
