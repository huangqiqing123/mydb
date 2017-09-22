package test.tool.gui.dbtool.dialog;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import test.tool.gui.dbtool.frame.MainFrame;
import test.tool.gui.dbtool.frame.MyNotePad;
import test.tool.gui.dbtool.image.ImageIcons;
import test.tool.gui.dbtool.mycomponent.MyJTextField;

public class FilterDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	MainFrame mainFrame = null;
	
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel_column;
    private javax.swing.JLabel jLabel_condition;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private MyJTextField jTextField_condition;
    
	private static FilterDialog instance = null;
	private static JTable jtable = null;
	
	/*
	 * 刷新Jlist
	 */
	public static void refreshJlist(){
		if(instance != null){
			jtable = instance.mainFrame.getCurrentJtable();
			if(jtable != null){			
				if(jtable.getRowSorter()!=null){//当Jtable中无数据时，jtable.getRowSorter()是null。				
				
					DefaultListModel defaultListModel = (DefaultListModel) instance.jList1.getModel();
					defaultListModel.removeAllElements();
					
					TableColumnModel columnModel = jtable.getTableHeader().getColumnModel();
					int columnCount = columnModel.getColumnCount();
					
					defaultListModel.addElement("所有列");
					for(int i = 1;i<columnCount;i++){
						defaultListModel.addElement(columnModel.getColumn(i).getHeaderValue());
					}
					instance.jList1.setSelectedIndex(0);//默认选中“所有列”
				}
			}else{
				((DefaultListModel)instance.jList1.getModel()).removeAllElements();
			}	
		}
	}
	/*
	 * 单例模式
	 */
	public static FilterDialog getInstance(MainFrame parent, boolean modal){
		if(instance == null){
			instance = new FilterDialog(parent,modal);
		}
		refreshJlist();
		return instance;
	}

	/*
	 * 私有构造函数
	 */
    private FilterDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        mainFrame = (MainFrame)parent;
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        
        //设置标题栏图标 
        this.setIconImage(ImageIcons.ico_png.getImage());
        initComponents();
    }

    /*
     * 初始化组件
     */
    private void initComponents() {
    	
    	setTitle("过滤对话框");
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jList1.setModel(new DefaultListModel());
        jLabel_column = new javax.swing.JLabel();
        jTextField_condition = new MyJTextField();
        jLabel_condition = new javax.swing.JLabel();
        jLabel_condition.setIcon(ImageIcons.help_png);
        jLabel_condition.setToolTipText("点击查看使用说明");
        jLabel_condition.addMouseListener(new MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	StringBuilder shuoMing = new StringBuilder();
                shuoMing.append("支持正则表达式：");
                shuoMing.append("\n");
                shuoMing.append("1: 两者取一“|” Java|Hello  Java或Hello"); 
                shuoMing.append("\n");
                shuoMing.append("2: ^为限制开头   ^java 条件限制为以Java为开头字符"); 
                shuoMing.append("\n");
                shuoMing.append("3: $为限制结尾   java$ 条件限制为以java为结尾字符");
                shuoMing.append("\n");
                shuoMing.append("4: J*  0个以上J");
                shuoMing.append("\n");
                shuoMing.append("5: .*  0个以上任意字符");
                shuoMing.append("\n");
                shuoMing.append("更多正则表达式规则请参阅相关资料！");
                MyNotePad notePad = new MyNotePad(instance,"说明",shuoMing.toString(),null);
                notePad.setVisible(true);
            }
            public void mouseEntered(MouseEvent e){//鼠标进入
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
               }
             public  void mouseExited(MouseEvent e){//鼠标移出
                setCursor(Cursor.getDefaultCursor());
              }
        });
        jButton1 = new javax.swing.JButton();
        jButton1.setIcon(ImageIcons.ok_png);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jScrollPane1.setViewportView(jList1);
        jLabel_column.setText("选择列(Ctrl多选)：");
        
        //为按钮添加事件
        jButton1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				doFilter();
			}	
        });
        //为输入框添加键盘事件
        jTextField_condition.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e) {
				doFilter();
			}
        });
        //为输入框添加鼠标双击事件，双击则清除查询条件
        jTextField_condition.setToolTipText("双击可清空本文本框内容");
        jTextField_condition.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2){
					jTextField_condition.setText("");
					doFilter();
				}
				}
		});
        
        //布局信息
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel_column)
                            .add(jTextField_condition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jButton1, 0, 0, Short.MAX_VALUE)
                            .add(jLabel_condition, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField_condition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel_column)
                    .add(jLabel_condition))
                .add(4, 4, 4)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>
    
    /*
     * 执行过滤操作
     */
    private void doFilter(){	
		
    	JTable jtable = mainFrame.getCurrentJtable();
		if(jtable != null){			
			String text = jTextField_condition.getText();	
			if(text==null || text.length()==0){
				((TableRowSorter<TableModel>)jtable.getRowSorter()).setRowFilter(null);
			}else{
				if(jtable.getRowSorter()!=null){//当Jtable中无数据时，jtable.getRowSorter()是null。		
					
					//解析选择的列
					int [] selectedCols  = jList1.getSelectedIndices();
					
					for (int i = 0; i < selectedCols.length; i++) {//如果选项中包含“所有列”，则按照所有列进行过滤，无需传递列信息。
						if(selectedCols[i] == 0){				
							((TableRowSorter<TableModel>)jtable.getRowSorter()).setRowFilter(RowFilter.regexFilter(text));
							return;
						}
					}
					((TableRowSorter<TableModel>)jtable.getRowSorter()).setRowFilter(RowFilter.regexFilter(text,selectedCols));
				}
			}
		}	
    }
}
