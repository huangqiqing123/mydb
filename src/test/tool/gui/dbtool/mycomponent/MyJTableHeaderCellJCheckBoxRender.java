package test.tool.gui.dbtool.mycomponent;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MyJTableHeaderCellJCheckBoxRender extends JCheckBox implements TableCellRenderer{

	private static final long serialVersionUID = 1L;

	public MyJTableHeaderCellJCheckBoxRender () {
		this.setBorderPainted(true);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		return this;
	}

}
