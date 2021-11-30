package packets.request;

import java.io.Serializable;

import packets.response.ExampleResponsePacket;
import server.MainServer;

public class ExampleRequestPacket implements Serializable {
	
	private static final long serialVersionUID = -4323029867682502486L;
	
	public ExampleResponsePacket serverHandle(MainServer mainServer) {
		return new ExampleResponsePacket(true);
	}
	
}
