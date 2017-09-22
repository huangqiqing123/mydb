package test.tool.gui.dbtool.dialog;

import java.awt.Font;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.swing.LayoutStyle.ComponentPlacement;

import org.apache.log4j.Logger;

import test.tool.gui.common.MyColor;
import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.frame.LineNumber;
import test.tool.gui.dbtool.frame.MainFrame;
import test.tool.gui.dbtool.frame.MyNotePad;
import test.tool.gui.dbtool.mycomponent.MyJextArea;
import test.tool.gui.dbtool.util.ConfigUtil;
import test.tool.gui.dbtool.util.ConnUtil;
import test.tool.gui.dbtool.util.DBUtil;

import javax.swing.JScrollPane;

import java.awt.BorderLayout;



public class ExportIOPSQL extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ExportIOPSQL.class);
	 
	private static ExportIOPSQL instance = null;

	public static ExportIOPSQL getInstance(Frame parent, boolean modal){
		if(instance == null){
			instance = new ExportIOPSQL(parent,modal);
		}
		instance.initComponents();//时间每次弹出都更新
		return instance;
	}
	
	private JComboBox startYear;
	private JComboBox startMonth;
	private JComboBox startDay;
	private JComboBox startHour;
	private JComboBox startMinute;
	private JComboBox startSecond;
	
	private JComboBox endYear;
	private JComboBox endMonth;
	private JComboBox endDay;
	private JComboBox endHour;
	private JComboBox endMinute;
	private JComboBox endSecond;
	private JScrollPane scrollPane;
	private MyJextArea jTextArea1;
	
    private ExportIOPSQL(Frame parent, boolean modal) {
        super(parent, modal);
        setResizable(false);
        setTitle("导出IOP变更SQL");
        
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "\u8D77\u59CB\u65F6\u95F4\u70B9", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setLayout(null);
        
        startYear = new JComboBox();
        startYear.setBounds(48, 39, 77, 21);
        panel.add(startYear);
        
        startMonth = new JComboBox();
        startMonth.setBounds(49, 70, 76, 21);
        panel.add(startMonth);
        
        startDay = new JComboBox();
        startDay.setBounds(48, 100, 76, 21);
        panel.add(startDay);
        
        startHour = new JComboBox();
        startHour.setBounds(48, 133, 76, 21);
        panel.add(startHour);
        
        startMinute = new JComboBox();
        startMinute.setBounds(48, 164, 76, 21);
        panel.add(startMinute);
        
        startSecond = new JComboBox();
        startSecond.setBounds(48, 198, 76, 21);
        panel.add(startSecond);
        
        JLabel label = new JLabel("年");
        label.setBounds(20, 35, 43, 29);
        panel.add(label);
        
        JLabel label_1 = new JLabel("月");
        label_1.setBounds(20, 66, 29, 29);
        panel.add(label_1);
        
        JLabel label_2 = new JLabel("日");
        label_2.setBounds(20, 96, 37, 29);
        panel.add(label_2);
        
        JLabel label_3 = new JLabel("时");
        label_3.setBounds(20, 129, 37, 29);
        panel.add(label_3);
        
        JLabel label_4 = new JLabel("分");
        label_4.setBounds(20, 160, 29, 29);
        panel.add(label_4);
        
        JLabel label_5 = new JLabel("秒");
        label_5.setBounds(20, 194, 37, 29);
        panel.add(label_5);
        
        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(null, "\u622A\u6B62\u65F6\u95F4\u70B9", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        
        JButton btnNewButton = new JButton("生成变更SQL");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		exportSQL();
        	}
        });
        
        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new TitledBorder(null, "关联表，多个表名称之间以分号分隔", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(8)
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap(150, Short.MAX_VALUE)
        			.addComponent(btnNewButton)
        			.addGap(144))
        		.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(7)
        			.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
        			.addGap(12))
        );
        panel_2.setLayout(new BorderLayout(0, 0));
        
        scrollPane = new JScrollPane();
        panel_2.add(scrollPane);
        
        endYear = new JComboBox();
        endYear.setBounds(50, 31, 80, 21);
        panel_1.setLayout(null);
        
        JLabel label_6 = new JLabel("年");
        label_6.setLabelFor(endYear);
        label_6.setBounds(20, 23, 30, 29);
        panel_1.add(label_6);
        panel_1.add(endYear);
        
        endMonth = new JComboBox();
        endMonth.setBounds(50, 66, 80, 21);
        panel_1.add(endMonth);
        
        endDay = new JComboBox();
        endDay.setBounds(50, 97, 80, 21);
        panel_1.add(endDay);
        
        endHour = new JComboBox();
        endHour.setBounds(50, 128, 80, 21);
        panel_1.add(endHour);
        
        endMinute = new JComboBox();
        endMinute.setBounds(50, 159, 80, 21);
        panel_1.add(endMinute);
        
        endSecond = new JComboBox();
        endSecond.setBounds(50, 190, 80, 21);
        panel_1.add(endSecond);
        
        JLabel label_7 = new JLabel("月");
        label_7.setBounds(20, 58, 30, 29);
        panel_1.add(label_7);
        
        JLabel label_8 = new JLabel("日");
        label_8.setBounds(20, 89, 43, 29);
        panel_1.add(label_8);
        
        JLabel label_9 = new JLabel("时");
        label_9.setBounds(20, 120, 43, 29);
        panel_1.add(label_9);
        
        JLabel label_10 = new JLabel("分");
        label_10.setBounds(20, 151, 43, 29);
        panel_1.add(label_10);
        
        JLabel label_11 = new JLabel("秒");
        label_11.setBounds(20, 182, 43, 29);
        panel_1.add(label_11);
        getContentPane().setLayout(groupLayout);
      
        jTextArea1 = new MyJextArea(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        //设置默认背景色
        MyColor defaultColor = (MyColor)ConfigUtil.getConfInfo().get(Const.EYE_SAFETY_COLOR);
        jTextArea1.setBackground(defaultColor.getColor());
       //设置字体
        Font font = (Font)ConfigUtil.getConfInfo().get(Const.SQL_FONT);
   	 	jTextArea1.setFont(font);
        
        scrollPane.setViewportView(jTextArea1);
        //设置行号 
        LineNumber lineNumber = new LineNumber();
        scrollPane.setRowHeaderView(lineNumber);
        lineNumber.setFont(font);
        pack();
    }

    private void initComponents() {
    	
    	Calendar cc = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
    	//起始年
    	int currentYear = cc.get(Calendar.YEAR);
    	startYear.addItem(currentYear);
    	startYear.addItem(currentYear-1);
    	startYear.addItem(currentYear-2);
    	startYear.setSelectedItem(currentYear);
    	endYear.addItem(currentYear);
    	endYear.addItem(currentYear-1);
    	endYear.addItem(currentYear-2);
    	endYear.setSelectedItem(currentYear);
    	
    	//起始月
    	for (int i = 1; i <=12; i++) {
    		startMonth.addItem(i<10?"0"+i:""+i);
    		endMonth.addItem(i<10?"0"+i:""+i);
		}
    	int currentMonth = cc.get(Calendar.MONTH)+1;
    	String currentMonthStr = currentMonth<10?"0"+currentMonth:""+currentMonth;
    	startMonth.setSelectedItem(currentMonthStr);
    	endMonth.setSelectedItem(currentMonthStr);
    	
    	//起始天
    	for(int day = 1 ;day<=31;day++){
    		startDay.addItem(day<10?"0"+day:""+day);
    		endDay.addItem(day<10?"0"+day:""+day);
    	}
    	int currentDay = cc.get(Calendar.DAY_OF_MONTH);
    	String currentDayStr = currentDay<10?"0"+currentDay:""+currentDay;
    	startDay.setSelectedItem(currentDayStr);
    	endDay.setSelectedItem(currentDayStr);
    	
    	//起始时
    	int currentHour = cc.get(Calendar.HOUR_OF_DAY);
    	String currentHourStr = currentHour<10?"0"+currentHour:""+currentHour;
    	for(int hour = 0; hour <= 24; hour ++){
    		startHour.addItem(hour<10?"0"+hour:""+hour);
    		endHour.addItem(hour<10?"0"+hour:""+hour);
    	}
    	endHour.setSelectedItem(currentHourStr);
    	
    	//起始 分
    	int currentMinute = cc.get(Calendar.MINUTE);
    	String currentMinuteStr = currentMinute<10?"0"+currentMinute:""+currentMinute;
    	for(int minute = 0;minute<=60;minute++){
    		startMinute.addItem(minute<10?"0"+minute:""+minute);
    		endMinute.addItem(minute<10?"0"+minute:""+minute);
    	}
    	endMinute.setSelectedItem(currentMinuteStr);
    	
    	
    	//起始 秒
    	int currentSecond = cc.get(Calendar.SECOND);
    	String currentSecondStr = currentSecond<10?"0"+currentSecond:""+currentSecond;
    	for(int second = 0;second<=60;second++){
    		startSecond.addItem(second<10?"0"+second:""+second);
    		endSecond.addItem(second<10?"0"+second:""+second);
    	}
    	endSecond.setSelectedItem(currentSecondStr);
    	
    	//表名称文本域
    	jTextArea1.setText("am_function_item;am_function_item_url;am_function_tree;am_role;am_role_function_item;am_role_url;am_url");
    }
    
    private void exportSQL(){
    	
    	String input = jTextArea1.getText();
    	if(input == null || input.isEmpty()){
    		JOptionPane.showMessageDialog(instance, "未指定表！");
    		return;
    	}
		String tableNames[] = input.split(";");
    	
    	//起始时间  格式：2017-09-05 00:00:00
    	String startTime = startYear.getSelectedItem()+"-"+startMonth.getSelectedItem()+"-"+startDay.getSelectedItem()
    			+" "+startHour.getSelectedItem()+":"+startMinute.getSelectedItem()+":"+startSecond.getSelectedItem();
    	//截止时间
    	String endTime = endYear.getSelectedItem()+"-"+endMonth.getSelectedItem()+"-"+endDay.getSelectedItem()
    			+" "+endHour.getSelectedItem()+":"+endMinute.getSelectedItem()+":"+endSecond.getSelectedItem();
    	//select where
    	String select = "select * from ";
    	String whereInsert = " where  created_at >= '"+startTime+"'  and  created_at <= '"+endTime+"'";
		
		//最终结果
		StringBuilder result = new StringBuilder();
		
		//mysql数据库的特殊处理
		String dbType = DBUtil.getDBProductInfo().getProductName();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		//生成insert sql
		for (String tableName : tableNames) {
			try {
				String sql = select + tableName + whereInsert;
				log.debug("IOP权限导出："+sql);
				ps = ConnUtil.getInstance().getConn().prepareStatement(sql);
				rs = ps.executeQuery();
				ResultSetMetaData rm = rs.getMetaData();
				int col_number = rm.getColumnCount();
				StringBuffer insertInit = new StringBuffer();
				if(dbType.contains("MYSQL")){
					insertInit.append("INSERT INTO `" + tableName + "` (");
					for (int j = 1; j <= col_number - 1; j++) {
						insertInit.append("`"+rm.getColumnName(j) + "`, ");
					}
					insertInit.append("`"+rm.getColumnName(col_number)+ "` ) VALUES ( ");
				}else{
					insertInit.append("INSERT INTO " + tableName + " (");
					for (int j = 1; j <= col_number - 1; j++) {
						insertInit.append(rm.getColumnName(j) + ", ");
					}
					insertInit.append(rm.getColumnName(col_number)+ " ) VALUES ( ");
				}
				while (rs.next()) {
					StringBuffer insertSql = new StringBuffer();
					insertSql.append(insertInit);
					for (int k = 1; k <= col_number - 1; k++) {
						int type = rm.getColumnType(k);
						String col = rs.getString(k);
						if (col == null || "(null)".equals(col)) {
							insertSql.append("null, ");
						} else {
							if(DBUtil.isChar(type)){
								col = col.replace("'", "''");
								insertSql.append("'" + col + "', ");
							} else {
								insertSql.append(col + ", ");
							}
						}
					}
					// 对于最后一列的特殊处理
					int type = rm.getColumnType(col_number);
					String col = rs.getString(col_number);
					if (col == null || "(null)".equals(col)) {
						insertSql.append("null );");
					} else {
						if(DBUtil.isChar(type)){
							col = col.replace("'", "''");
							insertSql.append("'" + col + "' );");
						} else {
							insertSql.append(col + " );");
						}
					}
					result.append(insertSql);//结果集中的每条记录生成一个insert语句
					result.append("\n");// 每个sql进行换行		
				}
				} catch (Exception e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
					JOptionPane.showMessageDialog(this, "出错了！"+e.getMessage());
					return;
				} finally {
					DBUtil.closeResultSetAndPreparedStatement(rs, ps);
				}
			}
		
		//生成update sql(含逻辑删除SQL)
		String whereUpdate = " where  updated_at >= '"+startTime+"'  and  updated_at <= '"+endTime+"'";
		String whereDelete = " where  is_deleted='1' and deleted_at >= '"+startTime+"'  and  deleted_at <= '"+endTime+"'";
		String[] whereArr = {whereUpdate,whereDelete};
		for (int i = 0; i < whereArr.length; i++) {
			for (String tableName : tableNames) {
				try {
					List<String> primaryKeyList = DBUtil.getPrimaryKeyList(tableName,MainFrame.getInstance().getDataSourceInfo().getUsername());
					if(primaryKeyList == null || primaryKeyList.isEmpty()){
						JOptionPane.showMessageDialog(this,"表[" +tableName+"]不存在主键，无法导出UPDATE SQL！");
						return;
					}
					String sql = select+tableName+whereArr[i];
					log.debug("IOP权限导出："+sql);
					ps = ConnUtil.getInstance().getConn().prepareStatement(sql);
					rs = ps.executeQuery();
					ResultSetMetaData rm = rs.getMetaData();
					int col_number = rm.getColumnCount();
					while (rs.next()) {
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
						for (int j = 1; j <= col_number; j++) {
							
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
							String col = rs.getString(j);
							if (col == null || "(null)".equals(col)) {
								updateSql.append("null,");
							} else {
								if (DBUtil.isChar(type)) {
									//使用双单引号，对单引号进行转义
									col = col.replace("'", "''");
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
							String pkValue = rs.getString(pk);
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
						result.append(updateSql);// 结果集中的每条记录生成一个update语句
						result.append("\n");// 每个sql进行换行
					}
				} catch (Exception e) {
					if ("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+ "")) {
						log.error(null, e);
					}
					JOptionPane.showMessageDialog(this,"出错了！" + e.getMessage());
					return;
				} finally {
					DBUtil.closeResultSetAndPreparedStatement(rs, ps);
				}
			}
		}
		 MyNotePad td = new MyNotePad(instance,"变更SQL",result.toString(),null);
         td.setVisible(true);
	}
}
