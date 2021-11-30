package client;

import packets.response.ResponsePacket;

public interface RunnableHandleResponsePacket {

	public void handlePacket(ResponsePacket responsePacket);
	
}
