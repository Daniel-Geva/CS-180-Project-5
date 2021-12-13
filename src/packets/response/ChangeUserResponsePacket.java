package packets.response;

/**
 * Sends a packet from server to client to inform the client the user information has been changed
 * Is used for recognizing type of change in user information
 *
 * @author Aryan Jain
 * @version 1.0.0
 */

public class ChangeUserResponsePacket extends ResponsePacket {

	public ChangeUserResponsePacket(boolean success, boolean push) {
		super(success, push);
	}
	
}
