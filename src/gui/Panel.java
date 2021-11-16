package gui;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class Panel extends JPanel {
	
	private static final long serialVersionUID = -5199098153231935544L;

	public Panel add(JComponent component) {
		super.add(component);
		return this;
	}
	
	public void open() {
		Frame f = new Frame();
		f.add(this);
		f.setVisible(true);
	}
	
}
