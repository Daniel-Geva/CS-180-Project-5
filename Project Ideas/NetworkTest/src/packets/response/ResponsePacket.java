package packets.response;

import java.io.Serializable;

public class ResponsePacket implements Serializable {

	private static final long serialVersionUID = -5153672930902069453L;

	boolean success;
	
	public ResponsePacket() {
		this.success = true;
	}
	
	public ResponsePacket(boolean success) {
		this.success = success;
	}
	
	public boolean wasSuccess() {
		return this.success;
	}
	
}
