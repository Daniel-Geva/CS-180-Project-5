package packets.response;

import packets.request.DeleteQuizRequestPacket;

public class DeleteQuizResponsePacket extends ResponsePacket{
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
