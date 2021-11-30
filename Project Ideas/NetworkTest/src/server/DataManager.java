package server;

public class DataManager {

	MainServer mainServer;
	
	String data;
	
	public DataManager(MainServer mainServer) {
		this.mainServer = mainServer;
	}

	public void init() {
		this.data = "";
	}

	public String getData() {
		return this.data;
	}
	
	public void setData(String newData) {
		this.data = newData;
	}

}
