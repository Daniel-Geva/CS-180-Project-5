package gui;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Dropdown extends JPanel {

	private static final long serialVersionUID = -585198543811384495L;

	String name;
	JLabel label;
	JComboBox<String> jComboBox;
	
	public Dropdown(String name, String[] options) {
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
