package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.StrokeBorder;

public class DebugListener extends MouseAdapter {

	public static final MouseListener INST;
	
	HashMap<JComponent, Border> borders = new HashMap<JComponent, Border>();
	
	static {
		//INST = new DebugListener();
		INST = new MouseAdapter() {};
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		JComponent component = (JComponent) e.getSource();
		//component.setBackground(new Color(0, 0, 0, 100));
		borders.put(component, component.getBorder());
		component.setBorder(new StrokeBorder(new BasicStroke(5), Color.BLUE));
	}
	@Override
	public void mouseExited(MouseEvent e) {
		JComponent component = (JComponent) e.getSource();
		//component.setBackground(Aesthetics.GENERAL_BACKGROUND);
		component.setBorder(borders.get(component));
	}
	
}
