package packets.request;

import datastructures.Quiz;
import packets.response.QuizResponsePacket;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

/**
 * Request Packet for a single quiz
 *
 * Requests the server to send a quiz to the client
 *
 * @author Liam Kelly
 *
 * @version October 7, 2021
 *
 */

public class QuizRequestPacket extends RequestPacket {
    int id;
    boolean push;
    Quiz quizWithChanges;

    public QuizRequestPacket(int id, boolean push) {
        this.id = id;
    }

    public QuizRequestPacket(Quiz quizWithChanges, boolean push) {
        this.quizWithChanges = quizWithChanges;
        this.push = push;
    }


    /**
     * Sends response packet to the client
     *
     * @param lms
     * @return a new response packet with the requested quiz
     */

    public ResponsePacket serverHandle(LearningManagementSystemServer lms) {
        if (quizWithChanges == null) {
            Quiz quiz = lms.getQuizManager().searchQuizByID(id);
            return new QuizResponsePacket(quiz, push);
        } else {
            return new QuizResponsePacket(quizWithChanges, push);
        }
    }



}
