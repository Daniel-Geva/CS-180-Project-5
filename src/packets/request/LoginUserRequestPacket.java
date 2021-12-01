package packets.request;

import datastructures.User;
import packets.response.NewUserResponsePacket;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;
import server.UserManager;

public class LoginUserRequestPacket extends RequestPacket{
    private String username;
    private String password;

    public LoginUserRequestPacket(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public ResponsePacket serverHandle(LearningManagementSystemServer mainServer) {
        //authenticate with usermanager
        if (mainServer.getUserManager().authenticator(username, password)) {
            return new NewUserResponsePacket(mainServer.getUserManager().getUser(username));
        } else {
            return new ResponsePacket(false);
        }

        //return user object based on input
           //need a response packet that takes input and stores it

    }

}
