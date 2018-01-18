/**
* 表头样式-重写方法-空表头
* date：2018.1.16
* @author headwind
* @version V0.0
*/
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class HeadRenderer implements TableCellRenderer {
	private TableCellRenderer parent;
	private Border emptyBorder = BorderFactory.createEmptyBorder();
	public HeadRenderer(TableCellRenderer parent) {
		this.parent = parent;
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel headerLabel = (JLabel) parent.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		headerLabel.setBorder(emptyBorder);
		return headerLabel;
	}
}