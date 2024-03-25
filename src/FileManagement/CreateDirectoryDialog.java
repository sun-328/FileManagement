package FileManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateDirectoryDialog extends JDialog implements DialogBox{
	private JTextField directoryNameField;
	private FileOperationManager fileOperationManager;

	public CreateDirectoryDialog(Frame parent, FileOperationManager fileOperationManager) {
		super(parent, "Create Directory", true);
		this.fileOperationManager = fileOperationManager;
		initializeUI();
	}

	public void initializeUI() {
		JPanel panel = new JPanel(new GridLayout(2, 2));

		JLabel nameLabel = new JLabel("Directory Name:");
		directoryNameField = new JTextField();

		JButton createButton = new JButton("Create");
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String directoryName = directoryNameField.getText();
				fileOperationManager.createDirectory(directoryName);
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
		panel.add(directoryNameField);
		panel.add(createButton);
		panel.add(cancelButton);

		getContentPane().add(panel);
		setSize(300, 150);
		setLocationRelativeTo((Component) getParent());
	}
}
