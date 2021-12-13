package packets.request;

import java.io.Serializable;

import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

/**
 * Parent class for all RequestPacket types
 * <p>
 * Sent from the client to the server to ask for information or to notify of a change
 *
 * @author Isaac Fleetwood
 * @version 12/13/2021
 */
public abstract class RequestPacket implements Serializable {

    /**
     * Method called in order to 'handle' or unpack the instructions of the packet
     *
     * @param lms - The server manager is provided in order to insure access to necessary information
     * @return responsePacket - Packet sent back to the client containing the server's response
     */
    public abstract ResponsePacket serverHandle(LearningManagementSystemServer lms);
    
}
