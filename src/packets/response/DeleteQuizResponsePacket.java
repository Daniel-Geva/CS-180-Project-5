package packets.response;

import packets.request.DeleteQuizRequestPacket;

/**
 * Informs the client that a quiz was succesfully deleted and gives acces to the id of the deleted quiz
 *
 * @author Liam Kelly
 * @version December 12, 2021
 *
 **/

public class DeleteQuizResponsePacket extends ResponsePacket{
    boolean success;
    int id;

    public DeleteQuizResponsePacket(boolean success, int id) {
        super(success, success);
        this.success = success;
        this.id = id;
    }

    /**
     * Returns a boolean representing if a quiz was found and deleted
     * @return success - if a quiz was successfully deleted
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Gives client access to the id of the deleted quiz
     * @return
     */
    public int getQuizId() {
        return id;
    }


}
