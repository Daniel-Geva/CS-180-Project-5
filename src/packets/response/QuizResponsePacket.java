package packets.response;

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
        super(true, push);
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
