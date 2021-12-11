package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {

	public Frame add(JPanel panel) {
		super.add(panel);
		return this;
	}
	
	public void open() {
		this.setVisible(true);
	}
	
}
