package FileManagement;

import java.io.File;

import javax.swing.table.AbstractTableModel;

class FileTableModel extends AbstractTableModel {
	private File[] files;
	private final String[] columnNames = { "Name" };

	public void updateData(File directory) {
		files = directory.listFiles();
		fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return (files != null) ? files.length : 0;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (files != null && rowIndex < files.length) {
			return new FileTableEntry(files[rowIndex]);
		}
		return null;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public FileTableEntry getFileAt(int rowIndex) {
		if (files != null && rowIndex < files.length) {
			return new FileTableEntry(files[rowIndex]);
		}
		return null;
	}
}