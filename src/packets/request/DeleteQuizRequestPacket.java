package packets.request;

import datastructures.*;
import packets.response.DeleteQuizResponsePacket;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

public class DeleteQuizRequestPacket extends RequestPacket{
    int id;

    public DeleteQuizRequestPacket(int id) {
        this.id = id;
    }


    public ResponsePacket serverHandle(LearningManagementSystemServer lms) {
        if (lms.getQuizManager().searchQuizByID(id) != null) {
            lms.getQuizManager().removeQuiz(id);
            return new DeleteQuizResponsePacket(true);
        } else {
            return new DeleteQuizResponsePacket(false);
        }
    }
}
