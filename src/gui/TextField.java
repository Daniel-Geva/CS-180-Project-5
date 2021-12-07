package gui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextField extends JPanel {

	private static final long serialVersionUID = -4102102193118450553L;

	private String resultKey;
	
	private JLabel jlabel;
	private JTextField jtextField;

	public TextField(String label, String resultKey, String fieldPlaceholder) {
		this.jlabel = new JLabel(label);
		this.resultKey = resultKey;
		this.jtextField = new JTextField();
		// TODO Debug
		this.jlabel.addMouseListener(DebugListener.INST);
		this.jtextField.addMouseListener(DebugListener.INST);
		
		this.setLayout(new GridLayout(2, 1));
		this.add(this.jlabel);
		this.add(jtextField);
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
}
