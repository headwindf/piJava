import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.plaf.TableUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class MenuJTable extends JTable{
	@Override
	public boolean isCellEditable(int row, int column) {
	     return false;
	}
	
	public MenuJTable(DefaultTableModel model) {
		super(model);
	}
       
}
