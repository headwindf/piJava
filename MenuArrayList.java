import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class MenuArrayList extends ArrayList<MenuItem>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public MenuItem remove(int index) {
		// TODO Auto-generated method stub
		
		return super.remove(index);
	}
	
	public MenuItem remove(int index,DefaultTableModel model) {
		// TODO Auto-generated method stub
		model.removeRow(index);
		return super.remove(index);
	}
	
	@Override
	public boolean add(MenuItem e) {
		// TODO Auto-generated method stub
		return super.add(e);
	}
	
	public void add(MenuItem e,DefaultTableModel model) {
		// TODO Auto-generated method stub
		Vector row = new Vector(); 
		Vector data = new Vector();
		row.add(e.getId());
		row.add(e.getItemName());
		row.add(e.getValue());
		data.add(row.clone());
		model.addRow(row);
	}
	
	public void remove(DefaultTableModel model) {
		// TODO Auto-generated method stub
		for (int row = model.getRowCount()-1; row >= 0; row--) {
			model.removeRow(row);
		}
	}
	
	@Override
	public void add(int index, MenuItem element) {
		// TODO Auto-generated method stub
		super.add(index, element);
	}
	
	
}
