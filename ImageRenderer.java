import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class ImageRenderer extends DefaultTableCellRenderer {
	DefaultTableCellRenderer renderer=new DefaultTableCellRenderer(); 
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,   
	        boolean isSelected, boolean hasFocus, int row, int column) {   
	    if(column==2&&row==3){
	        return new JLabel(new ImageIcon("img/pic1.png"));
	    }else{
	        return super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
	    }
	}   
	    
}
