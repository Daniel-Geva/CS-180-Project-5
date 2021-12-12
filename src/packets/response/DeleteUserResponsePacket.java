package packets.response;

/**
 *
 * DeleteUserResponsePacket file for CS 180 Project
 *
 * This connects the server with the client and
 * tells the client side that account deletion for
 * the current user has been done successfully and
 * the interface should go back to the login screen.
 *
 * @author Aryan Jain
 * @version 1.0.0
 *
 */

public class DeleteUserResponsePacket extends ResponsePacket{
    int id;

    public DeleteUserResponsePacket(int id) {
        super(true, true);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
