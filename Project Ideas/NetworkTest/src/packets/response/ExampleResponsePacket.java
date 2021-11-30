package packets.response;

import java.io.Serializable;

public class ExampleResponsePacket implements Serializable {

	private static final long serialVersionUID = -5153672930902069453L;

	boolean success;
	
	public ExampleResponsePacket() {
		this.success = true;
	}
	
	public ExampleResponsePacket(boolean success) {
		this.success = success;
	}
	
	public boolean wasSuccess() {
		return this.success;
	}
	
}
