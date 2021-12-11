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
    public void handlePacket(ResponsePacket responsePacket);
}
