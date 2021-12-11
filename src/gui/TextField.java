package gui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextField extends JPanel {

	private String resultKey;
	
	private JLabel jlabel;
	private JTextField jtextField;

	public TextField(String label, String resultKey, String fieldPlaceholder) {
		this.jlabel = new JLabel(label);
		this.resultKey = resultKey;
		this.jtextField = new JTextField();
		
		this.setLayout(new GridLayout(2, 1));
		this.add(this.jlabel);
		this.add(jtextField);
		
		this.jlabel.setForeground(Aesthetics.GENERAL_FOREGROUND);
		this.jtextField.setForeground(Aesthetics.TEXT_FIELD_FOREGROUND);
		this.jtextField.setBackground(Aesthetics.TEXT_FIELD_BACKGROUND);
		
		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
	}
	
	public TextField(String label, String resultKey) {
		this(label, resultKey, label);
	}
	
	public TextField(String label) {
		this(label, label, label);
	}
	
	public void setSize(int wid, int hei) {
		super.setSize(wid, hei);
		this.jtextField.setSize(wid, hei/2);
		this.jlabel.setSize(wid, hei/2);
	}

	public String getResultKey() {
		return this.resultKey;
	}
	
	public String getText() {
		return this.jtextField.getText();
	}
	
	public JTextField getJTextField() {
		return this.jtextField;
	}

	public void setText(String value) {
		this.jtextField.setText(value);
	}
}
