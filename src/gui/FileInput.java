package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class FileInput extends JPanel {

	private String resultKey;
	private JFileChooser jFileChooser;
	private Label selectedFileLabel;

	public FileInput(String text, String buttonName, String resultKey) {
		this.resultKey = resultKey;
		this.jFileChooser = new JFileChooser();
		this.selectedFileLabel = new Label("No file");
		// new DynamicLabel("No File Selected", "chosen-file")
		
		this.setLayout(new GridLayout(2, 1));
		this.add(new Label(text).margin(5));
		this.add(new Panel(new FlowLayout(FlowLayout.LEADING))
			.add(new Button(buttonName)
				.onClick((Panel panel) -> {
					jFileChooser.showOpenDialog(null);
					File f = jFileChooser.getSelectedFile();
					selectedFileLabel.setText(f != null ? f.getName() : "No File Selected");
				}))
			.add(selectedFileLabel)
		);
		
		//this.jlabel.setForeground(Aesthetics.GENERAL_FOREGROUND);
		this.jFileChooser.setForeground(Aesthetics.TEXT_FIELD_FOREGROUND);
		this.jFileChooser.setBackground(Aesthetics.TEXT_FIELD_BACKGROUND);
		
		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
	}

	public String getResultKey() {
		return this.resultKey;
	}
	
	public File getFile() {
		return this.jFileChooser.getSelectedFile();
	}

	public JComponent setPanelSize(int i, int j) {
		this.setPreferredSize(new Dimension(i, j));
		this.setSize(new Dimension(i, j));
		return this;
	}
	
}
