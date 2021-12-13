package packets.response;

/**
 * Gives clients access to id of quiz deleted, along with if a quiz was deleted
 *
 * @author Liam Kelly
 *
 * @version December 12, 2021
 *
 **/
public class DeleteQuizResponsePacket extends ResponsePacket {
    boolean success;
    int id;

    public DeleteQuizResponsePacket(boolean success, int id) {
        super(success, success);
        this.success = success;
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getQuizId() {
        return id;
    }


}
