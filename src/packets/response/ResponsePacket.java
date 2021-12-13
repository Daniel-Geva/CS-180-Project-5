package packets.response;

import java.io.Serializable;

/**
 * Parent class for all ResponsePacket types
 *
 * Sent from the server to the client to response to the requests it makes. It can return an object,
 * success condition, or be a push packet to update other clients
 *
 * @author Isaac Fleetwood
 */
public class ResponsePacket implements Serializable {

    private static final long serialVersionUID = -5153672930902069453L;

    boolean success;

    public ResponsePacket() {
        this.success = true;
        this.push = false;
    }

    public ResponsePacket(boolean success, boolean push) {
        this.success = success;
        this.push = push;
    }

    /**
     * Returns the value of the success field
     *
     * @return success - Boolean value of the success field; Whether the change succeeded
     */
    public boolean wasSuccess() {
        return this.success;
    }

    private boolean push;

    /**
     * Returns the value of the push field
     *
     * @return push - Boolean value of the push field; Whether it is a push packet
     */
    public boolean getPush() {
        return push;
    }

    /**
     * Sets the value of the push field
     *
     * @param push - Boolean value of the push field; Whether it is a push packet
     */
	public void setPush(boolean push) {
		this.push = push;
	}

}
