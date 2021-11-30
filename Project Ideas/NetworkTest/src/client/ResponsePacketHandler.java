package client;

import packets.response.ExampleResponsePacket;

public class ResponsePacketHandler {

	public ResponsePacketHandler() {
		
	}
	
	RunnableHandleResponsePacket onReceiveRunnable;
	
	public void onReceiveResponse(RunnableHandleResponsePacket runnable) {
		this.onReceiveRunnable = runnable;
	}
	
	public void handlePacket(ExampleResponsePacket packet) {
		if(this.onReceiveRunnable != null) {
			this.onReceiveRunnable.handlePacket(packet);
		}
	}
	
}
