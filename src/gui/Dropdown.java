package gui;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dropdown extends JPanel {

	private String resultKey;
	private String name;
	private JLabel label;
	private JComboBox<String> jComboBox;
	
	public Dropdown(String resultKey, String[] options) {
		this.resultKey = resultKey;
		this.jComboBox = new JComboBox<String>(options);

		this.setLayout(new GridLayout(1, 1));
		this.add(jComboBox);

		this.jComboBox.setForeground(Aesthetics.TEXT_FIELD_FOREGROUND);
		this.jComboBox.setBackground(Aesthetics.TEXT_FIELD_BACKGROUND);
		
		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
	}
	
	public Dropdown(String name, String resultKey, String[] options) {
		this.label = new JLabel(name);
		this.name = name;
		this.jComboBox = new JComboBox<String>(options);
		this.setLayout(new GridLayout(2, 1));
		this.add(label);
		this.add(jComboBox);

		this.label.setForeground(Aesthetics.GENERAL_FOREGROUND);
		this.jComboBox.setForeground(Aesthetics.TEXT_FIELD_FOREGROUND);
		this.jComboBox.setBackground(Aesthetics.TEXT_FIELD_BACKGROUND);
		
		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
	}
	
	public String getSelection() {
		return (String) this.jComboBox.getSelectedItem();
	}
	
	public String getResultKey() {
		return this.name;
	}
	
}
