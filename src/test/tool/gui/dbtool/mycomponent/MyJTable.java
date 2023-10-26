package test.tool.gui.dbtool.mycomponent;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

public class MyJTable extends JTable {

	private static final long serialVersionUID = 1L;
	
	//新增2个属性,分别保存单元格变化时的value值
	public Object newValue = null;
	public Object oldValue = null;

	/**
	 * 构造函数
	 * @param dm
	 */
	public MyJTable(TableModel dm) {
		super(dm);
	}
	/**
	 * 新增1方法，检查结束编辑时，单元格的值是否发生了变化
	 * @return
	 */
	public boolean isCellValueChanged(){
		if(newValue == oldValue){
			return false;
		}
		if((newValue+"").trim().equals((oldValue+"").trim())){
			return false;
		}
		return true;
	}

	@Override
	public void editingStopped(ChangeEvent e) {
		
		//新值、旧值
		 TableCellEditor editor = getCellEditor();
	        if (editor != null) {
	            newValue = editor.getCellEditorValue();
	            oldValue = getValueAt(editingRow, editingColumn);
	        }
		super.editingStopped(e);
	}

	
}
