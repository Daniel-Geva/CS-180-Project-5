package gui;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class Heading extends JLabel {

	private static final long serialVersionUID = 7183462484820139751L;

	public Heading(String text) {
		this.setText(text);
		Font f = new Font(this.getFont().getName(), Font.BOLD, 24);
		this.setFont(f);
		this.setForeground(Aesthetics.GENERAL_FOREGROUND);
		// TODO Debug
		this.addMouseListener(DebugListener.INST);
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
