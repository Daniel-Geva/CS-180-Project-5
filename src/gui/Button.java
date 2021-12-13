package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Encapsulation of a JButton that allows for additional features
 *
 * @author Isaac Fleetwood
 * @version December 12, 2021
 */
public class Button extends JButton {

	/**
	 * Creates a button
	 *
	 * @param label - The text (String) that goes on the button
	 */
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

	/**
	 * Defines a callback function for when the button is clicked
	 *
	 * @param runnable - The callback function for the button
	 * @return this - Returns the button
	 */
	public Button onClick(PanelRunnable runnable) {
		this.clickRunnable = runnable;
		return this;
	}

	/**
	 * Gets the maximumSize of the button
	 *
	 * @return preferredSize - The preferred max size of the button
	 */
	@Override
	public Dimension getMaximumSize() {
		return this.getPreferredSize();
	}

	/**
	 * Gets the callback function of the button
	 *
	 * @return clickRunnable - The runnable that defines the callback
	 */
	public PanelRunnable getClickRunnable() {
		return this.clickRunnable;
	}

	/**
	 * Encloses the button into a panel to help with sizing
	 *
	 * @return p - A panel that contains the button
	 */
	public Panel panelize() {
		Panel p = new Panel(new GridBagLayout());
		p.add(this, new GridBagConstraints());
		return p;
	}

	/**
	 * Sets the color of the button
	 *
	 * @param bg - Background color of the button
	 * @return button - The button object
	 */
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
