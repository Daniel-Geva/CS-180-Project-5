package packets.request;

import packets.response.QuizListResponsePacket;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;
import datastructures.*;

import java.util.ArrayList;

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
    ArrayList<Quiz> quizzesToReturn;

    public QuizListRequestPacket(String requestType, String searchTerm) {
        this.requestType = requestType;
        this.searchTerm = searchTerm;
    }

    /**
     * Sends response packet to the client
     *
     * @param lms
     * @return a new response packet with the requested quizzes
     */
    public ResponsePacket serverHandle(LearningManagementSystemServer lms) {
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
        return new QuizListResponsePacket(quizzesToReturn, requestType, searchTerm);
    }

}
