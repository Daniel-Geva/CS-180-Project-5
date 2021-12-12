package packets.request;


import packets.response.DeleteUserResponsePacket;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

/**
 *
 * DeleteUserRequestPacket file for CS 180 Project 5
 * This packet serves as a bridge for communication between the client side to server communication.
 * Information is sent from the client to the server once the user chooses the option to delete their
 * account in the GUI. This class provides the server class with the information to delete their account.
 *
 * @author Aryan Jain
 * @version 1.0.0
 *
 *
 */

public class DeleteUserRequestPacket extends RequestPacket{

    int id;

    public DeleteUserRequestPacket(int user) {
        this.id = user;
    }


    public ResponsePacket serverHandle(LearningManagementSystemServer mainServer) {
        /*
          If the user doesn't exist in the ArrayList of Users in UserManager file,
          then the operation is marked as false
        */
        if (!(mainServer.getUserManager().getUserById(id).getUsername() == null)) {
            //Operation is marked as successful
            mainServer.getUserManager().removeUserByID(id);
            mainServer.getUserFileManager().save();
            return new DeleteUserResponsePacket(id);
        } else {
            //If the condition turns out to be false, then the operation is marked as unsuccessful
            return new ResponsePacket(false, false);
        }
    }
}


