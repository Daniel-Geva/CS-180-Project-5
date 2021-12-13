package gui;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Heading extends JLabel {

	public Heading(String text) {
		this.setText(text);
		Font f = new Font(this.getFont().getName(), Font.BOLD, 24);
		this.setFont(f);
		this.setForeground(Aesthetics.GENERAL_FOREGROUND);
	}

	public Heading center() {
		this.setHorizontalAlignment(SwingConstants.CENTER);
		return this;
	}
	
	public Heading big() {
		Font f = new Font(this.getFont().getName(), Font.BOLD, 36);
		this.setFont(f);
		return this;
	}
	
	public Heading margin(int margin) {
		this.setBorder(BorderFactory.createEmptyBorder(0, 0, margin, 0));
		return this;
	}
	
}
