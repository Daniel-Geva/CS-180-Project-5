package gui;

import java.awt.Dimension;

import javax.swing.JComponent;

/**
 * A component composed of empty space used to better format the panels
 *
 * @author Isaac Fleetwood
 * @version December 12, 2021
 */
public class GapComponent extends JComponent {

	private int gapSize;

	public GapComponent(int gapSize) {
		this.gapSize = gapSize;
		this.setSize(gapSize, gapSize);
	}

	/**
	 * Gets the maximum size
	 *
	 * @return maxSize - The maximum size as an integer
	 */
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(this.gapSize, this.gapSize);
	}

	public GapComponent() {
		
	}

}
