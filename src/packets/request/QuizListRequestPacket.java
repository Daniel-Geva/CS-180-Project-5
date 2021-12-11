package packets.request;

import java.util.ArrayList;

import datastructures.Quiz;
import packets.response.QuizListResponsePacket;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

/**
 * Request Packet for a list of quizzes
 *
 * Requests the server to send a list of quizzes to the client
 *
 * @author Liam Kelly
 *
 * @version October 7, 2021
 *
 */

public class QuizListRequestPacket extends RequestPacket {
    
	String requestType;
    String searchTerm;
    ArrayList<Quiz> quizzesWithChanges;
    boolean push;
    ArrayList<Quiz> quizzesToReturn;

    public QuizListRequestPacket(String requestType, String searchTerm, boolean push) {
        this.requestType = requestType;
        this.searchTerm = searchTerm;
        this.push = push;
    }

    public QuizListRequestPacket(ArrayList<Quiz> quizzesWithChanges, boolean push) {
        this.quizzesWithChanges = quizzesWithChanges;
        this.push = push;
    }
    /**
     * Sends response packet to the client
     *
     * @param lms
     * @return a new response packet with the requested quizzes
     */
    public ResponsePacket serverHandle(LearningManagementSystemServer lms) {
        if (quizzesWithChanges == null) {
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
            return new QuizListResponsePacket(quizzesToReturn, push);
        } else {
            return new QuizListResponsePacket(quizzesWithChanges, push);
        }
    }

}
