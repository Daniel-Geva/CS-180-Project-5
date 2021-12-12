package packets.request;

import datastructures.User;

import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;


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
            return new ResponsePacket(true, false);
        } else {
            //If the condition turns out to be false, then the operation is marked as false
            return new ResponsePacket(false, false);
        }
    }
}


