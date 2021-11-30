package server;

public class MainServer {

	DataManager dataManager;
	NetworkManagerServer networkManagerServer;
	
	public static void main(String[] args)  {
		MainServer mainServer = new MainServer();
		mainServer.init();
	}

	private void init() {
		this.dataManager.init();
		this.networkManagerServer.init();
	}

	public MainServer() {
		this.dataManager = new DataManager(this);
		this.networkManagerServer = new NetworkManagerServer(this);
	}

	public DataManager getDataManager() {
		return this.dataManager;
	}
	
	
}
