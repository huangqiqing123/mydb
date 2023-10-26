package test.tool.gui.dbtool.mycomponent;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;

/*
 * 实现接口ListCellRenderer，用于支持在Jlist中显示图标和文字
 */
public class MyJlistCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;

	public MyJlistCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		if (value != null) {
			Object[] obja = (Object[]) value;
			setIcon((Icon) obja[0]);
			setText(obja[1].toString());
		}
		this.setBorder(BorderFactory.createLineBorder(Color.white));// 加边框是为了增加间距，好看。

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			// 设置选取与取消选取的前景与背景颜色.
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;
	}
}
