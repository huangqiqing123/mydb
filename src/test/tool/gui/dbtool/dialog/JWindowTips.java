package test.tool.gui.dbtool.dialog;

import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import test.tool.gui.common.SysFontAndFace;
import test.tool.gui.dbtool.frame.MainFrame;
import test.tool.gui.dbtool.mycomponent.MyJlistCellRenderer;

public class JWindowTips extends javax.swing.JWindow {

	private static final long serialVersionUID = 1L;
    public javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    
	public JWindowTips(MainFrame instance) {
		super(instance);
		initComponents();
	}
	private void initComponents() {

        jScrollPane1 = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jList1 = new javax.swing.JList();
        getContentPane().setLayout(new BorderLayout());

        jList1.setCellRenderer(new MyJlistCellRenderer());
        jList1.setModel(new DefaultListModel());
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//设置只允许单选
        jList1.setFont(SysFontAndFace.font14);
        jScrollPane1.setViewportView(jList1);
        getContentPane().add(jScrollPane1,BorderLayout.CENTER);  
        pack();   
        this.setSize(400,300);//该行代码必须放在pack()之后，否则不生效。
    }
}
