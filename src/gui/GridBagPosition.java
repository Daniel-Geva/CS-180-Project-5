package gui;

import java.awt.GridBagConstraints;

public enum GridBagPosition {

	LEFT(GridBagConstraints.LINE_START),
	RIGHT(GridBagConstraints.LINE_END),
	CENTER(GridBagConstraints.CENTER);
	
	int pos;
	private GridBagPosition(int pos) { 
		this.pos = pos;
	}
	
	public GridBagConstraints get() {
		GridBagConstraints obj = new GridBagConstraints();
		obj.anchor = this.pos;
		return obj;
	}
	
}
