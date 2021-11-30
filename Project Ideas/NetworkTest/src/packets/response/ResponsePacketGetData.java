package packets.response;

public class ResponsePacketGetData extends ResponsePacket {

	private static final long serialVersionUID = 195292918769341238L;
	
	String data;
	
	public ResponsePacketGetData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}
	
}
