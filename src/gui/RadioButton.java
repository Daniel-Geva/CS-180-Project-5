package gui;

import javax.swing.JRadioButton;

public class RadioButton extends JRadioButton {

	private static final long serialVersionUID = -973924243650919387L;

	String resultKey;
	
	public RadioButton(String text) {
		super(text);
	}

	public RadioButton(String answer, int resultKey) {
		this(answer, Integer.toString(resultKey));
	}
	
	public RadioButton(String text, String resultKey) {
		super(text);
		this.resultKey = resultKey;
		
		this.setForeground(Aesthetics.GENERAL_FOREGROUND);
		this.setBackground(Aesthetics.GENERAL_BACKGROUND);
	}

	public String getResultKey() {
		return resultKey;
	}
	
}
