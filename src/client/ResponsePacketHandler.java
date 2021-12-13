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

	public ResponsePacketHandler() { }

    RunnableHandleResponsePacket onRecieveRunnable;

    /**
     * Sets the interpreter to the corresponding one for the received packet
     *
     * @param runnable - A RunnableHandleResponsePacket interface to be used with the packet
     */
    public void onReceiveResponse(RunnableHandleResponsePacket runnable) {
        this.onRecieveRunnable = runnable;
    }

    /**
     * Runs the handle packet method in context with the handler after confirming that the packet is not null
     *
     * @param packet - The packet that will be handled with the provided RunnableHandleResponsePacket
     */
    public void handlePacket(ResponsePacket packet) {
        if (this.onRecieveRunnable != null) {
            this.onRecieveRunnable.handlePacket(packet);
        }
    }

}
