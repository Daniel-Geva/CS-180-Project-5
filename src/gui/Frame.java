package gui;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {

	private static final long serialVersionUID = 2645095664952620750L;

	public Frame add(JPanel panel) {
		super.add(panel);
		return this;
	}
	
	public void open() {
		for(Component comp: this.getComponents()) {
			if(comp instanceof Panel) {
				Panel p = (Panel) comp;
				p.runOnOpen();
			}
		}
		this.setVisible(true);
	}
	
}
