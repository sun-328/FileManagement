package FileManagement;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

class FileTableCellRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (value instanceof FileTableEntry) {
			FileTableEntry entry = (FileTableEntry) value;
			label.setIcon(entry.getIcon());
			label.setText(entry.getName());
		}
		return label;
	}
}