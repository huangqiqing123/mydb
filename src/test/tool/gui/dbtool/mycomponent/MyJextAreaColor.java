package test.tool.gui.dbtool.mycomponent;

import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import org.apache.log4j.Logger;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.dialog.FindReplaceDialog;
import test.tool.gui.dbtool.frame.MyNotePad;
import test.tool.gui.dbtool.image.ImageIcons;
import test.tool.gui.dbtool.util.ConfigUtil;
import test.tool.gui.dbtool.util.DocUtil;

/*
 * 重写JTextArea，实现复制、粘贴、剪切、撤销、恢复 功能
 * 右键菜单：复制、粘贴、剪切
 * 快捷键：撤销（ctrl+z）、复制（ctrl+c）、粘贴（ctrl+v）、剪切（ctrl+x）
 * 外界调用接口：this.undomang.redo();this.undomang.undo();	
 * 
 * 由于已经继承了JTextArea，所以没法再继承DropTargetAdapter，而只能通过实现接口DropTargetListener来实现文件拖放
 */
public class MyJextAreaColor extends RSyntaxTextArea implements DropTargetListener {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MyJextAreaColor.class);
	private JPopupMenu jPopupMenu = new JPopupMenu();
	private JMenuItem copy = new JMenuItem("复制");
	private JMenuItem paste = new JMenuItem("粘贴");
	private JMenuItem cut = new JMenuItem("剪切");
	private FindReplaceDialog findDialog = null;
	
	//用于记录当前文本域的关联对象，以便二者之间可以通信（如记事本中使用，则赋值为MyNotePad的一个实例）
	private Object relationObject = null;
	
	//查找替换菜单，默认没有实现监听事件，需要调用方来实现(外部实现时，可调用本类的showFindDialog(Frame frame))。
	public JMenuItem find = new JMenuItem("查找/替换");
	
	MyJextAreaColor myself = this;
	private boolean isShowFindMenu = false;
	
	//文本域内容是否发生变化，外部可进行修改。
	public boolean textIsChanged = false;

    //撤销、恢复
	/*
	 * 外界调用：
	 * if (this.undomang.canRedo()){
	         this.undomang.redo();
			}	
		if (this.undomang.canUndo()){
	    	this.undomang.undo();	
	    	}
	 */
    public Document doc = this.getDocument();
	public UndoManager undomang = new UndoManager(){
		private static final long serialVersionUID = -5960092671497318496L;
		public void undoableEditHappened(UndoableEditEvent e) {
			this.addEdit(e.getEdit());
		}
	};
	
	public MyJextAreaColor(boolean isShowFindMenu){

        //----------支持文件拖放--------------
        //注册DropTarget，并将它与组件相连，处理哪个组件的相连  
        //即连通组件（第一个this）和Listener(第二个this)  
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this,true);
        
        //开启文本拖放
        this.setDragEnabled(true);
        
        
		this.isShowFindMenu = isShowFindMenu;
		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent arg0) {
				if(arg0.getButton()==3){
					if(myself.isEnabled()){//如果当前组件处于不可用状态，则不弹出右键菜单
					
						jPopupMenu.removeAll();
						if(!myself.isEditable()){
							jPopupMenu.add(copy);
						}else{
							jPopupMenu.add(copy);
							jPopupMenu.add(paste);
							jPopupMenu.add(cut);
						}
						if(myself.isShowFindMenu){				
							jPopupMenu.addSeparator();
							jPopupMenu.add(find);
						}
						jPopupMenu.show(arg0.getComponent(), arg0.getX(), arg0.getY());		
					}	
				}
			}
        });        
		copy.setIcon(ImageIcons.copy_gif);
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myself.copy();
			}
		});
	    paste.setIcon(ImageIcons.paste_png);
        paste.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				myself.paste();		
			}   	
        });
        cut.setIcon(ImageIcons.cut_png);
        cut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				myself.cut();		
			}   	
        });    
        find.setIcon(ImageIcons.find_png16);
        
    	//撤销、恢复
    	doc.addUndoableEditListener(undomang);  
    	
    	//为文本域添加内容变动监听
    	doc.addDocumentListener(new DocumentListener(){
			public void changedUpdate(DocumentEvent e) {
				textIsChanged = true;
			}
			public void insertUpdate(DocumentEvent e) {
				textIsChanged = true;
			}
			public void removeUpdate(DocumentEvent e) {
				textIsChanged = true;
			}
        });
//--->>>>	父类RSyntaxTextArea已具有	ctrl+z 执行撤销 alt+下方向键 上下移动功能，不需要自行实现了。
//    	//为this设置键盘监听事件
//        this.addKeyListener(new java.awt.event.KeyAdapter() {
//        	
//        	//键盘按下
//            public void keyPressed(java.awt.event.KeyEvent evt) {
//           
//            	if(myself.isEditable()){
//            		//ctrl+z 执行撤销
//            		if ((evt.getKeyCode() == KeyEvent.VK_Z) && (evt.isControlDown())) {
//            			if (undomang.canUndo()){
//            				undomang.undo();	
//            			}
//            		//alt+下方向键 执行下移操作
//            		} else if(evt.isAltDown() && evt.getKeyCode() == KeyEvent.VK_DOWN){
//            			moveDown();
//            		//alt+上方向键 执行上移操作
//            		}else if(evt.isAltDown() && evt.getKeyCode() == KeyEvent.VK_UP){
//            			moveUp();
//            		}
//            	}
//            }
//        }); 
//    	<-----///
	}	
	
	@Override
	public void setText(String t) {
		super.setText(t);
		this.setCaretPosition(0);//设置文本后，使光标停留在文档开始位置。
	}

	/**
	 * 弹出查找替换对话框，供外部调用，外部调用时，须提供父窗口对象。
	 * @param owner
	 */
	public void showFindDialog(Frame owner){
		if(owner == null){
			return;
		}
		if(findDialog == null){
			findDialog = new FindReplaceDialog(owner, this);
		}
		findDialog.setVisible(true);
	}
	/**
	 * 弹出定位行对话框，供外部调用，外部调用时，须提供父窗口对象。
	 * @param owner
	 */
	public void showLocationLineDialog(Frame owner){
		
		//首先获得焦点，否则有可能无法正常定位
		this.requestFocusInWindow();
		
		//取得总行数
		int totalLineCount = this.getLineCount();
		if(totalLineCount <= 1){
			return ;
		}
		String title = "跳转至行：(1..."+totalLineCount+")";
		String line = JOptionPane.showInputDialog(owner,title);
		if(line==null||"".equals(line.trim())){
			return;
		}	
		try {
			int intLine = Integer.parseInt(line);
			if(intLine > totalLineCount){
				return;
			}
			//JTextArea起始行号是0，所以此处做减一处理
			int selectionStart = this.getLineStartOffset(intLine-1);
			int selectionEnd = this.getLineEndOffset(intLine-1);
			
			//如果是不是最后一行，selectionEnd做减一处理，是为了使光标与选中行在同一行
			if(intLine != totalLineCount){
				selectionEnd--;
			}
			this.setSelectionStart(selectionStart);
			this.setSelectionEnd(selectionEnd);
		} catch (Exception e) {
			if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
				log.error(null, e);
			}
		}
	}
	
	//实现接口 DropTargetListener，需要实现以下5个方法
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
	}
	@Override
	public void dragExit(DropTargetEvent dte) {
	}
	@Override
	public void dragOver(DropTargetDragEvent dtde) {}
	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {}
	//文件拖放处理
	@Override
	public void drop(DropTargetDropEvent event) {
		
		event.acceptDrop(DnDConstants.ACTION_COPY);
		Transferable transferable = event.getTransferable();
		DataFlavor[] flavors = transferable.getTransferDataFlavors();
		for (int i = 0; i < flavors.length; i++) {
			DataFlavor d = flavors[i];
			//拖放文件
			if (d.equals(DataFlavor.javaFileListFlavor)) {
				List<File> fileList = null;
				try {
					fileList = (List<File>) transferable.getTransferData(d);
					if(fileList != null && fileList.size() > 0){
						
						//一次性拖放多个文件时，只读取第一个文件的内容。
						//如果拖放的是文件夹
						String path = fileList.get(0).getAbsolutePath();
						if(fileList.get(0).isDirectory()){
							
							//如果当前文本域关联对象是记事本，则弹出打开对话框
							if(getRelationObject() != null && getRelationObject() instanceof MyNotePad){
								MyNotePad notePade = (MyNotePad)getRelationObject();
								notePade.open(path);
								return;
							}
						}
						String str[] = DocUtil.getCharDocContent(path);
						this.setText(str[1]);
					
						//如果当前文本域关联对象是记事本，则同步更新记事本的窗口标题
						if(getRelationObject() != null && getRelationObject() instanceof MyNotePad){
							
							MyNotePad notePade = (MyNotePad)getRelationObject();
							notePade.setTitle(str[0]);
							notePade.filePath = str[0];
							if(str[2] != null){
								notePade.encode_status.setText(str[2]);
							}
							
							//根据文件后缀，设置语法样式
				 			if(path.endsWith(".java")||path.endsWith(".JAVA")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);//设置语言高亮 
				 				notePade.syntaxStyle.setSelectedItem("Java");
				 			}else if(path.endsWith(".js")||path.endsWith(".JS")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
				 				notePade.syntaxStyle.setSelectedItem("JavaScript");
				 			}else if(path.endsWith(".c")||path.endsWith(".C")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
				 				notePade.syntaxStyle.setSelectedItem("C");
				 			}else if(path.endsWith(".css")||path.endsWith(".CSS")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSS);
				 				notePade.syntaxStyle.setSelectedItem("Css");
				 			}else if(path.endsWith(".csv")||path.endsWith(".CSV")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSV);
				 				notePade.syntaxStyle.setSelectedItem("Csv");
				 			}else if(path.endsWith(".dtd")||path.endsWith(".DTD")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_DTD);
				 				notePade.syntaxStyle.setSelectedItem("Dtd");
				 			}else if(path.endsWith(".dockerfile")||path.endsWith(".DOCKERFILE")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_DOCKERFILE);
				 				notePade.syntaxStyle.setSelectedItem("Dockerfile");
				 			}else if(path.endsWith(".go")||path.endsWith(".GO")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_GO);
				 				notePade.syntaxStyle.setSelectedItem("GO");
				 			}else if(path.endsWith(".groovy")||path.endsWith(".GROOVY")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_GROOVY);
				 				notePade.syntaxStyle.setSelectedItem("Groovy");
				 			}else if(path.endsWith(".html")||path.endsWith(".HTML")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
				 				notePade.syntaxStyle.setSelectedItem("Html");
				 			}else if(path.endsWith(".hosts")||path.endsWith(".HOSTS")|| (path.substring(path.lastIndexOf("\\")+1)).equalsIgnoreCase("hosts")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HOSTS);
				 				notePade.syntaxStyle.setSelectedItem("Hosts");
				 			}else if(path.endsWith(".ini")||path.endsWith(".INI")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_INI);
				 				notePade.syntaxStyle.setSelectedItem("Ini");
				 			}else if(path.endsWith(".jsp")||path.endsWith(".JSP")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSP);
				 				notePade.syntaxStyle.setSelectedItem("Jsp");
				 			}else if(path.endsWith(".json")||path.endsWith(".JSON")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
				 				notePade.syntaxStyle.setSelectedItem("Json");
				 			}else if(path.endsWith(".lua")||path.endsWith(".LUA")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LUA);
				 				notePade.syntaxStyle.setSelectedItem("Lua");
				 			}else if(path.endsWith(".md")||path.endsWith(".MD")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
				 				notePade.syntaxStyle.setSelectedItem("Markdown");
				 			}else if(path.endsWith(".php")||path.endsWith(".PHP")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PHP);
				 				notePade.syntaxStyle.setSelectedItem("Php");
				 			}else if(path.endsWith(".perl")||path.endsWith(".PERL")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PERL);
				 				notePade.syntaxStyle.setSelectedItem("Perl");
				 			}else if(path.endsWith(".properties")||path.endsWith(".PROPERTIES")||path.endsWith(".conf")||path.endsWith(".CONF")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
				 				notePade.syntaxStyle.setSelectedItem("Properties");
				 			}else if(path.endsWith(".py")||path.endsWith(".PY")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
				 				notePade.syntaxStyle.setSelectedItem("Python");
				 			}else if(path.endsWith(".rb")||path.endsWith(".RB")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_RUBY);
				 				notePade.syntaxStyle.setSelectedItem("Ruby");
				 			}else if(path.endsWith(".sql")||path.endsWith(".SQL")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
				 				notePade.syntaxStyle.setSelectedItem("SQL");
				 			}else if(path.endsWith(".sh")||path.endsWith(".SH")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
				 				notePade.syntaxStyle.setSelectedItem("Shell");
				 			}else if(path.endsWith(".xml")||path.endsWith(".XML")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
				 				notePade.syntaxStyle.setSelectedItem("Xml");
				 			}else if(path.endsWith(".yaml")||path.endsWith(".YAML")||path.endsWith(".yml")||path.endsWith(".YML")){
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_YAML);
				 				notePade.syntaxStyle.setSelectedItem("Yaml");
				 			}else{
				 				this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
				 				notePade.syntaxStyle.setSelectedItem("Text");
				 			}
							
							//记事本中，拖放新打开文件时，重置textIsChanged为false
							this.textIsChanged = false;
						}
					}
				}catch (Exception e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
				}
			//拖放文本(追加)
			}else if(d.equals(DataFlavor.stringFlavor)){
				try {
					this.append((String) transferable.getTransferData(d));
				} catch (Exception e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
				}}
			}
		}
		event.dropComplete(true);
	}
	/**
	 * 下移一行
	 */
	public void moveDown(){
		
		//首先获得焦点，否则有可能无法正常定位
		this.requestFocusInWindow();
		
		//获得选中文本
		String selection = this.getSelectedText();

		//没有选中文本的场景
		if (selection == null) {
			try {
				//获得当前光标位置
				int cur = this.getCaretPosition();
				//取得当前行行号
	            int row = this.getLineOfOffset(cur);
	            //取得最后一行的行号
	            int endRow = this.getLineCount()-1;
	            //如果当前行是最后一行，则return
				if(row == endRow){
					return ;
				}
				//取得当前行的起始位置
				int lineStartOffset = this.getLineStartOffset(row);
				//取得当前行结束位置
				int lineEndOffset = this.getLineEndOffset(row);
				//取得当前行的内容
				String lineText = this.getText(lineStartOffset, lineEndOffset-lineStartOffset);
				
				//取得下一行的起始位置
				int nextLineStartOffset = this.getLineStartOffset(row+1);
				//取得下一行的结束位置
				int nextLineEndOffset = this.getLineEndOffset(row+1);
				//取得下一行的内容
				String nextLineText = this.getText(nextLineStartOffset, nextLineEndOffset-nextLineStartOffset);
				
				//对于下一行不是以换行符结尾场景的特殊处理
				boolean isEndN = nextLineText.endsWith("\n");
				if(!isEndN){
					nextLineText = nextLineText+"\n";
					lineText = lineText.substring(0, lineText.length()-1);
				}
				
				//删除下一行内容
				this.replaceRange(null, nextLineStartOffset, nextLineEndOffset);
				//将当前行的内容插入下一行的起始位置
				this.insert(lineText, nextLineStartOffset);
				
				//删除当前行内容
				this.replaceRange(null, lineStartOffset, lineEndOffset);
				//将下一行的内容插入当前行的起始位置
				this.insert(nextLineText, lineStartOffset);

				//将新下移的行选中，以便于识别。
				//当前行下移后，新的下一行起始位置会发生变化，所以需要重新获取结束位置。
				this.setSelectionStart(this.getLineStartOffset(row+1));
				//(下移后结束位置不会发生变化)结束位置做减一处理，是为了防止光标所在行与当前选中行不一致（结束位置是换行符）
				if(isEndN){
					this.setSelectionEnd(nextLineEndOffset-1);
				}else{
					this.setSelectionEnd(nextLineEndOffset);
				}
			} catch (Exception e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
			}
		}else {
			try {
				//取得选中行的起始行号
				int startRow = this.getLineOfOffset(this.getSelectionStart());
				//取得选中行的结束行号
				int endRow = this.getLineOfOffset(this.getSelectionEnd());
				//取得最后一行的行号
	            int lastRow = this.getLineCount()-1;
	            //如果选中行的最后一行是文本域的最后一行，则return
				if(endRow == lastRow){
					return ;
				}
				//取得选中行的起始位置
				int startCur = this.getLineStartOffset(startRow);
				//取得选中行的结束位置
				int endCur = this.getLineEndOffset(endRow);
				//取得当前选择行的内容
				String text = this.getText(startCur,endCur-startCur);
				//取得下一行的起始位置
				int nextLineStartOffset = this.getLineStartOffset(endRow+1);
				//取得下一行的结束位置
				int nextLineEndOffset = this.getLineEndOffset(endRow+1);
				//取得下一行的内容
				String nextLineText = this.getText(nextLineStartOffset, nextLineEndOffset-nextLineStartOffset);
				
				//对于下一行不是以换行符结尾场景的特殊处理
				boolean isEndN = nextLineText.endsWith("\n");
				if(!isEndN){
					nextLineText = nextLineText+"\n";
					text = text.substring(0, text.length()-1);
				}
				
				//删除下一行内容
				this.replaceRange(null, nextLineStartOffset, nextLineEndOffset);
				//将选中行内容插入下一行的起始位置
				this.insert(text, nextLineStartOffset);
				
				//删除选中行内容
				this.replaceRange(null, startCur, endCur);
				//将下一行的内容插入选中行的起始位置
				this.insert(nextLineText, startCur);
				
				//将新下移的行选中，以便于识别。
				//选中行下移后，起始位置会发生变化，但是结束位置不会发生变化，所以需要重新获取起始位置。
				this.setSelectionStart(this.getLineStartOffset(startRow+1));
				//(下移后结束位置不会发生变化)结束位置做减一处理，是为了防止光标所在行与当前选中行不一致（结束位置是换行符）
				if(isEndN){
					this.setSelectionEnd(nextLineEndOffset-1);
				}else{
					this.setSelectionEnd(nextLineEndOffset);
				}
			} catch (Exception e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
			}
		}
	}
	/**
	 * 上移一行
	 */
	public void moveUp(){
		
		//首先获得焦点，否则有可能无法正常定位
		this.requestFocusInWindow();
		
		//获得选中文本
		String selection = this.getSelectedText();

		//没有选中文本的场景
		if (selection == null) {
			try {
				//获得当前光标位置
				int cur = this.getCaretPosition();
				//取得当前行行号
	            int row = this.getLineOfOffset(cur);
				if(row == 0){
					return ;//如果当前行是第一行，则return
				}
				//取得当前行的起始位置
				int lineStartOffset = this.getLineStartOffset(row);
				//取得当前行结束位置
				int lineEndOffset = this.getLineEndOffset(row);
				//取得当前行的内容
				String lineText = this.getText(lineStartOffset, lineEndOffset-lineStartOffset);
				
				//取得上一行的起始位置
				int upLineStartOffset = this.getLineStartOffset(row-1);
				//取得上一行的结束位置
				int upLineEndOffset = this.getLineEndOffset(row-1);
				//取得上一行的内容
				String upLineText = this.getText(upLineStartOffset, upLineEndOffset-upLineStartOffset);
				
				//对于当前行不是以换行符结尾场景的特殊处理
				if(!lineText.endsWith("\n")){
					lineText = lineText+"\n";
					upLineText = upLineText.substring(0, upLineText.length()-1);
				}
				
				//删除当前行内容
				this.replaceRange(null, lineStartOffset, lineEndOffset);
				//将上一行的内容插入当前行的起始位置
				this.insert(upLineText, lineStartOffset);
				
				//删除上一行内容
				this.replaceRange(null, upLineStartOffset, upLineEndOffset);
				//将当前行的内容插入上一行的起始位置
				this.insert(lineText, upLineStartOffset);
				
				//将新上移的行选中，以便于识别。
				//当前行上移后，新的上一行起始位置不会变化，但是结束位置会发生变化，所以需要重新获取结束位置。
				this.setSelectionStart(upLineStartOffset);
				//结束位置做减一处理，是为了防止光标所在行与当前选中行不一致（结束位置是换行符）
				this.setSelectionEnd(this.getLineEndOffset(row-1)-1);
			} catch (Exception e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
			}
		}else {
			try {
				//取得选中行的起始行号
				int startRow = this.getLineOfOffset(this.getSelectionStart());
				if(startRow == 0){
					return ;
				}
				//取得选中行的结束行号
				int endRow = this.getLineOfOffset(this.getSelectionEnd());
				//取得选中行的起始位置
				int startCur = this.getLineStartOffset(startRow);
				//取得选中行的结束位置
				int endCur = this.getLineEndOffset(endRow);
				//取得当前选择行的内容
				String text = this.getText(startCur,endCur-startCur);
				//取得上一行的起始位置
				int upLineStartOffset = this.getLineStartOffset(startRow-1);
				//取得上一行的结束位置
				int upLineEndOffset = this.getLineEndOffset(startRow-1);
				//取得上一行的内容
				String upLineText = this.getText(upLineStartOffset, upLineEndOffset-upLineStartOffset);
				
				//对于当前所选行不是以换行符结尾场景的特殊处理
				if(! text.endsWith("\n")){
					text = text+"\n";
					upLineText = upLineText.substring(0, upLineText.length()-1);
				}
				
				//删除选中行内容
				this.replaceRange(null, startCur, endCur);
				//将上一行的内容插入当前行的起始位置
				this.insert(upLineText, startCur);
				
				//删除上一行内容
				this.replaceRange(null, upLineStartOffset, upLineEndOffset);
				//将当前行的内容插入上一行的起始位置
				this.insert(text, upLineStartOffset);
				
				//将新上移的行选中，以便于识别。
				//当前行上移后，新的上一行起始位置不会变化，但是结束位置会发生变化，所以需要重新获取结束位置。
				this.setSelectionStart(upLineStartOffset);
				//结束位置做减一处理，是为了防止光标所在行与当前选中行不一致（结束位置是换行符）
				this.setSelectionEnd(this.getLineEndOffset(endRow-1)-1);
			} catch (Exception e) {
				if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
					log.error(null, e);
				}
			}
		}
	}
	//----------------一些set、get方法-------------------------
	public Object getRelationObject() {
		return relationObject;
	}
	public void setRelationObject(Object relationObject) {
		this.relationObject = relationObject;
	}
	
}


