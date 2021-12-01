package packets.request;

import packets.response.QuizResponsePacket;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

import java.io.Serializable;
import packets.response.GradedQuizResponsePacket;

public class GradedQuizRequestPacket implements Serializable {
    int id;

    public GradedQuizRequestPacket(int id) {
        this.id = id;
    }

    public GradedQuizResponsePacket serverHandle(LearningManagementSystemServer lms) {
        return new GradedQuizResponsePacket(lms, id);
    }
}
