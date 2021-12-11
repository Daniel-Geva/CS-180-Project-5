package datastructures;

import java.io.Serializable;

/**
 *
 * Class that contains the detail of a student
 *
 * @author Sean Lee
 * @version 11/14/21
 * @see User
 */
public class Student extends User implements Serializable {
    
	UserPermission userPermission;
    public Student(int id, String name, String username, String password) {
        super(id, name, username, password);
        this.userPermission = UserPermission.USER;
    }

    /**
     * Returns the permission of the student
     * @return Permission of Student
     */
    public Enum<UserPermission> getUserPermission() {
        return userPermission;
    }


    /**
     * Returns the student in string form
     * @return Student in String format
     */
    public String toString() {
        return super.toString() + String.format(", User Permission: %s", userPermission);
    }
}
