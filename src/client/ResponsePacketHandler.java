package client;

//make sure to run in EDT so the GUI can be updated

import packets.response.ResponsePacket;

/**
 * Handles packets received from the server
 * <p>
 * Makes sure the packet received is not null and interprets it in context
 *
 * @author Isaac Fleetwood
 *
 * @version December 10, 2021
 *
 */

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
