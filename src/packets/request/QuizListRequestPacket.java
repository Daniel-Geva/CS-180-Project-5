package packets.request;

import packets.response.QuizListResponsePacket;
import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

public class QuizListRequestPacket extends RequestPacket {
    String requestType;
    String searchTerm;
    public QuizListRequestPacket(String requestType, String searchTerm) {
        this.requestType = requestType;
        this.searchTerm = searchTerm;
    }

    public ResponsePacket serverHandle(LearningManagementSystemServer lms) {
        return new QuizListResponsePacket(lms, requestType, searchTerm);
    }

}
