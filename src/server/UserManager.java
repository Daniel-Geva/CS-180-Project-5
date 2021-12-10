//TODO: Gives reasons for why each part of the code is synchronized
package server;

import java.util.ArrayList;
import java.util.Random;

import datastructures.Manager;
import datastructures.User;

/**
 * The UserManager of the application
 * <p>
 * Implements the Manager class
 * <p>
 * Helps application keep track of all the users
 * and allows application to do several functionalities
 *
 * @author Aryan Jain
 * @version 1.0.0
 */

public class UserManager implements Manager {
    //Created an Object named obj to synchronize elements of this code
    Object obj = new Object();
    //Arraylist called users that has user objects
    private ArrayList<User> users = new ArrayList<>();

    LearningManagementSystemServer lms;

    /**
     * Constructor for the UserManager.
     * <p>
     * All initialization is done in {@link #init()}, so the
     * constructor solely sets the {@link #lms} field.
     */
    public UserManager(LearningManagementSystemServer lms) {
        this.lms = lms;
    }

    /**
     * Returns an ArrayList users of User objects
     */
    public ArrayList<User> getUsers() {

        synchronized (obj) {
            return users;
        }
    }

    /**
     * Set an ArrayList users of User objects
     */
    public void setUsers(ArrayList<User> users) {
        synchronized (obj) {
            this.users = users;
        }
    }

    /**
     * Adds a User object to the ArrayList users
     */
    public void addUser(User user) {
        synchronized (obj) {
            users.add(user);
        }
    }

    /**
     * Removes a User object from the ArrayList users
     */
    public void removeUser(User user) {
        synchronized (obj) {
            users.remove(user);
        }
    }

    /**
     * Takes an username parameter and a password parameter
     * and returns boolean if a specific user's
     * username matches its password
     */
    public boolean authenticator(String username, String password) {
        synchronized (obj) {
            for (User user : users) {

                if ((user.getUsername().equals(username)) && (user.getPassword().equals(password))) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Iterates through the ArrayList users and returns a user based
     * on a search by username
     */
    public User getUser(String username) {
        synchronized (obj) {
            for (User user : users) {

                if (user.getUsername().equals(username)) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Iterates through the ArrayList users and returns a user based
     * on a search by User ID
     */
    public User getUserById(int id) {
        synchronized (obj) {
            for (User user : users) {
                if (user.getID() == id) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Generates a uniqueID for each User object in the ArrayList users
     */
    public int getUniqueID() {
        Random rand = new Random();
        int id = 0;
        boolean exists = true;
        while (exists) {
            exists = false;
            id = rand.nextInt(999999);
            synchronized (obj) {
                for (int i = 0; i < users.size(); i++) {

                    if (users.get(i).getID() == i) {
                        exists = true;
                    }
                }
            }
        }
        return id;
    }

    /**
     * Default init method from the Manager Interface
     * Is blank because this method is not necessary for this class
     */
    @Override
    public void init() {

    }

    /**
     * Default exit method from the Manager Interface
     * Is blank because this method is not necessary for this class
     */
    @Override
    public void exit() {

    }
}
