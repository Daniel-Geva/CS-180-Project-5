package gui;

import javax.swing.JButton;

public class Button extends JButton {

	private static final long serialVersionUID = -4114844686642969288L;
	
	public Button(String label) {
		super(label);
		//super.setPreferredSize(new Dimension(1, 1));
	}
	
	PanelRunnable clickRunnable;
	
	public Button onClick(PanelRunnable runnable) {
		this.clickRunnable = runnable;
		return this;
	}

	public PanelRunnable getClickRunnable() {
		return this.clickRunnable;
	}
	
}
