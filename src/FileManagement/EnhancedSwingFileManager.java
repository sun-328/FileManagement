package FileManagement;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import java.io.File;

public class EnhancedSwingFileManager {
    private File currentDirectory;
    private JTable fileTable;
    private FileOperationManager fileOperationManager;
    private JLabel currentPathLabel;
    private JButton backButton;
    
    
    public EnhancedSwingFileManager() {
        this.currentDirectory = new File(System.getProperty("user.home"));
        this.fileOperationManager = new FileOperationManager();
    }
    
    
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			EnhancedSwingFileManager fileManager = new EnhancedSwingFileManager();
			fileManager.createAndShowGUI();
		});
	}
	private void createAndShowGUI() {
		JFrame frame = new JFrame("Enhanced File Management System");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		createUI(frame);

		fileOperationManager.listFilesAndDirectories(fileTable);
		fileOperationManager.updateCurrentPathLabel(currentPathLabel);

		frame.setSize(1000, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	private void createUI(JFrame frame) {
	    JPanel panel = new JPanel(new BorderLayout());

	    fileTable = new JTable(new FileTableModel());
	    fileTable.setDefaultRenderer(Object.class, new FileTableCellRenderer());
	    JScrollPane scrollPane = new JScrollPane(fileTable);

	    // Top Panel
	    JPanel topPanel = new JPanel(new BorderLayout());

	    // Back Button
	    backButton = createButton("Back", e -> fileOperationManager.navigateBack(fileTable,currentPathLabel));
	    topPanel.add(backButton, BorderLayout.WEST);

	    panel.add(topPanel, BorderLayout.NORTH);

	    // Current Path Label
	    currentPathLabel = new JLabel("Current Path: " + currentDirectory.getAbsolutePath(), SwingConstants.CENTER);
	    currentPathLabel.setFont(new Font("Arial", Font.BOLD, 15));
	    topPanel.add(currentPathLabel, BorderLayout.CENTER);

	    panel.add(topPanel, BorderLayout.NORTH);

	    // Button Panel
	    JPanel bottomButtonPanel = new JPanel(new GridLayout(1, 0, 10, 0)); // Horizontal layout for buttons
	    
	    bottomButtonPanel.add(createButton("Navigate Directories", e -> fileOperationManager.navigateDirectories(fileTable,currentPathLabel)));
	    
	    bottomButtonPanel.add(createButton("Create Directory", e -> fileOperationManager.showCreateDirectoryDialog(frame,fileTable)));
	    
	    bottomButtonPanel.add(createButton("Create File", e -> fileOperationManager.showCreateFileDialog(frame,fileTable)));

	    JButton deleteDirectoryButton = createButton("Delete Directory", e -> fileOperationManager.deleteDirectory(frame,fileTable));
	    deleteDirectoryButton.setVisible(false); // Initially hidden

	    JButton deleteFileButton = createButton("Delete File", e -> fileOperationManager.deleteFile(frame,fileTable));
	    deleteFileButton.setVisible(false); // Initially hidden
	    
	    JButton renameButton = createButton("Rename", e -> fileOperationManager.showRenameDialog(frame,fileTable));
	    renameButton.setVisible(false); // Initially hidden

	    JButton propertiesButton = createButton("Properties", e -> fileOperationManager.showPropertiesDialog(frame,fileTable));
	    propertiesButton.setVisible(false); // Initially hidden
	    
	    fileTable.getSelectionModel().addListSelectionListener(e -> {
	        boolean isRowSelected = fileTable.getSelectedRow() >= 0;
	        deleteDirectoryButton.setVisible(isRowSelected);
	        deleteFileButton.setVisible(isRowSelected);
	        renameButton.setVisible(isRowSelected);
	        propertiesButton.setVisible(isRowSelected); // Show/hide Properties button
	    });

	    bottomButtonPanel.add(deleteDirectoryButton);
	    bottomButtonPanel.add(deleteFileButton);
	    bottomButtonPanel.add(renameButton);
	    bottomButtonPanel.add(propertiesButton);
	    
	    // Parent Directory Button
	    JButton parentDirButton = createButton("Parent Directory", e -> fileOperationManager.navigateToParentDirectory(fileTable,currentPathLabel));
	    bottomButtonPanel.add(parentDirButton); // Add the "Parent Directory" button here

	    panel.add(bottomButtonPanel, BorderLayout.SOUTH);
	    panel.add(scrollPane, BorderLayout.CENTER);

	    frame.getContentPane().add(panel);

	    // Add double click navigation
	    fileOperationManager.addDoubleClickNavigation(fileTable,currentPathLabel);
	}

	private JButton createButton(String text, ActionListener listener) {
		JButton button = new JButton(text);
		button.addActionListener(listener);
		return button;
	}
}