package server;

import java.util.ArrayList;

import datastructures.Manager;
import datastructures.Student;
import datastructures.Teacher;
import datastructures.User;

/**
 * Reads and writes files with user information for initialization and storage
 * <p>
 * Contains an ArrayList of users from storage that will be passed to UserManager.
 * <p>
 *
 * @author Daniel Geva
 * @version 11/14/21
 * @see UserManager
 * @see FileWrapper
 */
public class UserFileManager implements Manager {

    LearningManagementSystemServer lms;
    private ArrayList<User> users;

    public UserFileManager(LearningManagementSystemServer lms) {
        this.lms = lms;
        this.users = this.readUsers();
    }

    /**
     * Sets the UserManager's arraylist of users after reading saved data
     */
    @Override
    public void init() {
        lms.getUserManager().setUsers(this.users);
    }

    @Override
    public void exit() {
        this.save();
    }

    /**
     * Gets the altered arraylist of users from UserManager after the program finishes, to be written to a file
     */
    public synchronized void save() {
        this.users = lms.getUserManager().getUsers();
        this.writeUsers();
    }

    /**
     * Reads the file that stores the user data and constructs an arraylist of users from it
     *
     * @return tempUsers - an arraylist of the users to be given to UserManager
     */
    public ArrayList<User> readUsers() {
        ArrayList<User> tempUsers = new ArrayList<>();
        String path = "./data/users.txt";
        ArrayList<String> contents = FileWrapper.readFile(path);

        if (contents == null) {
            return tempUsers;
        }

        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).isBlank() || contents.get(i).isEmpty()) {
                continue;
            }

            String[] list = contents.get(i).split("::", 2);
            //Two "::" colons are used to separate the type of user from the other information
            String[] info = list[1].split(";;", 4);
            //Two ";;" semicolons are used to separate the information used to construct a user
            int id = Integer.parseInt(info[0]);
            String username = info[1];
            String password = info[2];
            String name = info[3];
            if (list[0].equals("teacher")) {
                tempUsers.add(new Teacher(id, name, username, password));
            } else { //student
                tempUsers.add(new Student(id, name, username, password));
            }
        }
        return tempUsers;
    }

    /**
     * Writes the arraylist of users "users" to a file in order to store the data
     *
     * @return success - whether the writing to a file succeeded
     */
    private boolean writeUsers() {
        ArrayList<String> writableUsers = new ArrayList<>();
        String path = "./data/users.txt";

        for (int i = 0; i < users.size(); i++) {
            String write = "";
            if (users.get(i) instanceof Teacher) { //differentiate between a teacher and a student
                write += "teacher::";
            } else {
                write += "student::";
            }
            write += String.format("%d;;%s;;%s;;%s",
                    users.get(i).getID(), users.get(i).getUsername(),
                    users.get(i).getPassword(), users.get(i).getName());
            writableUsers.add(write);
        }
        boolean success = FileWrapper.writeFile(path, writableUsers);
        return success;
    }

}
