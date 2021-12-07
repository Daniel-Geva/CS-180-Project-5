package packets.request;

import packets.response.QuizResponsePacket;
import server.LearningManagementSystemServer;
import packets.response.ResponsePacket;

/**
 * Request Packet for a single quiz
 *
 * Requests the server to send a quiz to the client
 *
 * @author Liam Kelly
 *
 * @version October 7, 2021
 *
 */

public class QuizRequestPacket extends RequestPacket {
    int id;

    public QuizRequestPacket(int id) {
        this.id = id;
    }

    /**
     * Sends response packet to the client
     *
     * @param lms
     * @return a new response packet with the requested quiz
     */

    public ResponsePacket serverHandle(LearningManagementSystemServer lms) {
        return new QuizResponsePacket(lms, id);
    }



}
