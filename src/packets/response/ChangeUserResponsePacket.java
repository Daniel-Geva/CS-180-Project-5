package packets.response;

public class ChangeUserResponsePacket extends ResponsePacket {

	public ChangeUserResponsePacket(boolean success, boolean push) {
		super(success, push);
	}
	
}
