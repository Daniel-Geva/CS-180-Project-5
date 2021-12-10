package gui;

import java.awt.GridBagConstraints;

public class GridBagBuilder {

	GridBagConstraints con;
	
	public static GridBagBuilder start() {
		return new GridBagBuilder();
	}
	
	private GridBagBuilder() { 
		this.con = new GridBagConstraints();
	}
	
	public GridBagBuilder left() {
		this.con.anchor = GridBagConstraints.WEST;
		return this;
	}
	
	public GridBagBuilder right() {
		this.con.anchor = GridBagConstraints.EAST;
		return this;
	}
	
	public GridBagBuilder top() {
		this.con.anchor = GridBagConstraints.NORTH;
		return this;
	}
	
	public GridBagBuilder bottom() {
		this.con.anchor = GridBagConstraints.SOUTH;
		return this;
	}
	
	public GridBagBuilder stretchY() {
		this.con.fill = GridBagConstraints.VERTICAL;
		return this;
	}
	
	public GridBagBuilder stretchX() {
		this.con.fill = GridBagConstraints.VERTICAL;
		return this;
	}
	
	public GridBagBuilder weightY() {
		this.con.weighty = 1;
		return this;
	}
	
	public GridBagBuilder weightX() {
		this.con.weightx = 1;
		return this;
	}
	
	public GridBagConstraints build() {
		return con;
	}
	
}
