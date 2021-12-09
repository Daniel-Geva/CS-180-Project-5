package gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class Button extends JButton {

	private static final long serialVersionUID = -4114844686642969288L;
	
	public Button(String label) {
		super(label);
		
		this.setBackground(Aesthetics.BUTTON_BACKGROUND);
		this.setForeground(Aesthetics.BUTTON_FOREGROUND);
		this.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Aesthetics.GENERAL_BACKGROUND, 5),
				BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Aesthetics.BUTTON_BORDER, 1),
					BorderFactory.createEmptyBorder(
						Aesthetics.BUTTON_BORDER_SIZE, 
						Aesthetics.BUTTON_BORDER_SIZE, 
						Aesthetics.BUTTON_BORDER_SIZE, 
						Aesthetics.BUTTON_BORDER_SIZE
					)
				)
			)
		);
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
