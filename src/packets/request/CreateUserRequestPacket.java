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
    	user.setID(mainServer.getUserManager().getUniqueID());
        if (mainServer.getUserManager().getUser(user.getUsername()) == null) {
            mainServer.getUserManager().addUser(user);
            return new ResponsePacket(true);
        } else {
            return new ResponsePacket(false);
        }

    }

}
