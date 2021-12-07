package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Menu extends JFrame {
	
	private static final long serialVersionUID = 246858678709754513L;

	public Menu add(JPanel panel) {
		super.add(panel);
		return this;
	}
	
	public void open() {
		this.setVisible(true);
	}
	
}
