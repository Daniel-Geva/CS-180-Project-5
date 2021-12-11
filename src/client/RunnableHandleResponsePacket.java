package client;

import packets.response.ResponsePacket;
/**
 * short description
 * <p>
 * functionality
 *
 * @author Isaac Fleetwood
 *
 * @version December 10, 2021
 *
 */
public interface RunnableHandleResponsePacket {
    public void handlePacket(ResponsePacket responsePacket);
}
