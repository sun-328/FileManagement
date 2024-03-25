package FileManagement;

import java.io.File;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

class FileTableEntry {
	private File file;

	public FileTableEntry(File file) {
		this.file = file;
	}

	public String getName() {
		return file.getName();
	}

	public Icon getIcon() {
		return FileSystemView.getFileSystemView().getSystemIcon(file);
	}

	public File getFile() {
		return file;
	}
}