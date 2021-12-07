package packets.response;

import server.LearningManagementSystemServer;
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
    String searchTerm;
    public QuizListResponsePacket(LearningManagementSystemServer lms,  String requestType, String searchTerm) {
        this.searchTerm = searchTerm;
        switch (requestType) {
            case "All":
                quizzesToReturn = lms.getQuizManager().getQuizList();
                break;
            case "Author":
                quizzesToReturn = lms.getQuizManager().searchQuizByAuthor(searchTerm);
                break;
            case "Name":
                quizzesToReturn = lms.getQuizManager().searchQuizByName(searchTerm);
                break;
            case "Course":
                quizzesToReturn = lms.getQuizManager().searchQuizByCourse(searchTerm);
                break;
            default:
                quizzesToReturn = null;
        }
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
