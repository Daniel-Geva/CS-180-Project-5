package gui;

import java.awt.Dimension;

import javax.swing.JComponent;

public class GapComponent extends JComponent {

	private static final long serialVersionUID = 6540594197503433616L;

	int gapSize;
	public GapComponent(int gapSize) {
		this.gapSize = gapSize;
		this.setSize(gapSize, gapSize);
	}
	
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(this.gapSize, this.gapSize);
	}
	
	public GapComponent() {}
	
}
