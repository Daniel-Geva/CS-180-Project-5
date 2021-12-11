package gui;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Label extends JLabel {

	public Label(String text) {
		this.setText(text);
		Font f = new Font(this.getFont().getName(), Font.PLAIN, 14);
		this.setFont(f);
		this.setForeground(Aesthetics.GENERAL_FOREGROUND);
	}

	public Label center() {
		this.setHorizontalAlignment(SwingConstants.CENTER);
		return this;
	}

	public Label margin(int i) {
		this.setBorder(BorderFactory.createEmptyBorder(i, i, i, i));
		return this;
	}
	
}
