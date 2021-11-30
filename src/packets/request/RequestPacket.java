package packets.request;

import java.io.Serializable;

import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

public class RequestPacket implements Serializable {

    public RequestPacket serverHandle(LearningManagementSystemServer mainServer) {
        return new ResponsePacket(true);
    }
}
