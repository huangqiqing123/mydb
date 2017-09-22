
package test.tool.gui.dbtool.frame;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import test.tool.gui.common.FontSet;
import test.tool.gui.common.MyColor;
import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.image.ImageIcons;
import test.tool.gui.dbtool.mycomponent.MyJextArea;
import test.tool.gui.dbtool.util.ConfigUtil;
import test.tool.gui.dbtool.util.DocUtil;

//简单记事本，用于查看、编辑文本信息
public class MyNotePad extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MyNotePad.class);
	private JToolBar jToolBar1 = new JToolBar();
	private JScrollPane jScrollPane1;
	private MyJextArea jTextArea1;
	public JLabel status = new JLabel();
    private LineNumber lineNumber = new LineNumber(); //行号
    private JButton jButton_new = new JButton("新建",ImageIcons.newtext_png_24);
    private JButton jButton_open = new JButton("打开",ImageIcons.open_png_24);
    private JButton jButton_save = new JButton("保存",ImageIcons.save_png_24);
    private JButton jButton_saveAs = new JButton("另存为",ImageIcons.saveas_png_24);
    private JButton jButton_clear = new JButton("清空",ImageIcons.empty_png_24);;
    private JButton jButton_find = new JButton("查找/替换",ImageIcons.find_png24);
    private JButton jButton_gotoline = new JButton("定位行",ImageIcons.gotoline_png24);
    private JButton jButton_redo = new JButton("恢复",ImageIcons.redo_png_24);
    private JButton jButton_undo = new JButton("撤销",ImageIcons.undo_png_24);
    
    private JButton jButton_moveup = new JButton("上移",ImageIcons.moveup_png24);
    private JButton jButton_movedown = new JButton("下移",ImageIcons.movedown_png24);
    private JButton jButton_font = new JButton("字体设置",ImageIcons.font_png24);
    //默认不换行显示
    private JButton jButton_lineWrap = new JButton("换行显示",ImageIcons.unselect_png24);
    
    public String title,content,filePath;

    /**
     * 构造函数 当content为null时，则读取filePath文件的内容，当filePath也为null时，则当前记事本内容为空
     * @param parent 设置父组件
     * @param title  记事本标题
     * @param content  记事本内容
     * @param filePath  当前记事本打开的文本文件路径
     */
    public MyNotePad(Component parent,String title,String content,String filePath) {	
    	
    	initComponents();     
    	
    	this.title = title;
    	this.content = content;
    	this.filePath = filePath;
    	
    	//建立关联
    	jTextArea1.setRelationObject(this);
    	
    	this.setResizable(true);//允许手动调整大小
    	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    	this.setSize(750, 500);//设置窗口初始大小
    	this.setIconImage(ImageIcons.ico_png.getImage());//设置标题栏图标 
    	this.setLocationRelativeTo(parent);//设置父组件
        
    	//为jTextArea1.find添加事件
		jTextArea1.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog();
			}   	
        });    
	
		//设置文本域内容（即记事本要展现的内容）
		if(this.content != null){
			jTextArea1.setText(this.content);
		}else{
			if(filePath != null){
				String[] strArr = DocUtil.getCharDocContent(filePath);
				this.filePath = strArr[0];//filePath有可能是相对路径(如查看日志)，此处转换为绝对路径
				jTextArea1.setText(strArr[1]);
				this.status.setText("当前文件编码："+strArr[2]);
				
				//根据文件路径显示文件内容时，如果未指定窗口title，则使用文件路径作为title
				if(this.title == null){
					this.title = this.filePath;
				}
				//重置textIsChanged为false
				jTextArea1.textIsChanged = false;
			}
		}
		//设置窗口标题
		if(this.title == null){
			setTitle("未命名记事本");
		}else{
			setTitle(this.title);
		}
		
		
		//更新文本域背景色
    	MyColor mycolor = (MyColor)ConfigUtil.getConfInfo().get(Const.EYE_SAFETY_COLOR);
    	jTextArea1.setBackground(mycolor.getColor());
    	
        //为jTextArea1设置键盘监听事件
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
        	
        	//键盘按下
        	//Ctrl+N 新建
        	//ctrl+f 执行查找替换
        	//ctrl+S 保存
        	//Ctrl+O 打开文件
        	//Ctrl+L  弹出定位行对话框
            public void keyPressed(java.awt.event.KeyEvent evt) {
            	if ((evt.getKeyCode() == KeyEvent.VK_F) && (evt.isControlDown())) {
            		showFindReplaceDialog();
            	}else if((evt.getKeyCode() == KeyEvent.VK_S) && (evt.isControlDown())){
            		save();
            	}else if((evt.getKeyCode() == KeyEvent.VK_N) && (evt.isControlDown())){
            		newText();
            	}else if((evt.getKeyCode() == KeyEvent.VK_O) && (evt.isControlDown())){
            		open(null);
            	}else if (evt.getKeyCode() == KeyEvent.VK_L && evt.isControlDown()) {
					showLocationLineDialog(jTextArea1);
				}
            }
         });   
    }

    private void initComponents() {
    	
    	//为窗口添加监听事件
    	this.addWindowListener(new WindowAdapter(){

    		//windowClosing事件：当用户点击窗口右上角的关闭按钮时触发。
			public void windowClosing(WindowEvent arg0) {
				beforeClose();
			}
    	});
    	
    	//工具栏
    	jToolBar1.setFloatable(false);//设置工具栏是否浮动
		jToolBar1.setRollover(true);//鼠标滑过效果
		
		//新建文本文档
		jButton_new.setToolTipText("快捷键：Ctrl+N");
		jButton_new.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
	       	newText();
	        }
	    });
		
		//打开
		jButton_open.setToolTipText("快捷键：Ctrl+O");
		jButton_open.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
	       	open(null);
	        }
	    });
	    
		//保存按钮
		jButton_save.setToolTipText("快捷键：Ctrl+S");
	    jButton_save.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
	       	save();
	        }
	    });
	 
	    //另存为按钮
	    jButton_saveAs.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
	       	saveAs();
	        }
	    });
		//放大
		JButton jButtonFD = new JButton("放大",ImageIcons.fangda_png);
		jButtonFD.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	Font font = jTextArea1.getFont();
	            	Font newFont = new Font(font.getName(),font.getStyle(),font.getSize()+1);
	            	jTextArea1.setFont(newFont);
	            	lineNumber.setFont(newFont);
	            }
	        });
		//缩小
		JButton jButtonSX = new JButton("缩小",ImageIcons.suoxiao_png);
		jButtonSX.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	Font font = jTextArea1.getFont();
	            	Font newFont = new Font(font.getName(),font.getStyle(),(font.getSize()-1)>0?(font.getSize()-1):1);
	            	jTextArea1.setFont(newFont);
	            	lineNumber.setFont(newFont);
	            }
	        });
		
		 //上移
        jButton_moveup.setToolTipText("快捷键：Alt+上方向键");
        jButton_moveup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	moveUp();
            }
        });
       
        //下移
        jButton_movedown.setToolTipText("快捷键：Alt+下方向键");
        jButton_movedown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	moveDown();
            }
        });
		
		//换行显示
		jButton_lineWrap.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Icon icon = jButton_lineWrap.getIcon();
				if(icon.equals(ImageIcons.unselect_png24)){
					jButton_lineWrap.setIcon(ImageIcons.select_png24);
					jTextArea1.setLineWrap(true);
				}else{
					jButton_lineWrap.setIcon(ImageIcons.unselect_png24);
					jTextArea1.setLineWrap(false);
				}	
			}
        });
		  
        //查找替换
		jButton_find.setToolTipText("快捷键：Ctrl+F");
        jButton_find.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	showFindReplaceDialog();
            }
        });
        //定位行
        jButton_gotoline.setToolTipText("快捷键：Ctrl+L");
        jButton_gotoline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	showLocationLineDialog(jTextArea1);
            }
        });
        
     
        //清空
	    jButton_clear.addActionListener(new java.awt.event.ActionListener() {
	       public void actionPerformed(java.awt.event.ActionEvent evt) {
	    	   jTextArea1.setText(null);
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
        //字体设置
		jButton_font.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	fontSet();
            	}
        });
    	
        //将按钮添加到工具栏
	    jToolBar1.add(jButton_new); //新建
	    jToolBar1.add(jButton_open); //打开
        jToolBar1.add(jButton_save); //保存
        jToolBar1.add(jButton_saveAs); //另存为
        jToolBar1.add(jButton_clear);//清空
		jToolBar1.add(jButtonFD);//放大
		jToolBar1.add(jButtonSX);//缩小
		jToolBar1.add(jButton_find);//查找替换
		jToolBar1.add(jButton_gotoline);//定位行
		jToolBar1.add(jButton_undo);//撤销
		jToolBar1.add(jButton_redo);//恢复
		jToolBar1.add(jButton_moveup);//上移
		jToolBar1.add(jButton_movedown);//下移
		jToolBar1.add(jButton_font);//字体设置
		jToolBar1.add(jButton_lineWrap);//换行显示
		
		//批量设置工具栏上的按钮
		int count = jToolBar1.getComponentCount();
		for(int i=0;i<count;i++){
			Component com = jToolBar1.getComponentAtIndex(i);
			if(com instanceof JButton){
				
				JButton button = (JButton)com;
				
				//设置边框不显示
				button.setBorderPainted(false);
				
				//设置不获得焦点
				button.setFocusable(false);
				
				//设置文字相对于图标的位置（上方图标、下方文字）
				button.setVerticalTextPosition(SwingConstants.BOTTOM);
				button.setHorizontalTextPosition(SwingConstants.CENTER);
			}
		}
		
		//JTextarea展示内容
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new MyJextArea(true);
        jScrollPane1.setViewportView(jTextArea1);
    	jTextArea1.setBackground(((MyColor)( ConfigUtil.getConfInfo().get(Const.EYE_SAFETY_COLOR))).getColor());
    	
    	//设置jtextArea 与 lineNumber 字体保持一致
    	jTextArea1.setFont((Font)( ConfigUtil.getConfInfo().get(Const.NOTEPAD_FONT)));
    	lineNumber.setFont(jTextArea1.getFont());
    	
    	//设置行号 
        jScrollPane1.setRowHeaderView(lineNumber);
      
    	//东西南北布局
    	getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);
        getContentPane().add(status, java.awt.BorderLayout.SOUTH);
        pack();
    }
 
    /**
     * 打开
     */
     public void open(String defaultPath){
    	 
    	//弹出路径选择对话框
    	FileDialog saveDialog = new FileDialog(this, "打开文件",FileDialog.LOAD);
    	if(defaultPath != null){
    		saveDialog.setDirectory(defaultPath);
    	}
 		saveDialog.setVisible(true);
 		
 		// 点击了【确定】按钮
 		if (saveDialog.getDirectory() != null && saveDialog.getFile() != null) {
 			String path = saveDialog.getDirectory() + saveDialog.getFile();
 			String str[] = DocUtil.getCharDocContent(path);
 			
 			//记录打开的文件的路径，并设置为标题显示
 			this.filePath = str[0];
 			this.setTitle(str[0]);
 			jTextArea1.setText(str[1]);
 			this.status.setText("当前文件编码："+str[2]);
 			
 			//新打开文档，textIsChanged重置为false
 			jTextArea1.textIsChanged = false;
 		}
     }
     
    /**
     * 另存为
     */
     private void saveAs(){
    	 
    	//弹出路径选择对话框
    	FileDialog saveDialog = new FileDialog(this, "另存为",FileDialog.SAVE);
 		saveDialog.setFile("未命名记事本.txt");
 		saveDialog.setVisible(true);
 		
 		// 点击了【确定】按钮
 		if (saveDialog.getDirectory() != null) {
 			String path = saveDialog.getDirectory() + saveDialog.getFile();
 			String saveStr = jTextArea1.getText();
 			saveToFile(path,saveStr);
 		}
     }
     
    /**
    * 保存
    */
    private void save() {

    	//1、如果filePath为null，则弹出路径选择对话框
    	//2、如果filePath不为null，则更新原文件。
    	if(filePath == null){
    		
    		//弹出路径选择对话框
        	FileDialog saveDialog = new FileDialog(this, "保存为",FileDialog.SAVE);
     		saveDialog.setFile("未命名记事本.txt");
     		saveDialog.setVisible(true);
     		
     		// 点击了【确定】按钮
     		if (saveDialog.getDirectory() != null) {
     			String path = saveDialog.getDirectory() + saveDialog.getFile();
     			String saveStr = jTextArea1.getText();
     			saveToFile(path,saveStr);
     			
     			//保存完成后，记录新创建的文件路径，并更新窗口标题
     			filePath = path;
     			this.setTitle(filePath);
     			
     			//重置textIsChanged为false
     			jTextArea1.textIsChanged = false;
     		}
    	}else{
    		String saveStr = jTextArea1.getText();
			saveToFile(this.filePath,saveStr);
			
			//重置textIsChanged为false
 			jTextArea1.textIsChanged = false;
    	}
    }
    /**
     * 保存指定内容至指定文件（文件原有内容会被覆盖）
     * @param filePath
     * @param content
     */
    private void saveToFile(String filePath,String content){
    	
    	OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8");
			out.write(content);
			out.flush();
			status.setText("<html><font color=blue><b>保存成功（"+time()+")<b></font></html>");
		} catch (Exception e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(null, e);
			}
			JOptionPane.showMessageDialog(this, " 出错:  " + e.getMessage());
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
				JOptionPane.showMessageDialog(this, " 关闭文件流出错:  "+ e.getMessage());
			}
		}
    }
    /**
     * 获取当前时间戳，精确到秒
     * @return
     */
    public String time(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	//如果不指定时区，在有些机器上会出现时间误差。  
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf.format(new Date());
    }
    /**
     * 查找对话框
     */
    private void showFindReplaceDialog(){
    	jTextArea1.showFindDialog(this);
    }
	/**
	 * 弹出定位行对话框
	 * @param area
	 */
	private void showLocationLineDialog(MyJextArea area){
		area.showLocationLineDialog(this);
	}
    /**
     * 当显示的是文档（即filepath不为空时），并且文档被修改时，窗口关闭前的处理
     */
    private void beforeClose(){
    	if(filePath != null && jTextArea1.textIsChanged){
    		 int opt = JOptionPane.showConfirmDialog(this,
    				 "文档内容已更改，是否保存？","确认对话框", JOptionPane.YES_NO_OPTION);		
        	 if(opt == JOptionPane.YES_OPTION){
        		 save(); 
        	 }
    	}
	}
    /**
     * 新建文本
     */
    private void newText(){
    	MyNotePad newNote = new MyNotePad(this,"未命名记事本",null,null);
    	newNote.setVisible(true);
    }
    /**
     * 下移一行
     */
    private void moveDown(){
    	jTextArea1.moveDown();
    }
    /**
     * 上移一行
     */
    private void moveUp(){
    	jTextArea1.moveUp();
    }
    /**
     * 字体设置
     */
    private void fontSet(){ 
    	Font oldFont = null;
    	if(ConfigUtil.getConfInfo().get(Const.NOTEPAD_FONT)!=null){
    		oldFont = (Font)ConfigUtil.getConfInfo().get(Const.NOTEPAD_FONT);
    	}
    	Font newFont = FontSet.showFontSetDialog(this,oldFont);
    	if(newFont!=null){	
    		
    		//更新界面字体
    		this.jTextArea1.setFont(newFont);
    		this.lineNumber.setFont(newFont);
    		
    		//更新至磁盘
    		ConfigUtil.getConfInfo().put(Const.NOTEPAD_FONT, newFont);
    		ConfigUtil.updateConfInfo();
    	}	
    }
}
