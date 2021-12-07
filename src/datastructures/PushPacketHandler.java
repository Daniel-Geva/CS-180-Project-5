package datastructures;

import client.ResponsePacketHandler;
import packets.response.ResponsePacket;

public abstract class PushPacketHandler<T extends ResponsePacket> extends ResponsePacketHandler {

	public boolean canHandle(ResponsePacket packet) {
		return (packet instanceof ResponsePacket);
	}
	
	public abstract void run(T packet);
	
}
