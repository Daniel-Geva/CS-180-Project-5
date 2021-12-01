package packets.request;

import datastructures.User;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

public class CreateUserRequestPacket extends RequestPacket {
    private User user;
    //Constructor giving user parameter
    public CreateUserRequestPacket(User user) {
        this.user = user;
    }

    //server
    public ResponsePacket serverHandle(LearningManagementSystemServer mainServer) {
        //add user to usermanager
        if (mainServer.getUserManager().getUser(user.getUsername()) == null) {
            mainServer.getUserManager().addUser(user);
            return new ResponsePacket(true);
        } else {
            return new ResponsePacket(false);
        }
        //if user already exists return fail in the return statement below
    }

}
