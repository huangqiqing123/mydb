
package test.tool.gui.dbtool.dialog;

import java.awt.Frame;

import javax.swing.WindowConstants;

import test.tool.gui.dbtool.image.ImageIcons;

public class ConnectDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	private javax.swing.JLabel jLabel1;
	private static ConnectDialog instance = null;

	public static ConnectDialog getInstance(Frame parent, boolean modal){
		if(instance == null){
			instance = new ConnectDialog(parent,modal);
		}
		return instance;
	}
	
    private ConnectDialog(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        this.setTitle("联系我们");
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(parent);
       
        //设置标题栏图标 
        this.setIconImage(ImageIcons.ico_png.getImage());
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jLabel1.setIcon(ImageIcons.show_png); 
        getContentPane().add(jLabel1, java.awt.BorderLayout.CENTER);
        pack();
    }

}
