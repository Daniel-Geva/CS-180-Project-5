package gui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

/**
 * 
 * Debug class used for testing.
 * Highlight panels by changing their borders to blue.
 * 
 * Unused in the final project.
 * 
 * @author Isaac Fleetwood
 * @version December 12, 2021
 */
public class DebugListener extends MouseAdapter {

	public static final MouseListener INST;
	
	HashMap<JComponent, Border> borders = new HashMap<JComponent, Border>();
	
	static {
		// INST = new DebugListener();
		INST = new MouseAdapter() { };
	}

	/**
	 * Highlights a component when the mouse enters it
	 *
	 * @param e - MouseEvent used to get the component the mouse is in
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		JComponent component = (JComponent) e.getSource();
		// component.setBackground(new Color(0, 0, 0, 100));
		borders.put(component, component.getBorder());
		Insets i;
		if (component.getBorder() == null) {
			i = new Insets(0, 0, 0, 0);
		} else {
			i = component.getBorder().getBorderInsets(component);
		}
		component.setBorder(BorderFactory.createMatteBorder(
		    i.top + 1, i.left + 1, i.bottom + 1, i.right +1, Color.BLUE
		));
	}

	/**
	 * Unhighlights a component when the mouse leaves it
	 *
	 * @param e - MouseEvent used to get the component the mouse leaves
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		JComponent component = (JComponent) e.getSource();
		// component.setBackground(Aesthetics.GENERAL_BACKGROUND);
		component.setBorder(borders.get(component));
	}
	
}
