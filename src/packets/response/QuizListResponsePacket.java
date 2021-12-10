package packets.response;

import datastructures.Quiz;
import java.util.ArrayList;

/**
 * Gets the requested list of the quizzes to be returned to the client
 *
 * @author Liam Kelly
 *
 * @version October 7, 2021
 *
 **/

public class QuizListResponsePacket extends ResponsePacket {
    ArrayList<Quiz> quizzesToReturn;

    public QuizListResponsePacket(ArrayList<Quiz> quizzesToReturn, boolean push) {
        super(push);
        this.quizzesToReturn = quizzesToReturn;
    }

    /**
     * Allows the client to get the requested list of quizzes
     *
     * @return quizzesToReturn - the requested list of quizzes
     *
     */
    public ArrayList<Quiz> getListOfQuizzesResponse() {
        return quizzesToReturn;
    }

}
