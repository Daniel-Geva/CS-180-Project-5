package gui;

import javax.swing.JRadioButton;

public class RadioButton extends JRadioButton {

	private static final long serialVersionUID = -973924243650919387L;

	String resultKey;
	int selectionId;
	
	public RadioButton(String text) {
		super(text);
	}

	public RadioButton(String answer, int selectionId, int resultKey) {
		this(answer, selectionId, Integer.toString(resultKey));
	}
	
	public RadioButton(String text, int selectionId, String resultKey) {
		super(text);
		this.selectionId = selectionId;
		this.resultKey = resultKey;
		
		this.setForeground(Aesthetics.GENERAL_FOREGROUND);
		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
	}

	public String getResultKey() {
		return resultKey;
	}

	public int getSelectionId() {
		return selectionId;
	}
	
}
