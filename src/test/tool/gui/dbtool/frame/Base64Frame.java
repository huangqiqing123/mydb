package test.tool.gui.dbtool.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.ScrollPane;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.lf5.util.StreamUtils;
import org.apache.poi.util.IOUtils;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.themes.ThemesUtil;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.fasterxml.jackson.databind.ObjectMapper;

import test.tool.gui.common.MyColor;
import test.tool.gui.common.SysFontAndFace;
import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.image.ImageIcons;
import test.tool.gui.dbtool.mycomponent.MyJTextField;
import test.tool.gui.dbtool.mycomponent.MyJextArea;
import test.tool.gui.dbtool.mycomponent.MyJextAreaColor;
import test.tool.gui.dbtool.util.ConfigUtil;
import test.tool.gui.dbtool.util.SQLFormaterBasic;
import test.tool.gui.dbtool.util.SQLFormaterDDL;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Base64Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static Base64Frame frame;
	private JTabbedPane tabbedPane;

	public static Base64Frame getInstance(JFrame parentFrame){
		if(frame == null){
			frame = new Base64Frame(parentFrame);
		}
		return frame;
	}
	public static Base64Frame getInstance(){
		if(frame == null){
			frame = new Base64Frame(null);
		}
		return frame;
	}
	
	public void showJwtTab(){
		getInstance().tabbedPane.setSelectedIndex(0);
	}
	public void showBase64Tab(){
		getInstance().tabbedPane.setSelectedIndex(1);
	}
	public void showBase64FileTab(){
		getInstance().tabbedPane.setSelectedIndex(2);
	}
	public void showJsonTab(){
		getInstance().tabbedPane.setSelectedIndex(3);
	}
	public void showSqlTab(){
		getInstance().tabbedPane.setSelectedIndex(4);
	}
	public void showTimeStampTab(){
		getInstance().tabbedPane.setSelectedIndex(5);
	}
	public void showUrlTab(){
		getInstance().tabbedPane.setSelectedIndex(6);
	}
	public void showHashTab(){
		getInstance().tabbedPane.setSelectedIndex(7);
	}
	public void showHashFileTab(){
		getInstance().tabbedPane.setSelectedIndex(8);
	}
	public void showJInzhiTab(){
		getInstance().tabbedPane.setSelectedIndex(9);
	}
	public void showMdTab(){
		getInstance().tabbedPane.setSelectedIndex(10);
	}
	public void setBackColor(){
		MyColor mycolor = (MyColor)ConfigUtil.getConfInfo().get(Const.EYE_SAFETY_COLOR);
		originTextJwt.setBackground(mycolor.getColor());
		originTextBase64.setBackground(mycolor.getColor());
		originTextJson.setBackground(mycolor.getColor());
		originTextSql.setBackground(mycolor.getColor());
		originTextUrl.setBackground(mycolor.getColor());
		originTextHash.setBackground(mycolor.getColor());
		originMd.setBackground(mycolor.getColor());
	
		targetTextJwt.setBackground(mycolor.getColor());
		targetTextBase64.setBackground(mycolor.getColor());
		targetTextBase64File.setBackground(mycolor.getColor());
		targetTextJson.setBackground(mycolor.getColor());
		targetTextSql.setBackground(mycolor.getColor());
		targetTextUrl.setBackground(mycolor.getColor());
		targetTextHash.setBackground(mycolor.getColor());
		targetTextHashFile.setBackground(mycolor.getColor());
		targetMd.setBackground(mycolor.getColor());
	}
	public void setFont(Font font){
		originTextJwt.setFont(font);
		originTextBase64.setFont(font);
		originTextJson.setFont(font);
		originTextSql.setFont(font);
		originTextUrl.setFont(font);
		originTextHash.setFont(font);
		originMd.setFont(font);
	
		targetTextJwt.setFont(font);
		targetTextBase64.setFont(font);
		targetTextBase64File.setFont(font);
		targetTextJson.setFont(font);
		targetTextSql.setFont(font);
		targetTextUrl.setFont(font);
		targetTextHash.setFont(font);
		targetTextHashFile.setFont(font);
		targetMd.setFont(font);
	}
	

	/**
	 * Create the frame.
	 */
	private Base64Frame(JFrame parent) {
		if(parent != null){
			this.setLocationRelativeTo(parent);//设置父组件
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 976, 574);
		
		this.setIconImage(ImageIcons.ico_png.getImage());
		this.setTitle("工具箱");
		
		//整体JTabbedPane，一个JTabbedPane中可以加入多个选项卡。
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.addTab("JWT解码",initJwtTab());
		tabbedPane.addTab("Base64(文本)", initBase64Tab());
		tabbedPane.addTab("Base64(文件)",  initBase64FileTab());
		tabbedPane.addTab("Json格式化", initJsonFormatTab());
		tabbedPane.addTab("SQL格式化", initSqlFormatTab());
		tabbedPane.addTab("时间戳转换",  initTimeStampFormatTab());
		tabbedPane.addTab("URL编码解码",  initUrlEncodeTab());
		tabbedPane.addTab("哈希(文本)", initHashTab());
		tabbedPane.addTab("哈希(文件)",  initHashFile());
		tabbedPane.addTab("进制转换",  initJinzhi());
		tabbedPane.addTab("Markdown",  initMdTab());
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
	}
	final MyJextAreaColor originTextBase64 = new MyJextAreaColor(true);
	final MyJextAreaColor targetTextBase64 = new MyJextAreaColor(true);
	private JPanel initBase64Tab() {
		// base64panel中区域划分为两部分，上部分是分隔栏，下部分是设置和按钮区域。
		JPanel base64Panel = new JPanel(new BorderLayout());

		// 分隔栏
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setBorder(null);
		splitPane.setDividerSize(8);// 分隔栏宽度
		splitPane.setMinimumSize(new Dimension(0, 0)); // 最小可以为0
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);// 上下分割
		splitPane.setDividerLocation(150);// 分隔栏初始位置

		final String originTextDefaultValue = "待编码/解码内容，暂仅支持utf-8字符集";
		originTextBase64.setRelationObject(this);
		originTextBase64.setText(originTextDefaultValue);
		originTextBase64.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(originTextDefaultValue.equals(originTextBase64.getText())){
					originTextBase64.setText("");
				}
			}
		});
		originTextBase64.setLineWrap(true);// 自动换行
		originTextBase64.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		originTextBase64.setHighlightCurrentLine(false);
		originTextBase64.setCodeFoldingEnabled(true);
		ThemesUtil.updateTheme(originTextBase64, ThemesUtil.IDEA);
		originTextBase64.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originTextBase64);
			}   	
        });    
		// 将JTextArea放入RTextScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new RTextScrollPane(originTextBase64));
		targetTextBase64.setLineWrap(true);
		targetTextBase64.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		targetTextBase64.setHighlightCurrentLine(false);
		targetTextBase64.setCodeFoldingEnabled(true);
		ThemesUtil.updateTheme(targetTextBase64, ThemesUtil.IDEA);
		targetTextBase64.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(targetTextBase64);
			}   	
        });    
		splitPane.setBottomComponent(new RTextScrollPane(targetTextBase64));

		base64Panel.add(splitPane, BorderLayout.CENTER);

		// bottomPanel，东南西北布局， 包含设置区域、按钮区域
		JPanel bottomPanel = new JPanel(new BorderLayout());

		// 设置区域，流式布局，居中对齐
		JPanel settingsPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		ButtonGroup buttonGroup = new ButtonGroup();
		final JRadioButton basicRadio = new JRadioButton("Basic模式");
		final JRadioButton urlRadio = new JRadioButton("URL模式");
		urlRadio.setSelected(true);// 默认选中URL模式
		final JRadioButton mimeRadio = new JRadioButton("MIME模式");
		buttonGroup.add(basicRadio);
		buttonGroup.add(urlRadio);
		buttonGroup.add(mimeRadio);
		settingsPannel.add(basicRadio);
		settingsPannel.add(urlRadio);
		settingsPannel.add(mimeRadio);

		// 按钮区域，流式布局，居中对齐
		JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));// 按钮居中对齐
		JButton button = new JButton("编码");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (originTextBase64.getText() == null || originTextBase64.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "请输入待编码文本！");
				}
				if (basicRadio.isSelected()) {
					try {
						String result = Base64.getEncoder().encodeToString(originTextBase64.getText().getBytes("utf-8"));
						targetTextBase64.setText(result);
					} catch (Exception e1) {
						targetTextBase64.setText(e1.toString());
					}
				} else if (urlRadio.isSelected()) {
					try {
						String result = Base64.getUrlEncoder().encodeToString(originTextBase64.getText().getBytes("utf-8"));
						targetTextBase64.setText(result);
					} catch (Exception e1) {
						targetTextBase64.setText(e1.toString());
					}
				} else if (mimeRadio.isSelected()) {
					try {
						String result = Base64.getMimeEncoder().encodeToString(originTextBase64.getText().getBytes("utf-8"));
						targetTextBase64.setText(result);
					} catch (Exception e1) {
						targetTextBase64.setText(e1.toString());
					}
				} else {
					JOptionPane.showMessageDialog(frame, "请选择编码模式！");
				}
			}
		});
		buttonPannel.add(button);
		JButton button_1 = new JButton("解码");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (originTextBase64.getText() == null || originTextBase64.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "请输入待解码文本！");
					return;
				}
				if (basicRadio.isSelected()) {
					try {
						String result = new String(Base64.getDecoder().decode(originTextBase64.getText().getBytes("utf-8")),"utf-8");
						targetTextBase64.setText(result);
					} catch (Exception e1) {
						targetTextBase64.setText(e1.toString());
					}
				} else if (urlRadio.isSelected()) {
					try {
						String result = new String(
								Base64.getUrlDecoder().decode(originTextBase64.getText().getBytes("utf-8")),"utf-8");
						targetTextBase64.setText(result);
					} catch (Exception e1) {
						targetTextBase64.setText(e1.toString());
					}
				} else if (mimeRadio.isSelected()) {
					try {
						String result = new String(Base64.getMimeDecoder().decode(originTextBase64.getText().getBytes("utf-8")),"utf-8");
						targetTextBase64.setText(result);
					} catch (Exception e1) {
						targetTextBase64.setText(e1.toString());
					}
				} else {
					JOptionPane.showMessageDialog(frame, "请选择解码模式！");
				}
			}
		});
		buttonPannel.add(button_1);

		JButton emptyResult = new JButton("清空结果");
		emptyResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				targetTextBase64.setText("");
			}
		});
		buttonPannel.add(emptyResult);
		JButton emptyAll = new JButton("清空全部");
		emptyAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				originTextBase64.setText("");
				targetTextBase64.setText("");
			}
		});
		buttonPannel.add(emptyAll);

		// 将设置区域、按钮区域加入bottomPanel
		bottomPanel.add(settingsPannel, BorderLayout.NORTH);
		bottomPanel.add(buttonPannel, BorderLayout.SOUTH);

		// 将bottomPanel加入base64Panel
		base64Panel.add(bottomPanel, BorderLayout.SOUTH);
		return base64Panel;
	}
	
	String originFilePathBase64 = null;
	FileDialog openDialogBase64 = null;
	JButton jButtonOpenBase64 = null;
	final MyJextAreaColor targetTextBase64File = new MyJextAreaColor(true);
	private JPanel initBase64FileTab() {
		// base64panel中区域划分为两部分，上部分是分隔栏，下部分是设置和按钮区域。
		JPanel base64Panel = new JPanel(new BorderLayout());

		// 分隔栏
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setBorder(null);
		splitPane.setDividerSize(8);// 分隔栏宽度
		splitPane.setMinimumSize(new Dimension(0, 0)); // 最小可以为0
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);// 上下分割
		splitPane.setDividerLocation(100);// 分隔栏初始位置

		jButtonOpenBase64 = new JButton("请选择要编码的文件...",ImageIcons.open_png_24);
		jButtonOpenBase64.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//弹出路径选择对话框
				openDialogBase64 = new FileDialog(Base64Frame.getInstance(), "请选择要编码的文件...",FileDialog.LOAD);
		    	//openDialog.setDirectory(defaultPath);//设置默认打开的路径
				openDialogBase64.setVisible(true);
		 		
		 		// 点击了【确定】按钮
		 		if (openDialogBase64.getDirectory() != null && openDialogBase64.getFile() != null) {
		 			originFilePathBase64 = openDialogBase64.getDirectory() + openDialogBase64.getFile();
		 			jButtonOpenBase64.setText("已选择文件："+originFilePathBase64);;
		 		}
			}
		});
		splitPane.setTopComponent(jButtonOpenBase64);
		targetTextBase64File.setLineWrap(true);
		targetTextBase64File.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		targetTextBase64File.setHighlightCurrentLine(false);
		ThemesUtil.updateTheme(targetTextBase64File, ThemesUtil.IDEA);
		targetTextBase64File.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(targetTextBase64File);
			}   	
        });    
		splitPane.setBottomComponent(new RTextScrollPane(targetTextBase64File));

		base64Panel.add(splitPane, BorderLayout.CENTER);

		// bottomPanel，东南西北布局， 包含设置区域、按钮区域
		JPanel bottomPanel = new JPanel(new BorderLayout());

		// 设置区域，流式布局，居中对齐
		JPanel settingsPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		ButtonGroup buttonGroup = new ButtonGroup();
		final JRadioButton basicRadio = new JRadioButton("Basic模式");
		basicRadio.setSelected(true);// 默认选中basic模式
		final JRadioButton urlRadio = new JRadioButton("URL模式");
		final JRadioButton mimeRadio = new JRadioButton("MIME模式");
		buttonGroup.add(basicRadio);
		buttonGroup.add(urlRadio);
		buttonGroup.add(mimeRadio);
		settingsPannel.add(basicRadio);
		settingsPannel.add(urlRadio);
		settingsPannel.add(mimeRadio);

		// 按钮区域，流式布局，居中对齐
		JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));// 按钮居中对齐
		JButton button = new JButton("编码");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (originFilePathBase64 == null || originFilePathBase64.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "请选择待编码的文件！");
				}
				FileInputStream input = null;
				byte[] fileBytes = null;
				try {
					input = new FileInputStream(originFilePathBase64);
					fileBytes = StreamUtils.getBytes(input);
				} catch (FileNotFoundException e2) {
					targetTextBase64File.setText(e2.toString());
				} catch (Throwable e1 ) {
					targetTextBase64File.setText(e1.toString());
				}finally{
					try {
						input.close();
					} catch (Exception e2) {
					}
				}
				if(fileBytes == null || fileBytes.length == 0){
					JOptionPane.showMessageDialog(frame, "文件内容为空！");
				}
				if (basicRadio.isSelected()) {
					try {
						String result = Base64.getEncoder().encodeToString(fileBytes);
						targetTextBase64File.setText(result);
					} catch (Exception e1) {
						targetTextBase64File.setText(e1.toString());
					}
				} else if (urlRadio.isSelected()) {
					try {
						String result = Base64.getUrlEncoder().encodeToString(fileBytes);
						targetTextBase64File.setText(result);
					} catch (Exception e1) {
						targetTextBase64File.setText(e1.toString());
					}
				} else if (mimeRadio.isSelected()) {
					try {
						String result = Base64.getMimeEncoder().encodeToString(fileBytes);
						targetTextBase64File.setText(result);
					} catch (Exception e1) {
						targetTextBase64File.setText(e1.toString());
					}
				} else {
					JOptionPane.showMessageDialog(frame, "请选择编码模式！");
				}
			}
		});
		buttonPannel.add(button);

		JButton emptyResult = new JButton("清空结果");
		emptyResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				targetTextBase64File.setText("");
			}
		});
		buttonPannel.add(emptyResult);
		JButton emptyAll = new JButton("清空全部");
		emptyAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				originFilePathBase64 = null;
				jButtonOpenBase64.setText("请选择要编码的文件...");
				targetTextBase64File.setText("");
			}
		});
		buttonPannel.add(emptyAll);

		// 将设置区域、按钮区域加入bottomPanel
		bottomPanel.add(settingsPannel, BorderLayout.NORTH);
		bottomPanel.add(buttonPannel, BorderLayout.SOUTH);

		// 将bottomPanel加入base64Panel
		base64Panel.add(bottomPanel, BorderLayout.SOUTH);
		return base64Panel;
	}
	
	final MyJextAreaColor originTextJson = new MyJextAreaColor(true);
	final MyJextAreaColor targetTextJson = new MyJextAreaColor(true);
	private JPanel initJsonFormatTab() {
		// jsonpanel中区域划分为两部分，上部分是分隔栏面板(上栏是待格式化数据，下栏是格式化结果)，下部分是按钮区域。
		JPanel jsonPanel = new JPanel(new BorderLayout());

		// 分隔栏面板
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setBorder(null);
		splitPane.setDividerSize(8);// 分隔栏宽度
		splitPane.setMinimumSize(new Dimension(0, 0)); // 最小可以为0
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);// 上下分割
		splitPane.setDividerLocation(150);// 分隔栏初始位置

		final String originTextDefaultValue = "待格式化json文本";
		originTextJson.setName("json-origin");
		originTextJson.setText(originTextDefaultValue);
		originTextJson.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(originTextDefaultValue.equals(originTextJson.getText())){
					originTextJson.setText("");
				}
			}
		});
		originTextJson.setLineWrap(true);// 自动换行
		originTextJson.setCodeFoldingEnabled(true);
		originTextJson.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		originTextJson.setHighlightCurrentLine(false);
		ThemesUtil.updateTheme(originTextJson, ThemesUtil.IDEA);
		originTextJson.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originTextJson);
			}   	
        });    
		// 将JTextArea放入RTextScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new RTextScrollPane(originTextJson));
		targetTextJson.setLineWrap(true);
		targetTextJson.setCodeFoldingEnabled(true);
		targetTextJson.setHighlightCurrentLine(false);
		targetTextJson.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
		ThemesUtil.updateTheme(targetTextJson, ThemesUtil.IDEA);
		splitPane.setBottomComponent(new RTextScrollPane(targetTextJson));
		targetTextJson.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(targetTextJson);
			}   	
        });    

		//将分隔栏面板加入jsonPanel
		jsonPanel.add(splitPane, BorderLayout.CENTER);


		// 按钮区域，流式布局，居中对齐
		JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));// 按钮居中对齐
		JButton button = new JButton("格式化");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (originTextJson.getText() == null || originTextJson.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "请输入待格式化json文本！");
					return;
				}
				if(originTextJson.getText().trim().startsWith("{")){
					ObjectMapper mapper = new ObjectMapper();
					try {
						Map map = mapper.readValue(originTextJson.getText(), Map.class);
						String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);;
						targetTextJson.setText(result);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(frame, e2.getMessage());
						return;
					}
				}else if(originTextJson.getText().trim().startsWith("[")){
					ObjectMapper mapper = new ObjectMapper();
					try {
						List list = mapper.readValue(originTextJson.getText(), List.class);
						String result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);;
						targetTextJson.setText(result);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(frame, e2.getMessage());
						return;
					}
				}else{
					targetTextJson.setText(originTextJson.getText());
				}
			}
		});
		buttonPannel.add(button);

		JButton emptyResult = new JButton("清空结果");
		emptyResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				targetTextJson.setText("");
			}
		});
		buttonPannel.add(emptyResult);
		JButton emptyAll = new JButton("清空全部");
		emptyAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				originTextJson.setText("");
				targetTextJson.setText("");
			}
		});
		buttonPannel.add(emptyAll);

		// 将buttonPannel加入jsonPanel
		jsonPanel.add(buttonPannel, BorderLayout.SOUTH);
		return jsonPanel;
	}
	//====================================================
	final MyJextAreaColor originTextSql = new MyJextAreaColor(true);
	final MyJextAreaColor targetTextSql = new MyJextAreaColor(true);
	private JPanel initSqlFormatTab() {
		// jsonpanel中区域划分为两部分，上部分是分隔栏面板(上栏是待格式化数据，下栏是格式化结果)，下部分是按钮区域。
		JPanel jsonPanel = new JPanel(new BorderLayout());

		// 分隔栏面板
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setBorder(null);
		splitPane.setDividerSize(8);// 分隔栏宽度
		splitPane.setMinimumSize(new Dimension(0, 0)); // 最小可以为0
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);// 上下分割
		splitPane.setDividerLocation(150);// 分隔栏初始位置

		final String originTextDefaultValue = "待格式化SQL文本";
		originTextSql.setName("sql-origin");
		originTextSql.setText(originTextDefaultValue);
		originTextSql.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(originTextDefaultValue.equals(originTextSql.getText())){
					originTextSql.setText("");
				}
			}
		});
		originTextSql.setLineWrap(true);// 自动换行
		originTextSql.setCodeFoldingEnabled(true);
		originTextSql.setHighlightCurrentLine(false);
		originTextSql.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		ThemesUtil.updateTheme(originTextSql, ThemesUtil.IDEA);
		originTextSql.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originTextSql);
			}   	
        });    
		// 将JTextArea放入RTextScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new RTextScrollPane(originTextSql));
		targetTextSql.setLineWrap(true);
		targetTextSql.setCodeFoldingEnabled(true);
		targetTextSql.setHighlightCurrentLine(false);
		targetTextSql.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
		ThemesUtil.updateTheme(targetTextSql, ThemesUtil.IDEA);
		splitPane.setBottomComponent(new RTextScrollPane(targetTextSql));
		targetTextSql.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(targetTextSql);
			}   	
        });    

		//将分隔栏面板加入jsonPanel
		jsonPanel.add(splitPane, BorderLayout.CENTER);


		// 按钮区域，流式布局，居中对齐
		JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));// 按钮居中对齐
		JButton button = new JButton("格式化");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (originTextSql.getText() == null || originTextSql.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "请输入待格式化SQL文本！");
					return;
				}
				String sql = originTextSql.getText().trim();
				String sqlTmp = sql.toLowerCase();
				if(sqlTmp.startsWith("select") 
						|| sqlTmp.startsWith("on")
						||sqlTmp.startsWith("insert")
						||sqlTmp.startsWith("delete")
						||sqlTmp.startsWith("update")){
					
					targetTextSql.setText(new SQLFormaterBasic().format(sql).trim());
					
				}else if(sqlTmp.startsWith("alter") 
						|| sqlTmp.startsWith("create")
						||sqlTmp.startsWith("comment")){
					
					targetTextSql.setText(SQLFormaterDDL.INSTANCE.format(sql).trim());
					
				}else{
					targetTextSql.setText(sql);
				}
			}
		});
		buttonPannel.add(button);

		JButton emptyResult = new JButton("清空结果");
		emptyResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				targetTextJson.setText("");
			}
		});
		buttonPannel.add(emptyResult);
		JButton emptyAll = new JButton("清空全部");
		emptyAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				originTextSql.setText("");
				targetTextSql.setText("");
			}
		});
		buttonPannel.add(emptyAll);

		// 将buttonPannel加入jsonPanel
		jsonPanel.add(buttonPannel, BorderLayout.SOUTH);
		return jsonPanel;
	}
	//===============================================
	final MyJextAreaColor originMd = new MyJextAreaColor(true);
	final JEditorPane targetMd = new JEditorPane();
	private JPanel initMdTab() {
		
		// jsonpanel中区域划分为两部分，上部分是分隔栏面板(上栏是待格式化数据，下栏是格式化结果)，下部分是按钮区域。
		JPanel jsonPanel = new JPanel(new BorderLayout());

		// 分隔栏面板
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setBorder(null);
		splitPane.setDividerSize(8);// 分隔栏宽度
		splitPane.setMinimumSize(new Dimension(0, 0)); // 最小可以为0
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);// 上下分割
		splitPane.setDividerLocation(150);// 分隔栏初始位置

		originMd.setLineWrap(false);// 自动换行
		originMd.setCodeFoldingEnabled(true);
		originMd.setHighlightCurrentLine(false);
		originMd.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
		ThemesUtil.updateTheme(originMd, ThemesUtil.IDEA);
		originMd.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originMd);
			}   	
        });    
		// 将JTextArea放入RTextScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new RTextScrollPane(originMd));
		targetMd.setContentType("text/html");
		splitPane.setBottomComponent(new JScrollPane(targetMd));

		//将分隔栏面板加入jsonPanel
		jsonPanel.add(splitPane, BorderLayout.CENTER);


		// 按钮区域，流式布局，居中对齐
		JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));// 按钮居中对齐
		JButton button = new JButton("转换");
		JButton buttonHtml = new JButton("另存为html");
		buttonHtml.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 将内容输出至外部文件
				// 弹出路径选择框
				FileDialog saveDialog = new FileDialog(Base64Frame.getInstance(),"保存文件", FileDialog.SAVE);
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
					try {
						File file = new File(path);
						FileWriter fileWriter = new FileWriter(file);
						fileWriter.write(targetMd.getText());
						fileWriter.flush();
						fileWriter.close();
						JOptionPane.showMessageDialog(Base64Frame.getInstance(), "保存成功=>" + path);
						
						//用本地默认编辑器打开
						Desktop desk=Desktop.getDesktop(); 
						desk.open(file);
						
					} catch (IOException e2) {
						JOptionPane.showMessageDialog(Base64Frame.getInstance(), e2.getMessage());
					}
				}
			}
			
		});;
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//支持table表格
				List<Extension> tableExtensions = Arrays.asList(TablesExtension.create());
				List<Extension> headingAnchorExtensions = Arrays.asList(HeadingAnchorExtension.create());
				Parser parser = Parser.builder()
						.extensions(tableExtensions)
						.extensions(headingAnchorExtensions)
						.build();
				Node document = parser.parse(originMd.getText());
				HtmlRenderer renderer = HtmlRenderer.builder()
						.extensions(tableExtensions)
						.extensions(headingAnchorExtensions)
						.attributeProviderFactory(new AttributeProviderFactory() {
							@Override
							public AttributeProvider create(AttributeProviderContext context) {
								return new CustomAttributeProvider();
							}
						})
				       .build();
				String html = renderer.render(document);
				StringBuffer sb = new StringBuffer();
				sb.append("<html>");
				sb.append("<style>");
				sb.append("body");
				sb.append("{");
				sb.append("font-size:14pt;");
				sb.append("font-family:Arial;");
				sb.append("}");
				sb.append("td");
				sb.append("{");
				sb.append("font-size:14pt;");
				sb.append("border:1 solid #5b99c8;font-family:Arial;");
				sb.append("}");
				sb.append("th");
				sb.append("{");
				sb.append("font-size:14pt;");
				sb.append("background-color:#8fcae9;");
				sb.append("border:1 solid #5b99c8;font-family:Arial;");
				sb.append("}</style>");
				sb.append("<head>");
				sb.append("</head>");
				sb.append("<body bgcolor=\"#eff7ff\">");
				sb.append(html);
				sb.append("</body>");
				sb.append("</html>");
				System.out.println(sb.toString());
				targetMd.setText(sb.toString());
			}
			
		});
		buttonPannel.add(button);
		buttonPannel.add(buttonHtml);

		// 将buttonPannel加入jsonPanel
		jsonPanel.add(buttonPannel, BorderLayout.SOUTH);
		return jsonPanel;
	}
    static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            //改变a标签的target属性为_blank
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof TableBlock) {
            	attributes.put("border", "1");
            	attributes.put("cellpadding","2");
            	attributes.put("cellspacing","0");
                attributes.put("style", "border-collapse:collapse;border:1 solid #5b99c8;");
            }
        }
    }
	//===============================================
	final MyJextAreaColor originTextJwt = new MyJextAreaColor(true);
	final MyJextAreaColor targetTextJwt = new MyJextAreaColor(true);
	private JPanel initJwtTab() {
		JPanel jsonPanel = new JPanel(new BorderLayout());

		// 分隔栏面板
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setBorder(null);
		splitPane.setDividerSize(8);// 分隔栏宽度
		splitPane.setMinimumSize(new Dimension(0, 0)); // 最小可以为0
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);// 上下分割
		splitPane.setDividerLocation(150);// 分隔栏初始位置

		final String originTextDefaultValue = "待解码JWT";
		originTextJwt.setText(originTextDefaultValue);
		originTextJwt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(originTextDefaultValue.equals(originTextJwt.getText())){
					originTextJwt.setText("");
				}
			}
		});
		originTextJwt.setLineWrap(true);// 自动换行
		originTextJwt.setCodeFoldingEnabled(true);
		originTextJwt.setHighlightCurrentLine(false);
		originTextJwt.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		ThemesUtil.updateTheme(originTextJwt, ThemesUtil.IDEA);
		originTextJwt.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originTextJwt);
			}   	
        });    
		// 将JTextArea放入RTextScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new RTextScrollPane(originTextJwt));
		targetTextJwt.setLineWrap(true);
		targetTextJwt.setCodeFoldingEnabled(true);
		targetTextJwt.setHighlightCurrentLine(false);
		targetTextJwt.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);//设置json语言高亮
		ThemesUtil.updateTheme(targetTextJwt, ThemesUtil.IDEA);
		splitPane.setBottomComponent(new RTextScrollPane(targetTextJwt));
		originTextJwt.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(targetTextJwt);
			}   	
        });    

		//将分隔栏面板加入jsonPanel
		jsonPanel.add(splitPane, BorderLayout.CENTER);


		// 按钮区域，流式布局，居中对齐
		JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));// 按钮居中对齐
		JButton button = new JButton("解码");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (originTextJwt.getText() == null || originTextJwt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "请输入待解码JWT！");
					return;
				}
				String jwt = originTextJwt.getText().trim();
				if(jwt.startsWith("bearer") || jwt.startsWith("Bearer")){
					jwt = jwt.substring("bearer".length());
					jwt = jwt.trim();
				}
				String arr[] = jwt.split("\\.");
				if(arr.length != 3){
					JOptionPane.showMessageDialog(frame, "JWT格式不正确【应该是.分割的三段】");
					return;
				}
				targetTextJwt.setText("// ----header信息----\n");
				String header = arr[0];
				try {
					header = new String(Base64.getUrlDecoder().decode(header.getBytes("utf-8")),"utf-8");
					ObjectMapper mapper = new ObjectMapper();
					Map map = mapper.readValue(header, Map.class);
					header = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);;
					targetTextJwt.append(header);
				} catch (Exception e1) {
					targetTextJwt.setText(e1.toString());
					return;
				}
				targetTextJwt.append("\n// ----payload信息----\n");
				String payload = arr[1];
				try {
					payload = new String(Base64.getUrlDecoder().decode(payload.getBytes("utf-8")),"utf-8");
					ObjectMapper mapper = new ObjectMapper();
					Map map = mapper.readValue(payload, Map.class);
					payload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);;
					targetTextJwt.append(payload);
				} catch (Exception e1) {
					targetTextJwt.setText(e1.toString());
					return;
				}
				targetTextJwt.append("\n //----signature信息----\n");
				String signature = arr[2];
				targetTextJwt.append(signature);
			}
		});
		buttonPannel.add(button);

		JButton emptyResult = new JButton("清空结果");
		emptyResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				targetTextJwt.setText("");
			}
		});
		buttonPannel.add(emptyResult);
		JButton emptyAll = new JButton("清空全部");
		emptyAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				originTextJwt.setText("");
				targetTextJwt.setText("");
			}
		});
		buttonPannel.add(emptyAll);

		// 将buttonPannel加入jsonPanel
		jsonPanel.add(buttonPannel, BorderLayout.SOUTH);
		return jsonPanel;
	}
	private JPanel initTimeStampFormatTab() {
		//流式布局，左对齐
		JPanel timeStampPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JLabel labelTimeStamp = new JLabel("时间戳");labelTimeStamp.setFont(SysFontAndFace.font14);
		
		final JTextField fieldTimeStamp = new MyJTextField();fieldTimeStamp.setFont(SysFontAndFace.font14);
		fieldTimeStamp.setColumns(15);
		fieldTimeStamp.setPreferredSize(new Dimension(100, 25));
		fieldTimeStamp.setText((System.currentTimeMillis()/1000)+"");
		
		JRadioButton miao = new JRadioButton("秒");miao.setFont(SysFontAndFace.font14);
		miao.setSelected(true);
		final JRadioButton haomiao = new JRadioButton("毫秒");haomiao.setFont(SysFontAndFace.font14);
		ButtonGroup group = new ButtonGroup();
		group.add(miao);
		group.add(haomiao);
		
		JButton button = new JButton("转换");button.setFont(SysFontAndFace.font14);
		final JTextField result = new MyJTextField();result.setFont(SysFontAndFace.font14);
		result.setColumns(30);
		result.setPreferredSize(new Dimension(150, 25));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String originText = fieldTimeStamp.getText();
				if(originText == null || originText.isEmpty()){
					JOptionPane.showMessageDialog(frame, "请输入待转换的时间戳!");
					return;
				}
				try {
					if(haomiao.isSelected()){
						originText = originText.substring(0, originText.length()-3);
					}
					String dateStr = timeStamp2Date(originText);
					result.setText(dateStr);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(frame, "不是有效时间戳，请重新输入!");
					return;
				}
			}
		});
		
		
		JLabel beijing = new JLabel("北京时间");
		
		timeStampPanel.add(labelTimeStamp);
		timeStampPanel.add(fieldTimeStamp);
		timeStampPanel.add(miao);timeStampPanel.add(haomiao);
		timeStampPanel.add(button);
		timeStampPanel.add(result);
		timeStampPanel.add(beijing);
		return timeStampPanel;
	}

	public static String timeStamp2Date(String seconds) {
		if (seconds == null || seconds.isEmpty()) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	} 

	final MyJextAreaColor originTextUrl = new MyJextAreaColor(true);
	final MyJextAreaColor targetTextUrl = new MyJextAreaColor(true);
	private JPanel initUrlEncodeTab() {
		// urlpanel中区域划分为两部分，上部分是分隔栏，下部分是设置和按钮区域。
		JPanel urlpanel = new JPanel(new BorderLayout());

		// 分隔栏
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setBorder(null);
		splitPane.setDividerSize(8);// 分隔栏宽度
		splitPane.setMinimumSize(new Dimension(0, 0)); // 最小可以为0
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);// 上下分割
		splitPane.setDividerLocation(150);// 分隔栏初始位置

		final String originTextDefaultValue = "待编码/解码URL";
		originTextUrl.setText(originTextDefaultValue);
		originTextUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(originTextDefaultValue.equals(originTextUrl.getText())){
					originTextUrl.setText("");
				}
			}
		});
		originTextUrl.setLineWrap(true);// 自动换行
		originTextUrl.setHighlightCurrentLine(false);
		originTextUrl.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originTextUrl);
			}   	
        });    
		// 将JTextArea放入RTextScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new RTextScrollPane(originTextUrl));
		targetTextUrl.setLineWrap(true);
		targetTextUrl.setHighlightCurrentLine(false);
		splitPane.setBottomComponent(new RTextScrollPane(targetTextUrl));
		targetTextUrl.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(targetTextUrl);
			}   	
        });    

		urlpanel.add(splitPane, BorderLayout.CENTER);

		// bottomPanel，东南西北布局， 包含设置区域、按钮区域
		JPanel bottomPanel = new JPanel(new BorderLayout());

		// 设置区域，流式布局，居中对齐
		JPanel settingsPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		ButtonGroup buttonGroup = new ButtonGroup();
		final JRadioButton utf8Radio = new JRadioButton("UTF-8");
		final JRadioButton gbkRadio = new JRadioButton("GBK");
		utf8Radio.setSelected(true);// 默认选中URL模式
		buttonGroup.add(utf8Radio);
		buttonGroup.add(gbkRadio);
		settingsPannel.add(utf8Radio);
		settingsPannel.add(gbkRadio);

		// 按钮区域，流式布局，居中对齐
		JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));// 按钮居中对齐
		JButton button = new JButton("编码");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (originTextUrl.getText() == null || originTextUrl.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "请输入待编码URL！");
					return;
				}
				if (utf8Radio.isSelected()) {
					try {
						String result = URLEncoder.encode(originTextUrl.getText(), "utf-8");
						targetTextUrl.setText(result);
					} catch (Exception e1) {
						targetTextUrl.setText(e1.toString());
					}
				} else if (gbkRadio.isSelected()) {
					try {
						String result = URLEncoder.encode(originTextUrl.getText(), "gbk");
						targetTextUrl.setText(result);
					} catch (Exception e1) {
						targetTextUrl.setText(e1.toString());
					}
				}
			}
		});
		buttonPannel.add(button);
		JButton button_1 = new JButton("解码");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (originTextUrl.getText() == null || originTextUrl.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "请输入待解码URL！");
					return;
				}
				if (utf8Radio.isSelected()) {
					try {
						String result = URLDecoder.decode(originTextUrl.getText(), "utf-8");
						targetTextUrl.setText(result);
					} catch (Exception e1) {
						targetTextUrl.setText(e1.toString());
					}
				} else if (gbkRadio.isSelected()) {
					try {
						String result = URLDecoder.decode(originTextUrl.getText(), "gbk");
						targetTextUrl.setText(result);
					} catch (Exception e1) {
						targetTextUrl.setText(e1.toString());
					}
				}
			}
		});
		buttonPannel.add(button_1);

		JButton emptyResult = new JButton("清空结果");
		emptyResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				targetTextUrl.setText("");
			}
		});
		buttonPannel.add(emptyResult);
		JButton emptyAll = new JButton("清空全部");
		emptyAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				originTextUrl.setText("");
				targetTextUrl.setText("");
			}
		});
		buttonPannel.add(emptyAll);

		// 将设置区域、按钮区域加入bottomPanel
		bottomPanel.add(settingsPannel, BorderLayout.NORTH);
		bottomPanel.add(buttonPannel, BorderLayout.SOUTH);

		// 将bottomPanel加入base64Panel
		urlpanel.add(bottomPanel, BorderLayout.SOUTH);
		return urlpanel;
	}
	final MyJextAreaColor originTextHash = new MyJextAreaColor(true);
	final MyJextAreaColor targetTextHash = new MyJextAreaColor(true);
	private JPanel initHashTab() {
		
		//分为两部分，上部分是分隔栏，下部分是设置和按钮区域。
		JPanel hashpanel = new JPanel(new BorderLayout());

		// 分隔栏
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setBorder(null);
		splitPane.setDividerSize(8);// 分隔栏宽度
		splitPane.setMinimumSize(new Dimension(0, 0)); // 最小可以为0
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);// 上下分割
		splitPane.setDividerLocation(150);// 分隔栏初始位置

		final String originTextDefaultValue = "原值";
		originTextHash.setText(originTextDefaultValue);
		originTextHash.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(originTextDefaultValue.equals(originTextHash.getText())){
					originTextHash.setText("");
				}
			}
		});
		originTextHash.setLineWrap(true);// 自动换行
		originTextHash.setHighlightCurrentLine(false);
		originTextHash.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originTextUrl);
			}   	
        });    
		// 将JTextArea放入RTextScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new RTextScrollPane(originTextHash));
		targetTextHash.setLineWrap(true);
		targetTextHash.setHighlightCurrentLine(false);
		targetTextHash.setCodeFoldingEnabled(false);
		splitPane.setBottomComponent(new RTextScrollPane(targetTextHash));
		targetTextHash.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(targetTextHash);
			}   	
        });    

		hashpanel.add(splitPane, BorderLayout.CENTER);

		// bottomPanel，东南西北布局， 包含设置区域、按钮区域
		JPanel bottomPanel = new JPanel(new BorderLayout());

		// 设置区域，流式布局，居中对齐
		JPanel settingsPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		ButtonGroup buttonGroup = new ButtonGroup();
		final JRadioButton md5Radio = new JRadioButton("MD5");
		final JRadioButton sha1Radio = new JRadioButton("SHA1");
		final JRadioButton sha256Radio = new JRadioButton("SHA256");
		final JRadioButton sha384Radio = new JRadioButton("SHA384");
		final JRadioButton sha512Radio = new JRadioButton("SHA512");
		md5Radio.setSelected(true);// 默认选中MD5模式
		buttonGroup.add(md5Radio);
		buttonGroup.add(sha1Radio);
		buttonGroup.add(sha256Radio);
		buttonGroup.add(sha384Radio);
		buttonGroup.add(sha512Radio);
		settingsPannel.add(md5Radio);
		settingsPannel.add(sha1Radio);
		settingsPannel.add(sha256Radio);
		settingsPannel.add(sha384Radio);
		settingsPannel.add(sha512Radio);

		// 按钮区域，流式布局，居中对齐
		JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));// 按钮居中对齐
		JButton button = new JButton("计算");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (originTextHash.getText() == null || originTextHash.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame, "请输入原值！");
					return;
				}
				byte[] resultByteArr = null;
				if (md5Radio.isSelected()) {
					try {
						resultByteArr = DigestUtils.md5(originTextHash.getText().getBytes("utf-8"));
					} catch (Exception e1) {
						targetTextHash.setText(e1.toString());
					}
				} else if (sha1Radio.isSelected()) {
					try {
						resultByteArr  = DigestUtils.sha1(originTextHash.getText().getBytes("utf-8"));
					} catch (Exception e1) {
						targetTextHash.setText(e1.toString());
					}
				}else if (sha256Radio.isSelected()) {
					try {
						resultByteArr = DigestUtils.sha256(originTextHash.getText().getBytes("utf-8"));
					} catch (Exception e1) {
						targetTextHash.setText(e1.toString());
					}
				} else if (sha512Radio.isSelected()) {
					try {
						resultByteArr = DigestUtils.sha512(originTextHash.getText().getBytes("utf-8"));
					} catch (Exception e1) {
						targetTextHash.setText(e1.toString());
					}
				} else if (sha384Radio.isSelected()) {
					try {
						resultByteArr = DigestUtils.sha384(originTextHash.getText().getBytes("utf-8"));
					} catch (Exception e1) {
						targetTextHash.setText(e1.toString());
					}
				}
				String resultHex = new String(Hex.encodeHex(resultByteArr));
				String resultBase64 = Base64.getEncoder().encodeToString(resultByteArr);
				targetTextHash.setText("哈希后字节长度："+resultByteArr.length);
				targetTextHash.append("\n");
				targetTextHash.append("哈希后摘要位数："+resultByteArr.length*8);
				targetTextHash.append("\n");
				targetTextHash.append("哈希后16进制编码结果："+resultHex);
				targetTextHash.append("\n");
				targetTextHash.append("哈希后Base64编码结果："+resultBase64);
			}
		});
		buttonPannel.add(button);

		JButton emptyResult = new JButton("清空结果");
		emptyResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				targetTextHash.setText("");
			}
		});
		buttonPannel.add(emptyResult);
		JButton emptyAll = new JButton("清空全部");
		emptyAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				originTextHash.setText("");
				targetTextHash.setText("");
			}
		});
		buttonPannel.add(emptyAll);

		// 将设置区域、按钮区域加入bottomPanel
		bottomPanel.add(settingsPannel, BorderLayout.NORTH);
		bottomPanel.add(buttonPannel, BorderLayout.SOUTH);

		// 将bottomPanel加入base64Panel
		hashpanel.add(bottomPanel, BorderLayout.SOUTH);
		return hashpanel;
	}
	
	JButton jButtonOpenHash = null;
	FileDialog openDialogHash = null;
	String originFilePathHash = null;
	final MyJextAreaColor targetTextHashFile = new MyJextAreaColor(true);
	private JPanel initHashFile() {
		
		//分为两部分，上部分是分隔栏，下部分是设置和按钮区域。
		JPanel hashpanel = new JPanel(new BorderLayout());

		// 分隔栏
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setBorder(null);
		splitPane.setDividerSize(8);// 分隔栏宽度
		splitPane.setMinimumSize(new Dimension(0, 0)); // 最小可以为0
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);// 上下分割
		splitPane.setDividerLocation(100);// 分隔栏初始位置

		jButtonOpenHash = new JButton("请选择要哈希的文件...",ImageIcons.open_png_24);
		jButtonOpenHash.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//弹出路径选择对话框
				openDialogHash = new FileDialog(Base64Frame.getInstance(), "请选择要哈希的文件...",FileDialog.LOAD);
				openDialogHash.setVisible(true);
		 		
		 		// 点击了【确定】按钮
		 		if (openDialogHash.getDirectory() != null && openDialogHash.getFile() != null) {
		 			originFilePathHash = openDialogHash.getDirectory() + openDialogHash.getFile();
		 			jButtonOpenHash.setText("已选择文件："+originFilePathHash);;
		 		}
			}
		});
		splitPane.setTopComponent(jButtonOpenHash);

		targetTextHashFile.setLineWrap(true);
		targetTextHashFile.setHighlightCurrentLine(false);
		targetTextHashFile.setCodeFoldingEnabled(false);
		splitPane.setBottomComponent(new RTextScrollPane(targetTextHashFile));
		targetTextHashFile.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(targetTextHashFile);
			}   	
        });    
		hashpanel.add(splitPane, BorderLayout.CENTER);

		// bottomPanel，东南西北布局， 包含设置区域、按钮区域
		JPanel bottomPanel = new JPanel(new BorderLayout());

		// 设置区域，流式布局，居中对齐
		JPanel settingsPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		ButtonGroup buttonGroup = new ButtonGroup();
		final JRadioButton md5Radio = new JRadioButton("MD5");
		final JRadioButton sha1Radio = new JRadioButton("SHA1");
		final JRadioButton sha256Radio = new JRadioButton("SHA256");
		final JRadioButton sha384Radio = new JRadioButton("SHA384");
		final JRadioButton sha512Radio = new JRadioButton("SHA512");
		md5Radio.setSelected(true);// 默认选中MD5模式
		buttonGroup.add(md5Radio);
		buttonGroup.add(sha1Radio);
		buttonGroup.add(sha256Radio);
		buttonGroup.add(sha384Radio);
		buttonGroup.add(sha512Radio);
		settingsPannel.add(md5Radio);
		settingsPannel.add(sha1Radio);
		settingsPannel.add(sha256Radio);
		settingsPannel.add(sha384Radio);
		settingsPannel.add(sha512Radio);

		// 按钮区域，流式布局，居中对齐
		JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));// 按钮居中对齐
		JButton button = new JButton("计算");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (originFilePathHash == null || originFilePathHash.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "请选择要哈希的文件！");
					return;
				}
				FileInputStream input = null;
				byte[] fileBytes = null;
				try {
					input = new FileInputStream(originFilePathHash);
					fileBytes = StreamUtils.getBytes(input);
				} catch (FileNotFoundException e2) {
					targetTextHashFile.setText(e2.toString());
				} catch (Throwable e1) {
					e1.printStackTrace();
					targetTextHashFile.setText(e1.toString());
				}finally{
					try {
						input.close();
					} catch (Exception e2) {
					}
				}
				if(fileBytes == null || fileBytes.length == 0){
					JOptionPane.showMessageDialog(frame, "文件内容为空！");
					return;
				}
				
				byte[] resultByteArr = null;
				if (md5Radio.isSelected()) {
					try {
						resultByteArr = DigestUtils.md5(fileBytes);
					} catch (Exception e1) {
						targetTextHashFile.setText(e1.toString());
					}
				} else if (sha1Radio.isSelected()) {
					try {
						resultByteArr  = DigestUtils.sha1(fileBytes);
					} catch (Exception e1) {
						targetTextHashFile.setText(e1.toString());
					}
				}else if (sha256Radio.isSelected()) {
					try {
						resultByteArr = DigestUtils.sha256(fileBytes);
					} catch (Exception e1) {
						targetTextHashFile.setText(e1.toString());
					}
				} else if (sha512Radio.isSelected()) {
					try {
						resultByteArr = DigestUtils.sha512(fileBytes);
					} catch (Exception e1) {
						targetTextHashFile.setText(e1.toString());
					}
				} else if (sha384Radio.isSelected()) {
					try {
						resultByteArr = DigestUtils.sha384(fileBytes);
					} catch (Exception e1) {
						targetTextHashFile.setText(e1.toString());
					}
				}
				String resultHex = new String(Hex.encodeHex(resultByteArr));
				String resultBase64 = Base64.getEncoder().encodeToString(resultByteArr);
				targetTextHashFile.setText("哈希后字节长度："+resultByteArr.length);
				targetTextHashFile.append("\n");
				targetTextHashFile.append("哈希后摘要位数："+resultByteArr.length*8);
				targetTextHashFile.append("\n");
				targetTextHashFile.append("哈希后16进制编码结果："+resultHex);
				targetTextHashFile.append("\n");
				targetTextHashFile.append("哈希后Base64编码结果："+resultBase64);
			}
		});
		buttonPannel.add(button);

		JButton emptyResult = new JButton("清空结果");
		emptyResult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				targetTextHashFile.setText("");
			}
		});
		buttonPannel.add(emptyResult);
		JButton emptyAll = new JButton("清空全部");
		emptyAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				originFilePathHash = null;
				jButtonOpenHash.setText("请选择要哈希的文件...");
				targetTextHashFile.setText("");
			}
		});
		buttonPannel.add(emptyAll);

		// 将设置区域、按钮区域加入bottomPanel
		bottomPanel.add(settingsPannel, BorderLayout.NORTH);
		bottomPanel.add(buttonPannel, BorderLayout.SOUTH);

		// 将bottomPanel加入base64Panel
		hashpanel.add(bottomPanel, BorderLayout.SOUTH);
		return hashpanel;
	}
	private Component initJinzhi() {
		
		//JPanel jpanel = new JPanel(new BorderLayout());
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);//上下分割
		splitPane.setDividerSize(0);//分隔栏宽度
		splitPane.setDividerLocation(100);//分隔栏初始位置
		
		//流式布局，左对齐
		JPanel ten2OtherPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel originTenNum = new JLabel("十进制数：");originTenNum.setFont(SysFontAndFace.font14);
		final JTextField tenJinzhi = new MyJTextField();tenJinzhi.setFont(SysFontAndFace.font14);
		tenJinzhi.setColumns(30);
		tenJinzhi.setPreferredSize(new Dimension(100, 25));
		JLabel zhunaweitip = new JLabel("转换为：");zhunaweitip.setFont(SysFontAndFace.font14);
		JRadioButton two = new JRadioButton("2");two.setFont(SysFontAndFace.font14);
		two.setSelected(true);
		JRadioButton eight = new JRadioButton("8");eight.setFont(SysFontAndFace.font14);
		JRadioButton hex = new JRadioButton("16");hex.setFont(SysFontAndFace.font14);
		ButtonGroup group = new ButtonGroup();
		group.add(two);
		group.add(eight);
		group.add(hex);
		
		JButton button = new JButton("转换");button.setFont(SysFontAndFace.font14);
		final JTextField resultField = new MyJTextField();resultField.setFont(SysFontAndFace.font14);
		resultField.setColumns(30);
		resultField.setPreferredSize(new Dimension(150, 25));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String originText = tenJinzhi.getText();
				if(originText == null || originText.isEmpty()){
					JOptionPane.showMessageDialog(frame, "请输入待转换的数据!");
					return;
				}
				try {
					String result = null;
					if(two.isSelected()){
						result = Long.toBinaryString(Long.parseLong(originText));
					}else if(eight.isSelected()){
						result = Long.toOctalString(Long.parseLong(originText));
					}else if(hex.isSelected()){
						result = Long.toHexString(Long.parseLong(originText));
					}
					resultField.setText(result);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(frame, "进制转换出错!"+e2.getMessage());
					return;
				}
			}
		});
		ten2OtherPanel.setBorder(BorderFactory.createTitledBorder(""));
		ten2OtherPanel.add(originTenNum);
		ten2OtherPanel.add(tenJinzhi);
		ten2OtherPanel.add(zhunaweitip);
		ten2OtherPanel.add(two);ten2OtherPanel.add(eight);ten2OtherPanel.add(hex);
		ten2OtherPanel.add(button);
		ten2OtherPanel.add(resultField);
		
		//流式布局，左对齐
		JPanel other2TenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		TitledBorder titleBorder = BorderFactory.createTitledBorder("其他进制转为十进制:");
		titleBorder.setTitleFont(SysFontAndFace.font14);
		other2TenPanel.setBorder(titleBorder);
		JLabel originJinzhi = new JLabel("原进制：");originJinzhi.setFont(SysFontAndFace.font14);
		JRadioButton two2 = new JRadioButton("2");two2.setFont(SysFontAndFace.font14);
		two2.setSelected(true);
		JRadioButton eight2 = new JRadioButton("8");eight2.setFont(SysFontAndFace.font14);
		JRadioButton hex2 = new JRadioButton("16");hex2.setFont(SysFontAndFace.font14);
		ButtonGroup group2 = new ButtonGroup();
		group2.add(two2);
		group2.add(eight2);
		group2.add(hex2);
		JLabel originJinzhiData = new JLabel("原进制数据：");originJinzhiData.setFont(SysFontAndFace.font14);
		final JTextField originJinzhiText = new MyJTextField();originJinzhiText.setFont(SysFontAndFace.font14);
		originJinzhiText.setColumns(30);
		originJinzhiText.setPreferredSize(new Dimension(100, 25));
		JButton button2 = new JButton("转换");button2.setFont(SysFontAndFace.font14);
		final JTextField resultField2 = new MyJTextField();resultField2.setFont(SysFontAndFace.font14);
		resultField2.setColumns(30);
		resultField2.setPreferredSize(new Dimension(150, 25));
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String originText = originJinzhiText.getText();
				if(originText == null || originText.isEmpty()){
					JOptionPane.showMessageDialog(frame, "请输入待转换的数据!");
					return;
				}
				try {
					String result = null;
					if(two2.isSelected()){
						result = String.valueOf(Long.parseLong(originText,2));
					}else if(eight2.isSelected()){
						result = String.valueOf(Long.parseLong(originText,8));
					}else if(hex2.isSelected()){
						result = String.valueOf(Long.parseLong(originText,16));
					}
					resultField2.setText(result);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(frame, "进制转换出错!"+e2.getMessage());
					return;
				}
			}
		});
		other2TenPanel.add(originJinzhi);
		other2TenPanel.add(two2);other2TenPanel.add(eight2);other2TenPanel.add(hex2);
		other2TenPanel.add(originJinzhiData);
		other2TenPanel.add(originJinzhiText);
		other2TenPanel.add(button2);
		other2TenPanel.add(resultField2);
		splitPane.setTopComponent(ten2OtherPanel);
		splitPane.setBottomComponent(other2TenPanel);
		return splitPane;
	}

	private void showFindReplaceDialog(MyJextAreaColor area) {
		area.showFindDialog(this);
	}
}
