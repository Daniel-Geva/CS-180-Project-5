package gui;

import java.awt.Font;

import javax.swing.JLabel;

public class Label extends JLabel {

	private static final long serialVersionUID = 7183462484820139751L;

	public Label(String text) {
		this.setText(text);
		Font f = new Font(this.getFont().getName(), Font.PLAIN, 14);
		this.setFont(f);
		this.setForeground(Aesthetics.GENERAL_FOREGROUND);
	}
	
}
