package client;

//make sure to run in EDT so the GUI can be updated

import packets.response.ResponsePacket;

public class ResponsePacketHandler {
    public ResponsePacketHandler() {}

    RunnableHandleResponsePacket onRecieveRunnable;

    public void onReceiveResponse(RunnableHandleResponsePacket runnable) {
        this.onRecieveRunnable = runnable;
    }

    public void handlePacket(ResponsePacket packet) {
        if(this.onRecieveRunnable != null) {
            this.onRecieveRunnable.handlePacket(packet);
        }
    }

}
