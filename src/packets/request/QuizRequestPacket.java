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
 * @version December 5, 2021
 *
 */

public class QuizRequestPacket extends RequestPacket {
    int id;
    Quiz quizWithChanges;

    public QuizRequestPacket(int id) {
        this.id = id;
    }

    public QuizRequestPacket(Quiz quizWithChanges) {
        this.quizWithChanges = quizWithChanges;
    }


    /**
     * Sends response packet to the client and replaces the quiz if changes were made to it.
     * Also allows the user to add a quiz to QuizManager
     *
     * @param lms
     * @return a new response packet with the requested quiz
     */

    public ResponsePacket serverHandle(LearningManagementSystemServer lms) {
        if (quizWithChanges == null) {
            Quiz quiz = lms.getQuizManager().searchQuizByID(id);
            return new QuizResponsePacket(quiz, false);
        } else {
            if (lms.getQuizManager().searchQuizByID(quizWithChanges.getId()) != null) {
            	lms.getQuizManager().removeQuiz(quizWithChanges.getId());
            } else {
                quizWithChanges.setID(lms.getQuizManager().getUniqueID());
            }
            lms.getQuizManager().addQuiz(quizWithChanges);
            lms.getQuizFileManager().save();
            return new QuizResponsePacket(quizWithChanges, true);
        }
    }



}
