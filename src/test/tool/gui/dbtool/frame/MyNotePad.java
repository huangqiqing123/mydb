
package test.tool.gui.dbtool.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.apache.log4j.Logger;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.themes.ThemesUtil;
import org.fife.ui.rtextarea.RTextScrollPane;

import test.tool.gui.common.FontSet;
import test.tool.gui.common.MyColor;
import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.image.ImageIcons;
import test.tool.gui.dbtool.mycomponent.MyJextArea;
import test.tool.gui.dbtool.mycomponent.MyJextAreaColor;
import test.tool.gui.dbtool.util.ConfigUtil;
import test.tool.gui.dbtool.util.DBDriverUtil;
import test.tool.gui.dbtool.util.DocUtil;

//简单记事本，用于查看、编辑文本信息
public class MyNotePad extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MyNotePad.class);
	private JToolBar jToolBar1 = new JToolBar();
	private JScrollPane jScrollPane1;
	private MyJextAreaColor jTextArea1;
	public JLabel status = new JLabel();
	public JLabel row_col_status = new JLabel();
	public JLabel encode_status = new JLabel();
    private JButton jButton_new = new JButton("新建",ImageIcons.newtext_png_24);
    private JButton jButton_open = new JButton("打开",ImageIcons.open_png_24);
    private JButton jButton_reload = new JButton("重新载入",ImageIcons.reload_png24);
    private JButton jButton_save = new JButton("保存",ImageIcons.save_png_24);
    private JButton jButton_saveAs = new JButton("另存为",ImageIcons.saveas_png_24);
    private JButton jButton_clear = new JButton("清空",ImageIcons.empty_png_24);;
    private JButton jButton_find = new JButton("查找/替换",ImageIcons.find_png24);
    private JButton jButton_gotoline = new JButton("定位行",ImageIcons.gotoline_png24);
    private JButton jButton_redo = new JButton("恢复",ImageIcons.redo_png_24);
    private JButton jButton_undo = new JButton("撤销",ImageIcons.undo_png_24);
    
    public JComboBox<String> syntaxStyle = new JComboBox<String>();//语法样式
    private Map<String,String> syntaxStyleMap = new LinkedHashMap<String,String>();
    
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
    	this.setSize(800, 500);//设置窗口初始大小
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
				this.encode_status.setText(strArr[2]);
				
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
    	
    	//初始化支持高亮的语法列表
    	initSyntaxStyleMap();
    	
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
		//重新载入
		jButton_reload.addActionListener(new java.awt.event.ActionListener() {
	    public void actionPerformed(java.awt.event.ActionEvent evt) {
	       	reload();
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
	            }
	        });
		//缩小
		JButton jButtonSX = new JButton("缩小",ImageIcons.suoxiao_png);
		jButtonSX.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	Font font = jTextArea1.getFont();
	            	Font newFont = new Font(font.getName(),font.getStyle(),(font.getSize()-1)>0?(font.getSize()-1):1);
	            	jTextArea1.setFont(newFont);
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
		//语法样式设置
		syntaxStyle.setToolTipText("选择语法样式");
		for(String key : syntaxStyleMap.keySet()){
			syntaxStyle.addItem(key);
		}
		//默认选中普通文本
		syntaxStyle.setSelectedItem("Text");
    	
        //将按钮添加到工具栏
		jToolBar1.add(jButton_reload); //重新载入
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
        jTextArea1 = new MyJextAreaColor(true);
        jScrollPane1 = new RTextScrollPane(jTextArea1);
    	jTextArea1.setBackground(((MyColor)( ConfigUtil.getConfInfo().get(Const.EYE_SAFETY_COLOR))).getColor());
    	
    	//添加插入符侦听器，以便侦听任何插入符的更改通知。 
		jTextArea1.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                try {
                    //e.getDot() 获得插入符的位置。 
                    int offset = e.getDot() ;
                 
                    //getLineOfOffset(int offset)  将组件文本中的偏移量转换为行号
                    int row = jTextArea1.getLineOfOffset(offset);
                    
                    //getLineStartOffset(int line)   取得给定行起始处的偏移量。
                    //getLineEndOffset(int line)     取得给定行结尾处的偏移量。
                    int column = e.getDot() - jTextArea1.getLineStartOffset(row);
                   
                    // 在状态栏中显示当前光标所在行号、所在列号
                    row_col_status.setText("第" + (row + 1) + "行，第" + (column+1)+"列");
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        //设置主题
        ThemesUtil.updateTheme(jTextArea1, ThemesUtil.IDEA);
        //语言选择下拉框时间事件监听
		syntaxStyle.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					String key = e.getItem().toString();
					jTextArea1.setSyntaxEditingStyle(syntaxStyleMap.get(key));
				}   	
	        });
		
		//设置jtextArea 与 lineNumber 字体保持一致
    	jTextArea1.setFont((Font)( ConfigUtil.getConfInfo().get(Const.NOTEPAD_FONT)));
    	jTextArea1.setCodeFoldingEnabled(true);
    	jTextArea1.setHighlightCurrentLine(false);//设置当前行不高亮
      
    	//东西南北布局
    	getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
        getContentPane().add(jToolBar1, java.awt.BorderLayout.NORTH);
       
  		JPanel bottomPanel = new JPanel(new GridLayout());
  		bottomPanel.add(status);
  		bottomPanel.add(new JSeparator(SwingConstants.VERTICAL));
  		bottomPanel.add(row_col_status);
  		bottomPanel.add(new JSeparator(SwingConstants.VERTICAL));
  		bottomPanel.add(encode_status);
  		bottomPanel.add(syntaxStyle);
  		
  		//底部区域 添加BorderLayout布局的面板 bottomPanel
  		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        
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
 			this.encode_status.setText(str[2]);
 			
 			//根据文件后缀，设置语法样式
 			if(path.endsWith(".java")||path.endsWith(".JAVA")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);//设置语言高亮 
 				syntaxStyle.setSelectedItem("Java");
 			}else if(path.endsWith(".js")||path.endsWith(".JS")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
 				syntaxStyle.setSelectedItem("JavaScript");
 			}else if(path.endsWith(".c")||path.endsWith(".C")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
 				syntaxStyle.setSelectedItem("C");
 			}else if(path.endsWith(".css")||path.endsWith(".CSS")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSS);
 				syntaxStyle.setSelectedItem("Css");
 			}else if(path.endsWith(".csv")||path.endsWith(".CSV")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSV);
 				syntaxStyle.setSelectedItem("Csv");
 			}else if(path.endsWith(".dtd")||path.endsWith(".DTD")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_DTD);
 				syntaxStyle.setSelectedItem("Dtd");
 			}else if(path.endsWith(".dockerfile")||path.endsWith(".DOCKERFILE")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_DOCKERFILE);
 				syntaxStyle.setSelectedItem("Dockerfile");
 			}else if(path.endsWith(".go")||path.endsWith(".GO")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_GO);
 				syntaxStyle.setSelectedItem("GO");
 			}else if(path.endsWith(".groovy")||path.endsWith(".GROOVY")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_GROOVY);
 				syntaxStyle.setSelectedItem("Groovy");
 			}else if(path.endsWith(".html")||path.endsWith(".HTML")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
 				syntaxStyle.setSelectedItem("Html");
 			}else if(path.endsWith(".hosts")||path.endsWith(".HOSTS")|| (path.substring(path.lastIndexOf("\\")+1)).equalsIgnoreCase("hosts")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HOSTS);
 				syntaxStyle.setSelectedItem("Hosts");
 			}else if(path.endsWith(".ini")||path.endsWith(".INI")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_INI);
 				syntaxStyle.setSelectedItem("Ini");
 			}else if(path.endsWith(".jsp")||path.endsWith(".JSP")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSP);
 				syntaxStyle.setSelectedItem("Jsp");
 			}else if(path.endsWith(".json")||path.endsWith(".JSON")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
 				syntaxStyle.setSelectedItem("Json");
 			}else if(path.endsWith(".lua")||path.endsWith(".LUA")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
 				syntaxStyle.setSelectedItem("Lua");
 			}else if(path.endsWith(".md")||path.endsWith(".MD")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
 				syntaxStyle.setSelectedItem("Markdown");
 			}else if(path.endsWith(".php")||path.endsWith(".PHP")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PHP);
 				syntaxStyle.setSelectedItem("Php");
 			}else if(path.endsWith(".perl")||path.endsWith(".PERL")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PERL);
 				syntaxStyle.setSelectedItem("Perl");
 			}else if(path.endsWith(".properties")||path.endsWith(".PROPERTIES")||path.endsWith(".conf")||path.endsWith(".CONF")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
 				syntaxStyle.setSelectedItem("Properties");
 			}else if(path.endsWith(".py")||path.endsWith(".PY")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
 				syntaxStyle.setSelectedItem("Python");
 			}else if(path.endsWith(".rb")||path.endsWith(".RB")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_RUBY);
 				syntaxStyle.setSelectedItem("Ruby");
 			}else if(path.endsWith(".sql")||path.endsWith(".SQL")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
 				syntaxStyle.setSelectedItem("SQL");
 			}else if(path.endsWith(".sh")||path.endsWith(".SH")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
 				syntaxStyle.setSelectedItem("Shell");
 			}else if(path.endsWith(".xml")||path.endsWith(".XML")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
 				syntaxStyle.setSelectedItem("Xml");
 			}else if(path.endsWith(".yaml")||path.endsWith(".YAML")||path.endsWith(".yml")||path.endsWith(".YML")){
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_YAML);
 				syntaxStyle.setSelectedItem("Yaml");
 			}else{
 				jTextArea1.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
 				syntaxStyle.setSelectedItem("Text");
 			}
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
	private void showLocationLineDialog(MyJextAreaColor area){
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
    		
    		//更新至磁盘
    		ConfigUtil.getConfInfo().put(Const.NOTEPAD_FONT, newFont);
    		ConfigUtil.updateConfInfo();
    	}	
    }
    /**
     * 重新载入
     */
     public void reload() {

    	if(this.filePath != null){
    		if(jTextArea1.textIsChanged){
        		int select = JOptionPane.showConfirmDialog(this,"文档内容已更改，重新载入将会覆盖当前文档内容，是否继续？","确认对话框", JOptionPane.YES_NO_OPTION);
        		if(select == JOptionPane.YES_OPTION){
        			String str[] = DocUtil.getCharDocContent(this.filePath);
        			jTextArea1.setText(str[1]);
        			this.encode_status.setText("：" + str[2] + "");

        			// 新打开文档，textIsChanged重置为false
        			jTextArea1.textIsChanged = false;
        			status.setText("重新载入完成！");
        		}
        	}else{
    			String str[] = DocUtil.getCharDocContent(this.filePath);
    			jTextArea1.setText(str[1]);
    			this.encode_status.setText(str[2]);

    			// 新打开文档，textIsChanged重置为false
    			jTextArea1.textIsChanged = false;
    			status.setText("重新载入完成！");
    		}
    	 }else{
    		 status.setText("还没有打开文件！");
    	 }
	}
     
     private void initSyntaxStyleMap(){
    	syntaxStyleMap.put("C", SyntaxConstants.SYNTAX_STYLE_C);
    	syntaxStyleMap.put("Css", SyntaxConstants.SYNTAX_STYLE_CSS);
    	syntaxStyleMap.put("Csv", SyntaxConstants.SYNTAX_STYLE_CSV);
    	syntaxStyleMap.put("Dtd", SyntaxConstants.SYNTAX_STYLE_DTD);
    	syntaxStyleMap.put("Dockerfile", SyntaxConstants.SYNTAX_STYLE_DOCKERFILE);
    	syntaxStyleMap.put("GO", SyntaxConstants.SYNTAX_STYLE_GO);
    	syntaxStyleMap.put("Groovy", SyntaxConstants.SYNTAX_STYLE_GROOVY);
    	syntaxStyleMap.put("Html", SyntaxConstants.SYNTAX_STYLE_HTML);
    	syntaxStyleMap.put("Hosts", SyntaxConstants.SYNTAX_STYLE_HOSTS);
    	syntaxStyleMap.put("Ini", SyntaxConstants.SYNTAX_STYLE_INI);
    	syntaxStyleMap.put("Java", SyntaxConstants.SYNTAX_STYLE_JAVA);
    	syntaxStyleMap.put("JavaScript", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
    	syntaxStyleMap.put("Json", SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
    	syntaxStyleMap.put("Jsp", SyntaxConstants.SYNTAX_STYLE_JSP);
    	syntaxStyleMap.put("Lua", SyntaxConstants.SYNTAX_STYLE_LUA);
    	syntaxStyleMap.put("Markdown", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
    	syntaxStyleMap.put("Php", SyntaxConstants.SYNTAX_STYLE_PHP);
    	syntaxStyleMap.put("Perl", SyntaxConstants.SYNTAX_STYLE_PERL);
    	syntaxStyleMap.put("Properties", SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
    	syntaxStyleMap.put("Python", SyntaxConstants.SYNTAX_STYLE_PYTHON);
    	syntaxStyleMap.put("Ruby", SyntaxConstants.SYNTAX_STYLE_RUBY);
    	syntaxStyleMap.put("SQL", SyntaxConstants.SYNTAX_STYLE_SQL);
    	syntaxStyleMap.put("TypeScript", SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT);
    	syntaxStyleMap.put("Shell", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
    	syntaxStyleMap.put("Xml", SyntaxConstants.SYNTAX_STYLE_XML);
    	syntaxStyleMap.put("Yaml", SyntaxConstants.SYNTAX_STYLE_YAML);
    	syntaxStyleMap.put("Text", SyntaxConstants.SYNTAX_STYLE_NONE);
     }
}
