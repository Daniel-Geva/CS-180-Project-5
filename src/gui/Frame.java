package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {

	private static final long serialVersionUID = 2645095664952620750L;

	public Frame add(JPanel panel) {
		super.add(panel);
		return this;
	}
	
	public void open() {
		this.setVisible(true);
	}
	
}
