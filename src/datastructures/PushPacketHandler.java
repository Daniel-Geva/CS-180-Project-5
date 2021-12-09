package datastructures;

import client.ResponsePacketHandler;
import packets.response.ResponsePacket;

public abstract class PushPacketHandler extends ResponsePacketHandler {

	Class<?> clazz;
	
	public boolean canHandle(ResponsePacket packet) {
		return clazz.isInstance(packet);
	}
	
}
