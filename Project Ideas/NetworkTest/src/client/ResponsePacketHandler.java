package client;

import packets.response.ResponsePacket;

public class ResponsePacketHandler {

	public ResponsePacketHandler() {
		
	}
	
	RunnableHandleResponsePacket onReceiveRunnable;
	
	public void onReceiveResponse(RunnableHandleResponsePacket runnable) {
		this.onReceiveRunnable = runnable;
	}
	
	public void handlePacket(ResponsePacket packet) {
		if(this.onReceiveRunnable != null) {
			this.onReceiveRunnable.handlePacket(packet);
		}
	}
	
}
