package packets.request;

import packets.response.ExampleResponsePacket;
import packets.response.ResponsePacketGetData;
import server.MainServer;

public class RequestPacketGetData extends packets.request.ExampleRequestPacket {

	private static final long serialVersionUID = -5870700952455113461L;

	@Override
	public ExampleResponsePacket serverHandle(MainServer server) {
		String data = server.getDataManager().getData();
		ResponsePacketGetData response = new ResponsePacketGetData(data);
		return response;
	}
	
}
