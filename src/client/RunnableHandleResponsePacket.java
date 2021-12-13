package client;

import packets.response.ResponsePacket;
/**
 * An interface that gives a callback function whenever a packet is received for a specific request
 * <p>
 *
 *
 * @author Isaac Fleetwood
 *
 * @version December 10, 2021
 *
 */
public interface RunnableHandleResponsePacket {
    /**
     * Runs the handle packet method in context with the handler after confirming that the packet is not null
     *
     * @param responsePacket - The packet that will be handled with the provided RunnableHandleResponsePacket
     */
    public void handlePacket(ResponsePacket responsePacket);
}
