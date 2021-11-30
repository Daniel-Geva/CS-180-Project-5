package packets.request;

import java.io.Serializable;

import packets.response.ResponsePacket;
import server.MainServer;

public class RequestPacket implements Serializable {
	
	private static final long serialVersionUID = -4323029867682502486L;
	
	public ResponsePacket serverHandle(MainServer mainServer) {
		return new ResponsePacket(true);
	}
	
}
