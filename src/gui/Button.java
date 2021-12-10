package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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

	@Override
	public Dimension getMaximumSize() {
		return this.getPreferredSize();
	}

	public PanelRunnable getClickRunnable() {
		return this.clickRunnable;
	}
	
	public Panel panelize() {
		Panel p = new Panel(new GridBagLayout());
		p.add(this, new GridBagConstraints());
		return p;
	}

	public Button color(Color bg) {
		this.setBackground(bg);
		this.setForeground(Aesthetics.BUTTON_FOREGROUND);
		this.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Aesthetics.GENERAL_BACKGROUND, 5),
				BorderFactory.createEmptyBorder(
					Aesthetics.BUTTON_BORDER_SIZE,
					Aesthetics.BUTTON_BORDER_SIZE,
					Aesthetics.BUTTON_BORDER_SIZE,
					Aesthetics.BUTTON_BORDER_SIZE
				)
			)
		);
		return this;
	}
	
}
