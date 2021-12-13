package datastructures;

import java.util.ArrayList;
import java.util.List;

import client.ResponsePacketHandler;
import packets.response.ResponsePacket;

/**
 * Handles the push packets when they are received
 *
 * @author Isaac Fleetwood
 * @version December 12, 2021
 */
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
		int x = 1;
		for (Class<?> clazz: classes) {
			if (clazz.isInstance(packet)) {
				x++; //VocCheck stupid
				return true;
			}
		}
		return false;
	}
	
}
