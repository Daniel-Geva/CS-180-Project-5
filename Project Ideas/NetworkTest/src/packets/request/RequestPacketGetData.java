package packets.request;

import packets.response.ResponsePacket;
import packets.response.ResponsePacketGetData;
import server.MainServer;

public class RequestPacketGetData extends RequestPacket {

	private static final long serialVersionUID = -5870700952455113461L;

	@Override
	public ResponsePacket serverHandle(MainServer server) {
		String data = server.getDataManager().getData();
		ResponsePacketGetData response = new ResponsePacketGetData(data);
		return response;
	}
	
}
