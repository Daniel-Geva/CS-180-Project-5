package packets.response;

import packets.request.DeleteQuizRequestPacket;

public class DeleteQuizResponsePacket extends ResponsePacket{
    boolean success;
    public DeleteQuizResponsePacket(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
