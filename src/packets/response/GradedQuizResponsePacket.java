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
public class GradedQuizResponsePacket implements Serializable {
    GradedQuiz gradedQuiz;

    public GradedQuizResponsePacket(LearningManagementSystemServer lms, int id) {
        // if gradedQuiz is null the entered id is not valid
        gradedQuiz = lms.getGradedQuizManager().searchGradedQuizByID(id);
    }

    public GradedQuiz getQuizResponse() {
        return gradedQuiz;
    }
}
