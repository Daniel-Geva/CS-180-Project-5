package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextField extends JPanel {

	private String resultKey;

	private JLabel jlabel;
	private JTextField jtextField;

	public TextField(String label, String resultKey, String fieldPlaceholder) {
		this.resultKey = resultKey;

		this.setLayout(new GridLayout(label.isBlank() ? 1 : 2, 1));

		if (!label.isBlank()) {
			this.jlabel = new JLabel(label);
			this.add(this.jlabel);
			this.jlabel.setForeground(Aesthetics.GENERAL_FOREGROUND);
		}

		this.jtextField = new JTextField(fieldPlaceholder);
		this.add(jtextField);

		this.jtextField.setForeground(Aesthetics.TEXT_FIELD_FOREGROUND);
		this.jtextField.setBackground(Aesthetics.TEXT_FIELD_BACKGROUND);

		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
	}

	public TextField(String label, String resultKey) {
		this(label, resultKey, "");
	}

	public TextField(String label) {
		this(label, label, "");
	}

	@Override
	public void setSize(int wid, int hei) {
		Dimension d = new Dimension(wid, hei);
		super.setSize(wid, hei);
		super.setMaximumSize(d);
		super.setMinimumSize(d);
		super.setPreferredSize(d);
		if (this.jlabel != null) {
			this.jtextField.setSize(wid, hei / 2);
			this.jlabel.setSize(wid, hei / 2);
		} else {
			this.jtextField.setSize(d);
			this.jtextField.setMaximumSize(d);
			this.jtextField.setMinimumSize(d);
			this.jtextField.setPreferredSize(d);
		}
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

	public Panel panelize(int width, int height, int marginX, int marginY) {
		this.setSize(width - marginX, height - marginY);
		return new Panel().add(this).setPanelSize(width, height).alignLeft().setMargin(marginX, marginY);
	}
}
