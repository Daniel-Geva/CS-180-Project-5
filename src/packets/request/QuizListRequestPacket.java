package packets.request;

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
        return new QuizListResponsePacket(lms, requestType, searchTerm);
    }

}
