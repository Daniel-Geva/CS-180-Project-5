package client;

import packets.response.ExampleResponsePacket;

public class ExampleResponsePacketHandler {

	public ExampleResponsePacketHandler() {
		
	}
	
	ExampleRunnableHandleResponsePacket onReceiveRunnable;
	
	public void onReceiveResponse(ExampleRunnableHandleResponsePacket runnable) {
		this.onReceiveRunnable = runnable;
	}
	
	public void handlePacket(ExampleResponsePacket packet) {
		if(this.onReceiveRunnable != null) {
			this.onReceiveRunnable.handlePacket(packet);
		}
	}
	
}
