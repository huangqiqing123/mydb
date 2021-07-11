package test.tool.gui.dbtool.dialog;

import java.awt.Frame;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import test.tool.gui.common.SysFontAndFace;
import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.frame.MainFrame;
import test.tool.gui.dbtool.image.ImageIcons;
import test.tool.gui.dbtool.util.ConfigUtil;

public class CloseChoice extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	private static CloseChoice instance = null;

	public static CloseChoice getInstance(Frame parent, boolean modal){
		if(instance == null){
			instance = new CloseChoice(parent,modal);
		}
		return instance;
	}
	
	private CloseChoice(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(parent);
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jRadioButton_mini2Tray = new javax.swing.JRadioButton();
        jRadioButton_exit = new javax.swing.JRadioButton();
        jCheckBox_remmber = new javax.swing.JCheckBox();
        jButton_ok = new javax.swing.JButton();
        jButton_cancel = new javax.swing.JButton();
        setTitle("请选择窗口关闭方式");

        jLabel1.setIcon(ImageIcons.ask_jpg); // NOI18N

        jRadioButton_mini2Tray.setText("最小化托盘");
        jRadioButton_exit.setText("退出应用程序");
        jRadioButton_exit.setSelected(true);//默认选中"退出应用程序"
        bg.add(jRadioButton_mini2Tray);
        bg.add(jRadioButton_exit);

        jCheckBox_remmber.setText("记住我的选择");
        jCheckBox_remmber.setFont(SysFontAndFace.font);
        jButton_ok.setText("确定");
        jButton_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	okAction();
            }
        });
        jButton_cancel.setText("取消");
        jButton_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelAction();
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel1)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(65, 65, 65)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        	.add(jRadioButton_exit)
                            .add(jRadioButton_mini2Tray)
                            .add(jCheckBox_remmber))
                        .addContainerGap(158, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton_ok)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton_cancel)
                        .add(62, 62, 62))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .add(jRadioButton_exit)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRadioButton_mini2Tray)
                .add(18, 18, 18)
                .add(jCheckBox_remmber)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_cancel)
                    .add(jButton_ok))
                .add(13, 13, 13))
            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 159, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>

   private void cancelAction() {
		this.dispose();
		
	}
   private void okAction() {
	  
		// 是否选择了记住用户的选择,（1：关闭，2：最小化到托盘，3：弹出供用户选择）
	   String action = null;
	   if(jCheckBox_remmber.isSelected()){
		   if (jRadioButton_exit.isSelected()) {
			   action = "1";
		   } else if (jRadioButton_mini2Tray.isSelected()) {
			   action = "2";
		   } else {
				JOptionPane.showMessageDialog(this, "出错了，无效的关闭类型!");
			}
	   }else{
		   action = "3";
	   }
	   ConfigUtil.getConfInfo().put(Const.CLOSE_ACTION, action);
	   ConfigUtil.updateConfInfo();

	   
		// 退出应用程序，还是最小化托盘
		if (jRadioButton_exit.isSelected()) {
			System.exit(0);
		} else if (jRadioButton_mini2Tray.isSelected()) {//注意先后顺序，先this.dispose(); 然后parent.setVisible(false);
			this.dispose();
			this.getOwner().setVisible(false);
		} else {
			JOptionPane.showMessageDialog(this, "出错了，无效的关闭类型!");
		}
	}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_cancel;
    private javax.swing.JButton jButton_ok;
    private javax.swing.JCheckBox jCheckBox_remmber;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JRadioButton jRadioButton_exit;
    private javax.swing.JRadioButton jRadioButton_mini2Tray;
    private ButtonGroup bg = new ButtonGroup();
    // End of variables declaration//GEN-END:variables

}
