package packets.request;

import datastructures.User;
import packets.response.NewUserResponsePacket;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;
import server.UserManager;

/**
 *
 * LoginUserRequestPacket file for CS 180 Project 5
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

public class LoginUserRequestPacket extends RequestPacket{
    private String username;
    private String password;

    //Constructor with 2 paramaters: Username and Password
    public LoginUserRequestPacket(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Method to connect client information with the server
     * @param mainServer
     * @return
     */
    //need a response packet that takes input and stores it
    public ResponsePacket serverHandle(LearningManagementSystemServer mainServer) {
        //authenticate with the method called authenticator from the user manager file
        //If username and password are a match,
        //then the operation is a success
        if (mainServer.getUserManager().authenticator(username, password)) {
            //return user object based on input
            return new NewUserResponsePacket(mainServer.getUserManager().getUser(username));
        } else {
            //if the condition is not met, then the operation is unsuccessful
            return new ResponsePacket(false);
        }


    }

}
