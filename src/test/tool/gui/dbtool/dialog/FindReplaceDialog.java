package test.tool.gui.dbtool.dialog;

import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import test.tool.gui.common.SysFontAndFace;
import test.tool.gui.dbtool.mycomponent.MyJTextField;


public class FindReplaceDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	private javax.swing.JButton jButton_cancel;
    private javax.swing.JButton jButton_findNext;
    private javax.swing.JButton jButton_replace;
    private javax.swing.JButton jButton_replaceAll;
    private javax.swing.JCheckBox jCheckBox_case;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private MyJTextField jTextField_find;
    private MyJTextField jTextField_replace;
    
    private JTextArea textArea = null;
    private int  findIndex = 0 ;

    public FindReplaceDialog(java.awt.Frame parent, JTextArea textArea) {
        super(parent, false);
        initComponents();
        
        this.textArea = textArea;
        this.findIndex = 0;
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);//隐藏窗口并释放资源

		if(textArea.isEditable()){
			this.jButton_replace.setEnabled(true);
			this.jButton_replaceAll.setEnabled(true);
		}else{
			this.jButton_replace.setEnabled(false);
			this.jButton_replaceAll.setEnabled(false);
		}
		//如果textArea中有选中文本，则将选中文本作为默认的查询内容。
		String selectedText = textArea.getSelectedText();
		if(selectedText != null){
			jTextField_find.setText(selectedText);
		}
    }
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField_find = new MyJTextField();
        jTextField_replace = new MyJTextField();
        jCheckBox_case = new javax.swing.JCheckBox();
        jButton_findNext = new javax.swing.JButton();
        jButton_replace = new javax.swing.JButton();
        jButton_replaceAll = new javax.swing.JButton();
        jButton_cancel = new javax.swing.JButton();

        setTitle("查找/替换");
        jLabel1.setText("查找内容：");
        jLabel2.setText("替换为：");
        jCheckBox_case.setText("区分大小写");
        jCheckBox_case.setSelected(true);//默认是大小写敏感的
        jCheckBox_case.setFont(SysFontAndFace.font);
        
        jButton_findNext.setText("查找下一个");
        jButton_replace.setText("替换");
        jButton_replaceAll.setText("全部替换");
        jButton_cancel.setText("取消");
        jButton_findNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	String str = textArea.getSelectedText();
                if(str==null||"".equals(str))
                {
                    findIndex=0;
                }
            	find(jTextField_find.getText(),findIndex);
            }
        });
        jButton_replace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	 String str = textArea.getSelectedText();
            	 if(str!=null&&!"".equals(str)){
                	 textArea.replaceSelection(jTextField_replace.getText());
            	 }  
            }
        });
        jButton_replaceAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {  	
            	count = 0;
                replaceAll(jTextField_find.getText(),jTextField_replace.getText(), 0 ,textArea.getText().length());
            }
        });
        jButton_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	close();
            }
        });
        
        //为 jTextField_find 设置键盘监听事件，回车执行 【查找下一个】
        jTextField_find.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
            	if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                	String str = textArea.getSelectedText();
                    if(str==null||"".equals(str))
                    {
                        findIndex=0;
                    }
                	find(jTextField_find.getText(),findIndex);         
        		} 
            }
        });   
//-------------------布局信息-----------------
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(21, 21, 21)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jCheckBox_case)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 215, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jButton_replaceAll)
                            .add(jButton_cancel)))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(layout.createSequentialGroup()
                                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jTextField_replace))
                            .add(layout.createSequentialGroup()
                                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jTextField_find, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 178, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 30, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jButton_findNext)
                            .add(jButton_replace))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jTextField_find, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton_findNext))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(jTextField_replace, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton_replace))
                .add(1, 1, 1)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(28, 28, 28)
                        .add(jCheckBox_case))
                    .add(layout.createSequentialGroup()
                        .add(9, 9, 9)
                        .add(jButton_replaceAll)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton_cancel)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
 /*
  *  查找 
  */
    public void find(String str, int cur) {
    	
    	int i = -1;
    	String textAreaText = textArea.getText();
    	if(!jCheckBox_case.isSelected()){//如果大小写不敏感
    		
    		i = textAreaText.toUpperCase().indexOf(str.toUpperCase(),cur);
    	
    	}else{//如果大小写敏感
    	
    		i = textAreaText.indexOf(str,cur);
    	}
		if (i >= 0) {
			textArea.setSelectionStart(i); // 使找到的字符串选中
			textArea.setSelectionEnd(i + str.length());
			findIndex = ++i; // 用于查找下一个
		} else {
			if(findIndex == 0){
				return;
			}else{			
				//查询完毕，重新重头开始查找
				findIndex = 0;
				find(str, findIndex);
			}
		}
	} 
   /*
    *  替换全部 
    */
    int count = 0;
    public void replaceAll(String fromStr, String toStr, int cur, int end) {
    	
		if (cur > end) {
			return;
		} else {
			int i = -1;
			String textAreaText = textArea.getText();
			if (!jCheckBox_case.isSelected()) {// 如果大小写不敏感
				i = textAreaText.toUpperCase().indexOf(fromStr.toUpperCase(),
						cur);
			} else {
				i = textAreaText.indexOf(fromStr, cur);
			}
			if (i >= 0) {
				textArea.setSelectionStart(i); // 使找到的字符串反白选中
				textArea.setSelectionEnd(i + fromStr.length());
				textArea.replaceSelection(toStr); // 替换
				cur = ++i;
				count++;
			} else {
				JOptionPane.showMessageDialog(this, " 替换完毕，共替换  " + count + " 处！");
				return;
			}
			replaceAll(fromStr, toStr, cur, end); // 递归查找与替换
		}
	} 
    private void close(){
    	this.dispose();
    }
}
