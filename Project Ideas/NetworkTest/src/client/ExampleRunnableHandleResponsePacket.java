package client;

import packets.response.ExampleResponsePacket;

public interface ExampleRunnableHandleResponsePacket {

	public void handlePacket(ExampleResponsePacket responsePacket);
	
}
