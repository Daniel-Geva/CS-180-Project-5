package packets.request;

import datastructures.User;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

/**
 *
 * CreateUserRequestPacket file for CS 180 Project 5
 * Once the user chooses the option to create a user in the GUI,
 * The client side connects with the server to send the information
 * And process that information. This file acts as that bridge between
 * Client to server communication.
 *
 * @author Aryan Jain
 * @version 1.0.0
 *
 *
 */

public class CreateUserRequestPacket extends RequestPacket {
    private User user;

    //Constructor giving user parameter
    public CreateUserRequestPacket(User user) {
        this.user = user;
    }

    //server

    /**
     * Method to connect the client information with the server
     * @param mainServer
     * @return
     */
    public ResponsePacket serverHandle(LearningManagementSystemServer mainServer) {
        //Generates an ID for the user
        user.setID(mainServer.getUserManager().getUniqueID());
        /*
        Checks to see if the user already has an account or not
        Searches for the user in the arraylist of all the users
        */
        if (mainServer.getUserManager().getUser(user.getUsername()) == null) {
            //if the user cannot be found in that arraylist, then this new user is added to the list
            //Operation is marked as successful
            mainServer.getUserManager().addUser(user);
            return new ResponsePacket(true);
        } else {
            //If the condition turns out to be false, then the operation is marked as false
            return new ResponsePacket(false);
        }
    }

}
