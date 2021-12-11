package packets.request;

import java.io.Serializable;

import packets.response.ResponsePacket;
import server.LearningManagementSystemServer;

public abstract class RequestPacket implements Serializable {

    public abstract ResponsePacket serverHandle(LearningManagementSystemServer lms);
    
}
