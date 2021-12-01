package client;

import packets.response.ExampleResponsePacket;

public interface RunnableHandleResponsePacket {

	public void handlePacket(ExampleResponsePacket responsePacket);
	
}
