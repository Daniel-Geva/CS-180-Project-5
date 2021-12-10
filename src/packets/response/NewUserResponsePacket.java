package packets.response;

import datastructures.User;

/**
 *
 * NewUserResponsePacket file for CS 180 Project
 *
 * Stores the user that has logged in to the program
 * as an object
 *
 * @author Aryan Jain
 * @version 1.0.0
 *
 */

public class NewUserResponsePacket extends ResponsePacket {
    private User user;

    public NewUserResponsePacket(User user) {
        this.user = user;
    }

    /**
     * Returns the user that has logged in
     *
     * @return the current user
     */
    public User getUser() {
        return user;
    }
}
