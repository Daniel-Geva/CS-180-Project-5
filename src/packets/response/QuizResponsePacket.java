package packets.response;

import server.LearningManagementSystemServer;
import datastructures.Quiz;

/**
 * Gets the requested quiz to be returned to the client
 *
 * @author Liam Kelly
 *
 * @version October 7, 2021
 *
 **/

public class QuizResponsePacket extends ResponsePacket{
    Quiz quizToReturn;

    public QuizResponsePacket(Quiz quiz, boolean push) {
        super(push);
        quizToReturn = quiz;
    }

    /**
     * Allows the client to get the requested quiz
     *
     * @return quizToReturn - the requested quiz
     *
     */

    public Quiz getQuizResponse() {
        return quizToReturn;
    }

}
