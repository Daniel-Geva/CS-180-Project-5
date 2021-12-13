package gui;

import javax.swing.JRadioButton;

/**
 * Helps with RadioButton creation and contains all the methods related to it.
 * It is an encapsulation of the JRadioButton
 *
 * @author Isaac Fleetwood
 * @version 1.0.0
 */

public class RadioButton extends JRadioButton {

	private String resultKey;
	private String selectionId;
	
	public RadioButton(String text) {
		super(text);
	}

	public RadioButton(String selection, int selectionId, int resultKey) {
		this(selection, Integer.toString(selectionId), Integer.toString(resultKey));
	}
	
	public RadioButton(String text, String selectionId, String resultKey) {
		super(text);
		this.selectionId = selectionId;
		this.resultKey = resultKey;
		
		this.setForeground(Aesthetics.GENERAL_FOREGROUND);
		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
	}

	public String getResultKey() {
		return resultKey;
	}

	public String getSelectionId() {
		return selectionId;
	}
	
}
