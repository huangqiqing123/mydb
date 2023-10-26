package test.tool.gui.dbtool.mycomponent;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel{
	
	private static final long serialVersionUID = 1L;

	public MyTableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
	}

	/*
	 * 根据每列的第一个值来返回每列真实的数据类型
	 * 如不重写，默认始终返回return Object.class;
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		
		Class<? extends Object> returnValue = null;	
		try {
			returnValue = getValueAt(0, columnIndex).getClass();
		} catch (Exception e) {
			returnValue = Object.class;
		}
		return returnValue;
	}

	
}
