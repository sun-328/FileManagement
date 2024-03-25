
package FileManagement;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeSet;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;


public class FileOperationManager {
	private File currentDirectory;
	private Stack<File> directoryStack;
	private Queue<FileOperation> fileOperationQueue;
	private Map<String, Object> settingsMap;
	private TreeSet<FileTableEntry> fileSet;

	public FileOperationManager() {
		this.currentDirectory = new File(System.getProperty("user.home"));
		this.directoryStack = new Stack<>();
		this.fileOperationQueue = new LinkedList<>();
		this.settingsMap = new HashMap<>();
		this.fileSet = new TreeSet<>(Comparator.comparing(FileTableEntry::getName));
	}
	
	
	
	public void listFilesAndDirectories(JTable fileTable) {
		((FileTableModel) fileTable.getModel()).updateData(currentDirectory);
	}
	
	
	public void deleteDirectory(JFrame frame , JTable fileTable) {
	    int selectedRow = fileTable.getSelectedRow();
	    if (selectedRow >= 0) {
	        FileTableEntry selectedEntry = ((FileTableModel) fileTable.getModel()).getFileAt(selectedRow);
	        File selectedFile = selectedEntry.getFile();

	        int choice = JOptionPane.showConfirmDialog(frame,
	                "Are you sure you want to delete the directory: " + selectedFile.getName(),
	                "Confirm Delete", JOptionPane.YES_NO_OPTION);

	        if (choice == JOptionPane.YES_OPTION) {
	            if (deleteDirectoryRecursive(selectedFile)) {
	            	listFilesAndDirectories(fileTable); // Refresh the file list
	            } else {
	                JOptionPane.showMessageDialog(frame, "Failed to delete the directory.", "Error",
	                        JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    }
	}
	
	public void deleteFile(JFrame frame, JTable fileTable) {
	    int selectedRow = fileTable.getSelectedRow();
	    if (selectedRow >= 0) {
	        FileTableEntry selectedEntry = ((FileTableModel) fileTable.getModel()).getFileAt(selectedRow);
	        File selectedFile = selectedEntry.getFile();

	        int choice = JOptionPane.showConfirmDialog(frame,
	                "Are you sure you want to delete the file: " + selectedFile.getName(),
	                "Confirm Delete", JOptionPane.YES_NO_OPTION);

	        if (choice == JOptionPane.YES_OPTION) {
	        	if (deleteFile(selectedFile)) {
	        		listFilesAndDirectories(fileTable); // Refresh the file list
	        	} else {
	        	    JOptionPane.showMessageDialog(frame, "Failed to delete the file.", "Error", JOptionPane.ERROR_MESSAGE);
	        	}
	        }
	    }
	}
	
	public void showRenameDialog(JFrame frame, JTable fileTable) {
	    int selectedRow = fileTable.getSelectedRow();
	    if (selectedRow >= 0) {
	        FileTableEntry selectedEntry = ((FileTableModel) fileTable.getModel()).getFileAt(selectedRow);
	        File selectedFile = selectedEntry.getFile();

	        String newName = JOptionPane.showInputDialog(frame, "Enter New Name:");
	        if (newName != null) {
	            if (selectedFile.renameTo(new File(currentDirectory, newName))) {
	            	listFilesAndDirectories(fileTable); // Refresh the file list
	            } else {
	                JOptionPane.showMessageDialog(frame, "Failed to rename.", "Error",
	                        JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    }
	}
	
	public void showPropertiesDialog(JFrame frame, JTable fileTable) {
	    int selectedRow = fileTable.getSelectedRow();
	    if (selectedRow >= 0) {
	        FileTableEntry selectedEntry = ((FileTableModel) fileTable.getModel()).getFileAt(selectedRow);
	        File selectedFile = selectedEntry.getFile();

	        // Gather properties information here (e.g., file name, size, type)
	        String fileName = selectedFile.getName();
	        long fileSize = selectedFile.length(); // Size in bytes
	        String fileType = "File"; // You can determine if it's a file or directory
	        if (selectedFile.isDirectory()) {
	            fileType = "Directory";
	        }

	        // Create a message to display properties
	        String message = "Name: " + fileName + "\n" +
	                         "Type: " + fileType + "\n" +
	                         "Size: " + fileSize + " bytes";

	        JOptionPane.showMessageDialog(frame, message, "Properties", JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	
	public void navigateToParentDirectory(JTable fileTable,JLabel currentPathLabel) {
		File parentDir = currentDirectory.getParentFile();
		if (parentDir != null) {
			currentDirectory = parentDir;
			updateCurrentPathLabel(currentPathLabel);
			listFilesAndDirectories(fileTable);
		}
	}
	
	public void updateCurrentPathLabel(JLabel currentPathLabel) {
		currentPathLabel.setText("Current Path: " + currentDirectory.getAbsolutePath());
	}
	
	public void navigateBack(JTable fileTable,JLabel currentPathLabel) {
		if (!getDirectoryStack().isEmpty()) {
			currentDirectory = popDirectoryStack();
			updateCurrentPathLabel(currentPathLabel);
			listFilesAndDirectories(fileTable);
		}
	}
	
	public void showCreateDirectoryDialog(JFrame frame, JTable fileTable) {
	    String newDirectoryName = JOptionPane.showInputDialog(frame, "Enter Directory Name:");
	    if (newDirectoryName != null) {
	        File newDirectory = new File(currentDirectory, newDirectoryName);
	        if (!newDirectory.exists()) {
	            if (newDirectory.mkdir()) {
	            	listFilesAndDirectories(fileTable); // Refresh the file list
	            } else {
	                JOptionPane.showMessageDialog(frame, "Failed to create the directory.", "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        } else {
	            JOptionPane.showMessageDialog(frame, "Directory already exists.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
	
	public void showCreateFileDialog(JFrame frame, JTable fileTable) {
	    String newFileName = JOptionPane.showInputDialog(frame, "Enter File Name:");
	    if (newFileName != null) {
	        File newFile = new File(currentDirectory, newFileName);
	        if (!newFile.exists()) {
	            try {
	                if (newFile.createNewFile()) {
	                	listFilesAndDirectories(fileTable); // Refresh the file list
	                } else {
	                    JOptionPane.showMessageDialog(frame, "Failed to create the file.", "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	                JOptionPane.showMessageDialog(frame, "Error creating the file.", "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        } else {
	            JOptionPane.showMessageDialog(frame, "File already exists.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
	
	public void navigateDirectories(JTable fileTable,JLabel currentPathLabel) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(currentDirectory);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			pushDirectoryStack(currentDirectory); // Save current directory
			currentDirectory = fileChooser.getSelectedFile();
			updateCurrentPathLabel(currentPathLabel);
			listFilesAndDirectories(fileTable);
		}
	}
	
	public void addDoubleClickNavigation(JTable fileTable,JLabel currentPathLabel) {
		fileTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = fileTable.getSelectedRow();
					if (row >= 0) {
						FileTableEntry entry = ((FileTableModel) fileTable.getModel()).getFileAt(row);
						if (entry.getFile().isDirectory()) {
							navigateToDirectory(entry.getFile(),fileTable,currentPathLabel);
						} else if (entry.getFile().isFile()) {
							openFile(entry.getFile());
						}
					}
				}
			}
		});
	}
	
	public void navigateToDirectory(File directory,JTable fileTable, JLabel currentPathLabel) {
		pushDirectoryStack(currentDirectory); // Save current directory
		currentDirectory = directory;
		updateCurrentPathLabel(currentPathLabel);
		listFilesAndDirectories(fileTable);
	}
	private void openFile(File file) {
		try {
			// Determine the file type and use the appropriate application to open it
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			e.printStackTrace();
			// Handle any exceptions or errors that occur during file opening
		}
	}
	// Implement other file-related operations

	public void executeFileOperation(FileOperation fileOperation) {
		fileOperation.execute(this);
	}

	public File getCurrentDirectory() {
		return currentDirectory;
	}

	public Stack<File> getDirectoryStack() {
		return directoryStack;
	}

	public Queue<FileOperation> getFileOperationQueue() {
		return fileOperationQueue;
	}

	public Map<String, Object> getSettingsMap() {
		return settingsMap;
	}

	public TreeSet<FileTableEntry> getFileSet() {
		return fileSet;
	}

	// Additional methods for specific operations

	public void createDirectory(String directoryName) {
		File newDirectory = new File(currentDirectory, directoryName);
		try {
			if (newDirectory.exists()) {
				System.out.println("Directory already exists.");
			} else if (newDirectory.mkdir()) {
				System.out.println("Directory created successfully.");
			} else {
				System.out.println("Failed to create directory.");
			}
		} catch (SecurityException e) {
			System.out.println("Permission denied to create directory.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	boolean deleteDirectoryRecursive(File directoryToDelete) {
	    if (directoryToDelete.isDirectory()) {
	        File[] files = directoryToDelete.listFiles();
	        if (files != null) {
	            for (File subFile : files) {
	                if (!deleteDirectoryRecursive(subFile)) {
	                    return false;
	                }
	            }
	        }
	    }
	    return directoryToDelete.delete();
	}
	


	public void createFile(String fileName) {
		File newFile = new File(currentDirectory, fileName);

		try {
			if (newFile.createNewFile()) {
				System.out.println("File created successfully: " + newFile.getAbsolutePath());
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred while creating the file: " + e.getMessage());
		} catch (SecurityException e) {
			System.out.println("Permission denied to create the file.");
		}
	}
	
	public boolean deleteFile(File fileToDelete) {
	    if (fileToDelete.exists() && fileToDelete.isFile()) {
	        if (fileToDelete.delete()) {
	            System.out.println("File deleted successfully.");
	            return true; // Return true for successful deletion
	        } else {
	            System.out.println("Failed to delete file.");
	            return false; // Return false for deletion failure
	        }
	    } else {
	        System.out.println("File not found.");
	        return false; // Return false if file doesn't exist
	    }
	}
	
	public void pushDirectoryStack(File directory) {
		directoryStack.push(directory);
	}

	public File popDirectoryStack() {
		if (!directoryStack.isEmpty()) {
			return directoryStack.pop();
		}
		return null; // or handle this case as you see fit
	}
}
