package gui;

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
	}
	
	public String getSelection() {
		return (String) this.jComboBox.getSelectedItem();
	}
	
	public String getResultKey() {
		return this.name;
	}
	
}
