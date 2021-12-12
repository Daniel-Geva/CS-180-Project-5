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
     * Sends response packet to the client
     *
     * @param lms
     * @return a new response packet with the requested quiz
     */

    public ResponsePacket serverHandle(LearningManagementSystemServer lms) {
        System.out.println("Handling!");
        if (quizWithChanges == null) {
            Quiz quiz = lms.getQuizManager().searchQuizByID(id);
            return new QuizResponsePacket(quiz, false);
        } else {
            System.out.println("Else!");
            if (lms.getQuizManager().searchQuizByID(quizWithChanges.getId()) != null) {
            	System.out.println("Remove!");
            	lms.getQuizManager().removeQuiz(quizWithChanges.getId());
            } else {
                quizWithChanges.setID(lms.getQuizManager().getUniqueID());
            }
            lms.getQuizManager().addQuiz(quizWithChanges);
            System.out.println("Saving!");
            lms.getQuizFileManager().save();
            return new QuizResponsePacket(quizWithChanges, true);
        }
    }



}
