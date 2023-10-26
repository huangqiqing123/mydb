package test.tool.gui.dbtool.dialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import test.tool.gui.common.SysFontAndFace;
import test.tool.gui.dbtool.consts.Const;
import test.tool.gui.dbtool.frame.MainFrame;
import test.tool.gui.dbtool.mycomponent.MyJTable;
import test.tool.gui.dbtool.util.ConfigUtil;

public class SetDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	private MainFrame parent = null;
	private static SetDialog instance = null;

	public static SetDialog getInstance(MainFrame parent, boolean modal){
		if(instance == null){
			instance = new SetDialog(parent,modal);
		}	
		instance.myInit();	
		return instance;
	}

    private SetDialog(MainFrame parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(parent);
    }

    private void initComponents() {

    	//windowClosing事件：当用户点击窗口右上角的关闭按钮时触发。
    	this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent arg0) {
				String action = null;
				if(jRadioButton_exit.isSelected()){
					action = "1";
				}else if(jRadioButton_miniTray.isSelected()){
					action = "2";
				}else if(jRadioButton_userChoose.isSelected()){
					action = "3";
				}else{
					action = "3";
				}
				 ConfigUtil.getConfInfo().put(Const.CLOSE_ACTION, action);
				 ConfigUtil.updateConfInfo();
		}});
    	
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jRadioButton_exit = new javax.swing.JRadioButton();
        jRadioButton_miniTray = new javax.swing.JRadioButton();
        jRadioButton_userChoose = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jSlider_height = new javax.swing.JSlider();
        jPanel4 = new javax.swing.JPanel();
        jCheckBox_generateSQL = new javax.swing.JCheckBox();
        jCheckBox_generateSQL.setFont(SysFontAndFace.font);//设置选框字体
        jCheckBox_log.setFont(SysFontAndFace.font);//设置选框字体

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("其他设置");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("关闭按钮行为设置"));

        jRadioButton_exit.setText("退出程序");
        jRadioButton_miniTray.setText("最小化托盘");
        jRadioButton_userChoose.setText("用户选择");
        buttonGroup1.add(jRadioButton_exit);
        buttonGroup1.add(jRadioButton_miniTray);
        buttonGroup1.add(jRadioButton_userChoose);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(jRadioButton_exit)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jRadioButton_miniTray)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jRadioButton_userChoose)
                .addContainerGap(215, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jRadioButton_exit)
                    .add(jRadioButton_miniTray)
                    .add(jRadioButton_userChoose))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("列表行高设置"));

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(93, 93, 93)
                .add(jSlider_height, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(179, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .add(jSlider_height, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(20, 20, 20))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("其他"));
        jCheckBox_generateSQL.setText(" 树中操作是否同步生成SQL");

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(91, 91, 91)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox_log, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jCheckBox_generateSQL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(212, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jCheckBox_generateSQL)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 15, Short.MAX_VALUE)
                .add(jCheckBox_log))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        //滑块初始化设置
        jSlider_height.setMaximum(35);
        jSlider_height.setMinimum(15);
        jSlider_height.setPaintLabels(true);//是否在滑块上绘制标签。
        jSlider_height.setPaintTicks(true);//是否在滑块上绘制刻度标记
        jSlider_height.setMajorTickSpacing(10);//设置主刻度标记的间隔
        jSlider_height.setMinorTickSpacing(1);//设置次刻度标记的间隔
        jSlider_height.setEnabled(true);
        jSlider_height.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent evt) {
        		JSlider slider = (JSlider)evt.getSource();                
        		int value = slider.getValue();
        		List<MyJTable> tableList = parent.getJtables();
        		for(int i=0;i<tableList.size();i++){
        			tableList.get(i).setRowHeight(value);
        		}
        		//更新配置信息
        		ConfigUtil.getConfInfo().put(Const.JTABLE_LINE_HEIGHT, value);
        		ConfigUtil.updateConfInfo();
        	}
        });
        //自动生成sql设置监听事件
        jCheckBox_generateSQL.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				
				//更新配置信息
				 ConfigUtil.getConfInfo().put(Const.IS_GENERATE_SQL, jCheckBox_generateSQL.isSelected());
				 ConfigUtil.updateConfInfo();
			}   	
        });
        //是否启用日志
        jCheckBox_log.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
				
				//更新配置信息
				 ConfigUtil.getConfInfo().put(Const.IS_LOG, jCheckBox_log.isSelected());
				 ConfigUtil.updateConfInfo();
			}   	
        });
        
        pack();    
    }// </editor-fold>

    /*
     * 初始化业务信息
     */
    private void myInit(){
    	
    	//关闭按钮行为设置
    	Object close_action =  ConfigUtil.getConfInfo().get(Const.CLOSE_ACTION);
    	if("2".equals(close_action+"")){
    		jRadioButton_miniTray.setSelected(true);
    	}else if("1".equals(close_action+"")){
    		jRadioButton_exit.setSelected(true);
    	}else{	
    		jRadioButton_userChoose.setSelected(true);
    	}
    	
    	//列表行高设置
    	jSlider_height.setValue(Integer.parseInt( ConfigUtil.getConfInfo().get(Const.JTABLE_LINE_HEIGHT).toString()));//设置滑块的默认值
    	 
    	//自动生成SQL设置
	    Object is_generate_sql =  ConfigUtil.getConfInfo().get(Const.IS_GENERATE_SQL);
	    if("true".equals(is_generate_sql+"")){
	    	jCheckBox_generateSQL.setSelected(true);
	    }else{
	    	jCheckBox_generateSQL.setSelected(false);
	    }
	  //是否启用日志 设置
	    Object is_log =  ConfigUtil.getConfInfo().get(Const.IS_LOG);
	    if("true".equals(is_log+"")){
	    	jCheckBox_log.setSelected(true);
	    }else{
	    	jCheckBox_log.setSelected(false);
	    }
    }

    // Variables declaration - do not modify
    private javax.swing.ButtonGroup buttonGroup1;
    private JCheckBox jCheckBox_generateSQL;
    private JCheckBox jCheckBox_log = new JCheckBox(" 启用日志");
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton_exit;
    private javax.swing.JRadioButton jRadioButton_miniTray;
    private javax.swing.JRadioButton jRadioButton_userChoose;
    private javax.swing.JSlider jSlider_height;
    // End of variables declaration

}
