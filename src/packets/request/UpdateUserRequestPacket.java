package packets.request;

import datastructures.User;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

public class UpdateUserRequestPacket extends RequestPacket {

	User user;
	
	public UpdateUserRequestPacket(User user) {
		this.user = user;
	}
	
	@Override
	public ResponsePacket serverHandle(LearningManagementSystemServer mainServer) {
		User oldUser = mainServer.getUserManager().getUserById(this.user.getID());
		User sameUsernameUser = mainServer.getUserManager().getUser(this.user.getUsername());
		if(sameUsernameUser != null && sameUsernameUser.getID() != this.user.getID())
			return new ResponsePacket(false, false);
		oldUser.setName(user.getName());
		oldUser.setUsername(user.getUsername());
		oldUser.setPassword(user.getPassword());
		return new ResponsePacket(true, false);
	}

}
