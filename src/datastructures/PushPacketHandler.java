package datastructures;

import client.ResponsePacketHandler;
import packets.push.PushPacket;
import packets.response.ResponsePacket;

public abstract class PushPacketHandler<T extends PushPacket> extends ResponsePacketHandler {

	public boolean canHandle(PushPacket packet) {
		return (packet instanceof T);
	}
	
	public void run(ResponsePacket responsePacket) {
		
	}
	
	public abstract void run(T packet);
	
}
