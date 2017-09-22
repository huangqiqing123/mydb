/*
 * ExportFrame.java
 *
 * Created on 2012年2月1日, 下午5:15
 */

package test.tool.gui.dbtool.frame;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.csv.CsvDataSetWriter;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import test.tool.gui.common.MyColor;
import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.image.ImageIcons;
import test.tool.gui.dbtool.mycomponent.MyJextArea;
import test.tool.gui.dbtool.util.ConfigUtil;
import test.tool.gui.dbtool.util.ConnUtil;
import test.tool.gui.dbtool.util.DBUtil;

import javax.swing.JRadioButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ExportFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ExportFrame.class);
	
	private static ExportFrame instance = null;
	
	public static boolean hasInstance(){
		return instance != null;
	}
	
    public static ExportFrame getInstance(String text){
    	if(instance == null){
    		instance = new ExportFrame();
    	}
    	//更新背景色
    	MyColor mycolor = (MyColor)ConfigUtil.getConfInfo().get(Const.EYE_SAFETY_COLOR);
    	instance.jTextArea1.setBackground(mycolor.getColor());
    	
    	//更新字体
    	Font font = (Font)ConfigUtil.getConfInfo().get(Const.SQL_FONT);
    	instance.jTextArea1.setFont(font);
    	
    	//更新文本域内容
    	if(text != null){	
    		instance.jTextArea1.setText(text);
    	}else{
    		instance.jTextArea1.setText("");
    	}
    	
    	return instance;
    }
	//私有化构造函数，单例模式
    private ExportFrame() {
        initComponents();
    }

    private void initComponents() {

    	this.setIconImage(ImageIcons.ico_png.getImage());//设置标题栏图标
        jScrollPane1 = new javax.swing.JScrollPane();
        okButton = new JButton("确定",ImageIcons.ok_png);
        cancelButton = new JButton("关闭",ImageIcons.exit_png);
        exportEXCEL = new javax.swing.JRadioButton();
        exportXML = new javax.swing.JRadioButton();
        exportSQL = new javax.swing.JRadioButton();
        exportUpdateSQL = new JRadioButton();
        exportCSV =  new JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("导出对话框");setResizable(false);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        
        //为jTextArea1.find添加事件
        jTextArea1.find.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				showFindReplaceDialog();
			}   	
        }); 
        // ctrl+f 执行查找替换	
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				if (evt.isControlDown()) {
					if (evt.getKeyCode() == KeyEvent.VK_F) {
						showFindReplaceDialog();
					}
				}
			}
        });
        exportEXCEL.setText("导出EXCEL");
        exportXML.setText("导出XML");
        exportSQL.setText("导出INSERT SQL");
        exportUpdateSQL.setText("导出UPDATE SQL");
        exportCSV.setText("导出CSV");
        exportSQL.setSelected(true);//默认选中导出insert sql
        
        buttonGroup.add(exportEXCEL);
        buttonGroup.add(exportXML);
        buttonGroup.add(exportSQL);
        buttonGroup.add(exportUpdateSQL);
        buttonGroup.add(exportCSV);
        
        //点击【取消】按钮
        cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(hasInstance()){					
					instance.dispose();
				}
			} 	
        });

        //点击【确定】按钮
        okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(exportEXCEL.isSelected()){//导出excel
					exportExcel();
				}else if(exportXML.isSelected()){//导出xml
					exportXml();
				}else if(exportCSV.isSelected()){//导出CSV
					exportCSV();
				}else if(exportUpdateSQL.isSelected()){
					exportUpdateSQL();//导出updateSQL
				}else{
					exportInsertSql();//导出insertSQL
				}
			} 	
        });

        jLabel1.setText("请输入表名称，多个表之间用分号分隔，支持 where 条件过滤：");
        
       
        
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        					.addGap(29)
        					.addComponent(exportEXCEL)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(exportXML)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(exportSQL)
        					.addPreferredGap(ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
        					.addComponent(exportUpdateSQL, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(exportCSV, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
        					.addGap(12))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(163)
        					.addComponent(okButton)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(cancelButton))
        				.addGroup(layout.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE))
        				.addGroup(layout.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 447, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addComponent(jLabel1)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 236, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(exportCSV)
        				.addComponent(exportEXCEL)
        				.addComponent(exportXML)
        				.addComponent(exportSQL)
        				.addComponent(exportUpdateSQL))
        			.addGap(6)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(cancelButton)
        				.addComponent(okButton))
        			.addGap(20))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>
    
    /*
     * 菜单--导出excel
     */
    private void exportExcel() {

		String inputValue = jTextArea1.getText();
		if(inputValue!=null&&!"".equals(inputValue.trim())){

			// 弹出路径选择框
			FileDialog saveDialog = new FileDialog(this, "导出数据库表",FileDialog.SAVE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));//如果不指定时区，在有些机器上会出现时间误差。  
			String currentDateTime = sdf.format(new Date());	
			saveDialog.setFile(currentDateTime+".xls");
			saveDialog.setVisible(true);
			String path = null;
			if (saveDialog.getDirectory() != null) {// 成功点击了确定按钮		
				path = saveDialog.getDirectory() + saveDialog.getFile();
				if (!path.toLowerCase().endsWith(".xls")) {
					path = path + ".xls";
				}
				// 解析指定的数据库表进行备份。
				String tableNames[] = inputValue.split(";");// 分号分隔
				File exportfile = null;
				FileOutputStream out = null;
				boolean isExportSuccess = true;
				try {	
					// 解析需要备份的表
					QueryDataSet backupDataSet = new QueryDataSet(MainFrame.getInstance().getDBUnitConnection());
					for (int i = 0; i < tableNames.length; i++) {
						String tabName = tableNames[i].trim().split(" ")[0];//trim去除空格
						backupDataSet.addTable(tabName,"select * from "+tableNames[i]);
					}
					exportfile = new File(path);
					out = new FileOutputStream(exportfile);
					XlsDataSet.write(backupDataSet, out);
					JOptionPane.showMessageDialog(this, "导出完成！>>>"+path);
				} catch (Exception e) {
					if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
						log.error(null, e);
					}
					JOptionPane.showMessageDialog(this, "出错了！"+e.getMessage());
					isExportSuccess = false;
				}finally{
					if(out!=null){
						try {
							out.close();
						} catch (IOException e) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
								log.error(null, e);
							}
							JOptionPane.showMessageDialog(this, "关闭文件流出错！"+e.getMessage());
						}
					}
					if(!isExportSuccess&&exportfile!=null&&exportfile.exists()){
						exportfile.delete();
					}
				}
			}
		}	
	}
    /*
     * 菜单--导出Xml
     */
    private void exportXml() {
    	String inputValue = jTextArea1.getText();
		if (inputValue != null && !"".equals(inputValue.trim())) {

			FileDialog saveDialog = new FileDialog(this, "导出数据库表",FileDialog.SAVE);// 弹出路径选择框
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));//如果不指定时区，在有些机器上会出现时间误差。  
			String currentDateTime = sdf.format(new Date());	
			saveDialog.setFile(currentDateTime+".xml");
			saveDialog.setVisible(true);
			String path = null;
			if (saveDialog.getDirectory() != null) {// 成功点击了确定按钮
				path = saveDialog.getDirectory() + saveDialog.getFile();
				if (!path.toLowerCase().endsWith(".xml")) {
					path = path + ".xml";
				}
				// 解析指定的数据库表进行备份。
				String tableNames[] = inputValue.split(";");// 分号分隔
				FileOutputStream out = null;
				File exportfile = null;
				boolean isExportSuccess = true;
				try {
					// 解析需要备份的表
					QueryDataSet backupDataSet = new QueryDataSet(MainFrame.getInstance().getDBUnitConnection());
					for (int i = 0; i < tableNames.length; i++) {
						String tabName = tableNames[i].trim().split(" ")[0];
						backupDataSet.addTable(tabName,"select * from "+tableNames[i]);
					}
					exportfile = new File(path);
					out = new FileOutputStream(exportfile);
					FlatXmlDataSet.write(backupDataSet, out);
					JOptionPane.showMessageDialog(this, "导出完成！>>>" + path);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, "出错了！"+e.getMessage());
					isExportSuccess = false;
				}finally{
					if(out!=null){
						try {
							out.close();
						} catch (IOException e) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
								log.error(null, e);
							}
							JOptionPane.showMessageDialog(this, "关闭文件流出错！"+e.getMessage());
						}
						if(!isExportSuccess&&exportfile!=null&&exportfile.exists()){
							exportfile.delete();//出现异常则删除已生成的无效的文件。
						}
					}	
				}
			}
		}
	}
    /*
     * 菜单--导出CSV
     */
    private void exportCSV() {
    	String inputValue = jTextArea1.getText();
		if (inputValue != null && !"".equals(inputValue.trim())) {

			FileDialog saveDialog = new FileDialog(this, "导出数据库表",FileDialog.SAVE);// 弹出路径选择框
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));//如果不指定时区，在有些机器上会出现时间误差。  
			String currentDateTime = sdf.format(new Date());	
			saveDialog.setFile(currentDateTime+".csv");
			saveDialog.setVisible(true);
			String path = null;
			if (saveDialog.getDirectory() != null) {// 成功点击了确定按钮
				path = saveDialog.getDirectory() + saveDialog.getFile();
				if (!path.toLowerCase().endsWith(".csv")) {
					path = path + ".csv";
				}
				// 解析指定的数据库表进行备份。
				String tableNames[] = inputValue.split(";");// 分号分隔
				FileOutputStream out = null;
				File exportfile = null;
				boolean isExportSuccess = true;
				try {
					// 解析需要备份的表
					QueryDataSet backupDataSet = new QueryDataSet(MainFrame.getInstance().getDBUnitConnection());
					for (int i = 0; i < tableNames.length; i++) {
						String tabName = tableNames[i].trim().split(" ")[0];
						backupDataSet.addTable(tabName,"select * from "+tableNames[i]);
					}
					exportfile = new File(path);
					CsvDataSetWriter.write(backupDataSet, exportfile);
					JOptionPane.showMessageDialog(this, "导出完成！>>>" + path);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, "出错了！"+e.getMessage());
					isExportSuccess = false;
				}finally{
					if(!isExportSuccess&&exportfile!=null&&exportfile.exists()){
						exportfile.delete();//出现异常则删除已生成的无效的文件。
					}
				}
			}
		}
	}
    /*
	 * 导出插入sql
	 */
	private void exportInsertSql() {
		
		String inputValue = jTextArea1.getText();
		if (inputValue != null && !"".equals(inputValue.trim())) {
			inputValue = inputValue.trim();

			// 弹出路径选择框
			FileDialog saveDialog = new FileDialog(this, "导出数据库表",FileDialog.SAVE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));//如果不指定时区，在有些机器上会出现时间误差。  
			String currentDateTime = sdf.format(new Date());		
			saveDialog.setFile(currentDateTime+".sql");//设置默认文件名称
			saveDialog.setVisible(true);
			String path = null;
			if (saveDialog.getDirectory() != null) {// 成功点击了确定按钮
				path = saveDialog.getDirectory() + saveDialog.getFile();
				if (!path.toLowerCase().endsWith(".sql")) {
					path = path + ".sql";
				}
				// 解析指定的数据库表.
				PreparedStatement ps = null;
				ResultSet rs = null;
				// 逗号分隔 pub_stru;pub_organ where organ_id='O01';pub_menu_type
				String tableNames[] = inputValue.split(";");
				StringBuilder sqls = new StringBuilder();
				
				//mysql数据库的特殊处理
				String dbType = DBUtil.getDBProductInfo().getProductName();

				for (int i = 0; i < tableNames.length; i++) {
					try {
						ps = ConnUtil.getInstance().getConn().prepareStatement("select * from "+ tableNames[i]);
						rs = ps.executeQuery();
						ResultSetMetaData rm = rs.getMetaData();
						int col_number = rm.getColumnCount();
						StringBuffer insertInit = new StringBuffer();
						if(dbType.contains("MYSQL")){
							insertInit.append("INSERT INTO `" + tableNames[i].trim().split(" ")[0] + "` (");
							for (int j = 1; j <= col_number - 1; j++) {
								insertInit.append("`"+rm.getColumnName(j) + "`, ");
							}
							insertInit.append("`"+rm.getColumnName(col_number)+ "` ) VALUES ( ");
						}else{
							insertInit.append("INSERT INTO " + tableNames[i].trim().split(" ")[0] + " (");
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
							sqls.append(insertSql);//结果集中的每条记录生成一个insert语句
							sqls.append("\n");// 每个sql进行换行		
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
					//执行保存操作
					OutputStreamWriter out = null;
					try {
						out = new OutputStreamWriter(new FileOutputStream(path),"UTF-8");
						out.write(sqls.toString());
						out.flush();
						JOptionPane.showMessageDialog(this, " 导出完成! >>> "+path);
					} catch (Exception e) {
						if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
							log.error(null, e);
						}
						JOptionPane.showMessageDialog(this, " 出错了:  " + e.getMessage());
					} finally {
						try {
							out.close();
						} catch (IOException e) {
							if("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+"")){				
								log.error(null, e);
							}
						}
					}
			}
		}

	}
	/*
	 * 导出更新sql
	 */
	private void exportUpdateSQL() {

		String inputValue = jTextArea1.getText();
		if (inputValue != null && !"".equals(inputValue.trim())) {
			inputValue = inputValue.trim();

			// 弹出路径选择框
			FileDialog saveDialog = new FileDialog(this, "导出数UPDATE SQL",FileDialog.SAVE);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));// 如果不指定时区，在有些机器上会出现时间误差。
			String currentDateTime = sdf.format(new Date());
			saveDialog.setFile(currentDateTime + ".sql");// 设置默认文件名称
			saveDialog.setVisible(true);
			String path = null;
			if (saveDialog.getDirectory() != null) {// 成功点击了确定按钮
				path = saveDialog.getDirectory() + saveDialog.getFile();
				if (!path.toLowerCase().endsWith(".sql")) {
					path = path + ".sql";
				}
				// 解析指定的数据库表.
				PreparedStatement ps = null;
				ResultSet rs = null;
				
				// 分号分隔 pub_str;pub_organ where organ_id='O01';pub_menu_type
				String sqlsInput[] = inputValue.split(";");
				StringBuilder sqls = new StringBuilder();

				// 取得数据库类型
				String dbType = DBUtil.getDBProductInfo().getProductName();
				for (int i = 0; i < sqlsInput.length; i++) {
					try {
						String tableName = sqlsInput[i].trim().split(" ")[0];
						List<String> primaryKeyList = DBUtil.getPrimaryKeyList(tableName,MainFrame.getInstance().getDataSourceInfo().getUsername());
						if(primaryKeyList == null || primaryKeyList.isEmpty()){
							JOptionPane.showMessageDialog(this,"表[" +tableName+"]不存在主键，无法导出UPDATE SQL！");
							return;
						}
						ps = ConnUtil.getInstance().getConn().prepareStatement("select * from " + sqlsInput[i]);
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
							sqls.append(updateSql);// 结果集中的每条记录生成一个update语句
							sqls.append("\n");// 每个sql进行换行
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
				// 执行保存操作
				OutputStreamWriter out = null;
				try {
					out = new OutputStreamWriter(new FileOutputStream(path),"UTF-8");
					out.write(sqls.toString());
					out.flush();
					JOptionPane.showMessageDialog(this, " 导出完成! >>> " + path);
				} catch (Exception e) {
					if ("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG) + "")) {
						log.error(null, e);
					}
					JOptionPane.showMessageDialog(this," 出错了:  " + e.getMessage());
				} finally {
					try {
						out.close();
					} catch (IOException e) {
						if ("true".equals(ConfigUtil.getConfInfo().get(Const.IS_LOG)+ "")) {
							log.error(null, e);
						}
					}
				}
			}
		}

	}
	 /**
     * 查找对话框
     */
    private void showFindReplaceDialog(){
    	jTextArea1.showFindDialog(this);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton okButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane jScrollPane1;
    private MyJextArea jTextArea1 = new MyJextArea(true);
    private javax.swing.JRadioButton exportEXCEL;
    private javax.swing.JRadioButton exportXML;
    private javax.swing.JRadioButton exportSQL;
    private JRadioButton exportCSV ;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
    private JRadioButton exportUpdateSQL; 
}
