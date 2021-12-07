package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.border.StrokeBorder;

public class DebugListener extends MouseAdapter {

	public static final DebugListener INST;
	
	static {
		INST = new DebugListener();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		JComponent component = (JComponent) e.getSource();
		component.setBorder(new StrokeBorder(new BasicStroke(5), Color.BLUE));
	}
	@Override
	public void mouseExited(MouseEvent e) {
		JComponent component = (JComponent) e.getSource();
		component.setBorder(null);
	}
	
}
