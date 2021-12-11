package datastructures;

import java.io.Serializable;

import server.UserManager;

/**
 *
 * Super class that contains the details of a particular user
 *
 * @author Sean Lee
 * @version 11/14/21
 * @see UserManager
 */
public class User implements Serializable {
    
	private int id;
    private String username;
    private String password;
    private String name;
    private static Object obj = new Object();

    public User(int id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    /**
     * Sets name of user
     * @param name Name of user
     */
    public void setName(String name) {
        synchronized (obj) {
            this.name = name;
        }
    }

    /**
     * Returns name of user
     * @return Name of user
     */
    public String getName() {
        synchronized (obj) {
            return name;
        }
    }

    /**
     * Returns ID of user
     * @return ID of user
     */
    public int getID() {
        synchronized (obj) {
            return id;
        }
    }

    /**
     * Sets ID of user
     * @param inputID ID of user
     */
    public void setID(int inputID) {
        synchronized (obj) {
            this.id = inputID;
        }
    }

    /**
     * Returns username of user
     * @return username of user
     */
    public String getUsername() {
        synchronized (obj) {
            return username;
        }
    }

    /**
     * Sets username of user
     * @param username Username of user
     */
    public void setUsername(String username) {
        synchronized (obj) {
            this.username = username;
        }
    }

    /**
     * Returns password of user
     * @return Password of user
     */
    public String getPassword() {
        synchronized (obj) {
            return password;
        }
    }

    /**
     * Sets password of user
     * @param password Password of user
     */
    public void setPassword(String password) {
        synchronized (obj) {
            this.password = password;
        }
    }

    /**
     * Returns the user in String form
     * @return User in String form
     */
    public String toString() {
        String format = "ID: %d, Username: %s, Password: %s";
        synchronized (obj) {
            return String.format(format, id, username, password);
        }
    }
}
