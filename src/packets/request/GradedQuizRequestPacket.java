package packets.request;

import server.LearningManagementSystemServer;

import java.io.Serializable;
import packets.response.GradedQuizResponsePacket;

public class GradedQuizRequestPacket implements Serializable {
    public GradedQuizResponsePacket serverHandle(LearningManagementSystemServer mainServer) {
        return new GradedQuizResponsePacket(true);
    }
}
