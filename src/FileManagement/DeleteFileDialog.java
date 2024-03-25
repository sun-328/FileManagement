package FileManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteFileDialog extends JDialog implements DialogBox{
	private FileOperationManager fileOperationManager;

	public DeleteFileDialog(Frame parent, FileOperationManager fileOperationManager) {
		super(parent, "Delete File", true);
		this.fileOperationManager = fileOperationManager;
		initializeUI();
	}

	public void initializeUI() {
		JPanel panel = new JPanel(new GridLayout(2, 1));

		JLabel nameLabel = new JLabel("Select the file to delete:");

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Add code to delete the selected file here
				// You can use fileOperationManager to perform the deletion
				dispose(); // Close the dialog
			}
		});

		panel.add(nameLabel);
		panel.add(deleteButton);

		getContentPane().add(panel);
		setSize(300, 150);
		setLocationRelativeTo((Component) getParent());
	}
}
