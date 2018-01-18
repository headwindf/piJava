/**
* 自定义的表格，继承JTable，重写方法使其不能被编辑
* date：2018.1.16
* @author headwind
* @version V0.0
*/

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MenuJTable extends JTable{
	@Override
	public boolean isCellEditable(int row, int column) {
	     return false;
	}
	
	public MenuJTable(DefaultTableModel model) {
		super(model);
	}
       
}
