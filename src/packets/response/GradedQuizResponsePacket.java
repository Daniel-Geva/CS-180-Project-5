package packets.response;
import datastructures.GradedQuiz;
import server.LearningManagementSystemServer;
import java.io.Serializable;

/**
 * Graded Quiz Response Packet
 *
 * Holds the Graded Quiz packet that is sent as a response
 *
 * @author Sean Lee
 * @version 12/7/21
 * @see GradedQuizResponsePacket
 * @see datastructures.GradedQuiz
 */
public class GradedQuizResponsePacket extends ResponsePacket implements Serializable {
    GradedQuiz gradedQuiz;
//    boolean success = false;

//    public GradedQuizResponsePacket(boolean success) {
//        this.success = success;
//    }

//    public GradedQuizResponsePacket(boolean success, GradedQuiz gradedQuiz) {
//        this.success = success;
//    }

    public GradedQuizResponsePacket(boolean push, GradedQuiz gradedQuiz) {
        super(push);
        this.gradedQuiz = gradedQuiz;
    }

    public GradedQuizResponsePacket(boolean push) {

    }

    public GradedQuiz getQuizResponse() {
        return gradedQuiz;
    }

//    public boolean getSuccess() {
//        return this.success;
//    }
}
