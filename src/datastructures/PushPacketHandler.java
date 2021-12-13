package datastructures;

import java.util.ArrayList;
import java.util.List;

import client.ResponsePacketHandler;
import packets.response.ResponsePacket;

public abstract class PushPacketHandler extends ResponsePacketHandler {

	List<Class<?>> classes;
	
	public PushPacketHandler() {
		this.classes = new ArrayList<Class<?>>();
	}
	
	public PushPacketHandler addClass(Class<?> clazz) {
		classes.add(clazz);
		return this;
	}
	
	public boolean canHandle(ResponsePacket packet) {
		for (Class<?> clazz: classes) {
			if (clazz.isInstance(packet))
				return true;
		}
		return false;
	}
	
}
