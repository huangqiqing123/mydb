package test.tool.gui.dbtool.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.commons.codec.digest.DigestUtils;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import com.fasterxml.jackson.databind.ObjectMapper;

import test.tool.gui.common.MyColor;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
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
	public void showJsonTab(){
		getInstance().tabbedPane.setSelectedIndex(2);
	}
	public void showSqlTab(){
		getInstance().tabbedPane.setSelectedIndex(3);
	}
	public void showTimeStampTab(){
		getInstance().tabbedPane.setSelectedIndex(4);
	}
	public void showUrlTab(){
		getInstance().tabbedPane.setSelectedIndex(5);
	}
	public void showHashTab(){
		getInstance().tabbedPane.setSelectedIndex(6);
	}
	public void setBackColor(){
		MyColor mycolor = (MyColor)ConfigUtil.getConfInfo().get(Const.EYE_SAFETY_COLOR);
		originTextJwt.setBackground(mycolor.getColor());
		originTextBase64.setBackground(mycolor.getColor());
		originTextJson.setBackground(mycolor.getColor());
		originTextSql.setBackground(mycolor.getColor());
		originTextUrl.setBackground(mycolor.getColor());
		originTextHash.setBackground(mycolor.getColor());
	
		targetTextJwt.setBackground(mycolor.getColor());
		targetTextBase64.setBackground(mycolor.getColor());
		targetTextJson.setBackground(mycolor.getColor());
		targetTextSql.setBackground(mycolor.getColor());
		targetTextUrl.setBackground(mycolor.getColor());
		targetTextHash.setBackground(mycolor.getColor());
	}
	public void setFont(Font font){
		originTextJwt.setFont(font);
		originTextBase64.setFont(font);
		originTextJson.setFont(font);
		originTextSql.setFont(font);
		originTextUrl.setFont(font);
		originTextHash.setFont(font);
	
		targetTextJwt.setFont(font);
		targetTextBase64.setFont(font);
		targetTextJson.setFont(font);
		targetTextSql.setFont(font);
		targetTextUrl.setFont(font);
		targetTextHash.setFont(font);
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
		tabbedPane.addTab("JWT解码", ImageIcons.txt_gif, initJwtTab(), "JWT编码解码");
		tabbedPane.addTab("Base64编码解码", ImageIcons.txt_gif, initBase64Tab(), "Base64编码解码");
		tabbedPane.addTab("Json格式化", ImageIcons.txt_gif, initJsonFormatTab(), "Json格式化");
		tabbedPane.addTab("SQL格式化", ImageIcons.txt_gif, initSqlFormatTab(), "SQL格式化");
		tabbedPane.addTab("时间戳转换", ImageIcons.txt_gif, initTimeStampFormatTab(), "时间戳转换");
		tabbedPane.addTab("URL编码解码", ImageIcons.txt_gif, initUrlEncodeTab(), "URL编码解码");
		tabbedPane.addTab("哈希计算", ImageIcons.txt_gif, initHashTab(), "哈希计算");
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
	}
	final MyJextArea originTextBase64 = new MyJextArea(true);
	final MyJextArea targetTextBase64 = new MyJextArea(true);
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
		originTextBase64.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originTextBase64);
			}   	
        });    
		// 将JTextArea放入JScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new JScrollPane(originTextBase64));
		targetTextBase64.setLineWrap(true);
		targetTextBase64.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(targetTextBase64);
			}   	
        });    
		splitPane.setBottomComponent(new JScrollPane(targetTextBase64));

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
						originTextBase64.setText(result);
					} catch (Exception e1) {
						originTextBase64.setText(e1.toString());
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
		originTextJson.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
		originTextJson.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originTextJson);
			}   	
        });    
		// 将JTextArea放入JScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new JScrollPane(originTextJson));
		targetTextJson.setLineWrap(true);
		targetTextJson.setCodeFoldingEnabled(true);
		targetTextJson.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON_WITH_COMMENTS);
		splitPane.setBottomComponent(new JScrollPane(targetTextJson));
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
	final MyJextArea originTextSql = new MyJextArea(true);
	final MyJextArea targetTextSql = new MyJextArea(true);
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
		originTextSql.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originTextSql);
			}   	
        });    
		// 将JTextArea放入JScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new JScrollPane(originTextSql));
		targetTextSql.setLineWrap(true);
		splitPane.setBottomComponent(new JScrollPane(targetTextSql));
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
	final MyJextArea originTextJwt = new MyJextArea(true);
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
		originTextJwt.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originTextJwt);
			}   	
        });    
		// 将JTextArea放入JScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new JScrollPane(originTextJwt));
		targetTextJwt.setLineWrap(true);
		targetTextJwt.setCodeFoldingEnabled(true);
		targetTextJwt.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);//设置json语言高亮
		splitPane.setBottomComponent(new JScrollPane(targetTextJwt));
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
				targetTextJwt.setText("----header信息----\n");
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
				targetTextJwt.append("\n----payload信息----\n");
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
				targetTextJwt.append("\n----signature信息----\n");
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
		
		JLabel labelTimeStamp = new JLabel("时间戳");
		
		final JTextField fieldTimeStamp = new MyJTextField();
		fieldTimeStamp.setColumns(15);
		fieldTimeStamp.setPreferredSize(new Dimension(100, 25));
		fieldTimeStamp.setText((System.currentTimeMillis()/1000)+"");
		
		JRadioButton miao = new JRadioButton("秒");
		miao.setSelected(true);
		final JRadioButton haomiao = new JRadioButton("毫秒");
		ButtonGroup group = new ButtonGroup();
		group.add(miao);
		group.add(haomiao);
		
		JButton button = new JButton("转换");
		final JTextField result = new MyJTextField();
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

	final MyJextArea originTextUrl = new MyJextArea(true);
	final MyJextArea targetTextUrl = new MyJextArea(true);
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
		originTextUrl.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originTextUrl);
			}   	
        });    
		// 将JTextArea放入JScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new JScrollPane(originTextUrl));
		targetTextUrl.setLineWrap(true);
		splitPane.setBottomComponent(new JScrollPane(targetTextUrl));
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
	final MyJextArea originTextHash = new MyJextArea(true);
	final MyJextArea targetTextHash = new MyJextArea(true);
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
		originTextHash.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog(originTextUrl);
			}   	
        });    
		// 将JTextArea放入JScrollPane可以解决滚动条不展示的问题
		splitPane.setTopComponent(new JScrollPane(originTextHash));
		targetTextHash.setLineWrap(true);
		splitPane.setBottomComponent(new JScrollPane(targetTextHash));
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
				if (md5Radio.isSelected()) {
					try {
						String result = DigestUtils.md5Hex(originTextHash.getText().getBytes("utf-8"));
						targetTextHash.setText(result);
					} catch (Exception e1) {
						targetTextHash.setText(e1.toString());
					}
				} else if (sha1Radio.isSelected()) {
					try {
						String result = DigestUtils.sha1Hex(originTextHash.getText().getBytes("utf-8"));
						targetTextHash.setText(result);
					} catch (Exception e1) {
						targetTextHash.setText(e1.toString());
					}
				}else if (sha256Radio.isSelected()) {
					try {
						String result = DigestUtils.sha256Hex(originTextHash.getText().getBytes("utf-8"));
						targetTextHash.setText(result);
					} catch (Exception e1) {
						targetTextHash.setText(e1.toString());
					}
				} else if (sha512Radio.isSelected()) {
					try {
						String result = DigestUtils.sha512Hex(originTextHash.getText().getBytes("utf-8"));
						targetTextHash.setText(result);
					} catch (Exception e1) {
						targetTextHash.setText(e1.toString());
					}
				} else if (sha384Radio.isSelected()) {
					try {
						String result = DigestUtils.sha384Hex(originTextHash.getText().getBytes("utf-8"));
						targetTextHash.setText(result);
					} catch (Exception e1) {
						targetTextHash.setText(e1.toString());
					}
				}
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

	private void showFindReplaceDialog(MyJextArea area) {
		area.showFindDialog(this);
	}
	private void showFindReplaceDialog(MyJextAreaColor area) {
		area.showFindDialog(this);
	}
}
