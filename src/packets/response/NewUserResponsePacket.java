package packets.response;

import datastructures.User;

public class NewUserResponsePacket extends ResponsePacket {
    private User user;

    public NewUserResponsePacket(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
