package FileManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateFileDialog extends JDialog implements DialogBox{
	private JTextField fileNameField;
	private FileOperationManager fileOperationManager;
	private Frame parent; // Add parent as a field

	public CreateFileDialog(Frame parent, FileOperationManager fileOperationManager) {
		super(parent, "Create File", true);
		this.parent = parent; // Initialize parent
		this.fileOperationManager = fileOperationManager;
		initializeUI();
	}

	public void initializeUI() {
		JPanel panel = new JPanel(new GridLayout(3, 2));

		JLabel nameLabel = new JLabel("File Name:");
		fileNameField = new JTextField();

		JLabel extensionLabel = new JLabel("Extension (e.g., .txt, .java):");
		JTextField extensionField = new JTextField();

		JButton createButton = new JButton("Create");
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = fileNameField.getText().trim();
				String extension = extensionField.getText().trim();
				if (!extension.startsWith(".")) {
					extension = "." + extension;
				}
				fileOperationManager.createFile(fileName + extension);
				dispose(); // Close the dialog
			}
		});

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(); // Close the dialog without performing the operation
			}
		});

		panel.add(nameLabel);
		panel.add(fileNameField);
		panel.add(extensionLabel);
		panel.add(extensionField);
		panel.add(createButton);
		panel.add(cancelButton);

		getContentPane().add(panel);
		setSize(400, 200);
		setLocationRelativeTo(parent);
	}
}
