package gui;

public class DynamicLabel extends Label {

	String id;
	
	public DynamicLabel(String text, String id) {
		super(text);
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
}
