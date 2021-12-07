package gui;

import java.awt.Font;

import javax.swing.JLabel;

public class Heading extends JLabel {

	private static final long serialVersionUID = 7183462484820139751L;

	public Heading(String text) {
		this.setText(text);
		Font f = new Font(this.getFont().getName(), Font.BOLD, 24);
		this.setFont(f);
	}
	
}
