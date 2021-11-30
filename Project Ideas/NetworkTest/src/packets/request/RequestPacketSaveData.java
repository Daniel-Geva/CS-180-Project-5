package packets.request;

import packets.response.ResponsePacket;
import server.MainServer;

public class RequestPacketSaveData extends RequestPacket {

	private static final long serialVersionUID = 3299553115373290198L;
	
	String newData;
	
	public RequestPacketSaveData(String data) {
		this.newData = data;
	}
	
	@Override
	public ResponsePacket serverHandle(MainServer server) {
		server.getDataManager().setData(newData);
		
		return new ResponsePacket(true);
	}
	
}
