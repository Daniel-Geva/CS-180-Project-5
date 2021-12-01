package packets.request;

import packets.response.QuizResponsePacket;
import server.LearningManagementSystemServer;
import packets.response.ResponsePacket;

public class QuizRequestPacket extends RequestPacket {
    int id;

    public QuizRequestPacket(int id) {
        this.id = id;
    }

    public ResponsePacket serverHandle(LearningManagementSystemServer lms) {
        return new QuizResponsePacket(lms, id);
    }



}
