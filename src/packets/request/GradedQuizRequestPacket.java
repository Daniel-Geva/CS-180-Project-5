package packets.request;
import server.LearningManagementSystemServer;
import java.io.Serializable;
import packets.response.GradedQuizResponsePacket;

/**
 * Graded Quiz Request Packet
 *
 * Contains the method that handles a handle request by the server.
 *
 * @author Sean Lee
 * @version 12/7/21
 * @see GradedQuizResponsePacket
 * @see datastructures.GradedQuiz
 */
public class GradedQuizRequestPacket implements Serializable {
    int id;

    public GradedQuizRequestPacket(int id) {
        this.id = id;
    }

    public GradedQuizResponsePacket serverHandle(LearningManagementSystemServer lms) {
        return new GradedQuizResponsePacket(lms, id);
    }
}
