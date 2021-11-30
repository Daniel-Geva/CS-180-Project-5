package client;

public class MainClient {

	UIManager uiManager;
	NetworkManagerClient networkManagerClient;
	
	public static void main(String[] args)  {
		MainClient mainClient = new MainClient();
		mainClient.init();
		mainClient.run();
	}

	private void init() {
		this.uiManager.init();
		this.networkManagerClient.init();
	}
	
	private void run() {
		this.uiManager.run();
	}

	public MainClient() {
		this.uiManager = new UIManager(this);
		this.networkManagerClient = new NetworkManagerClient(this);
	}

	public UIManager getUIManager() {
		return this.uiManager;
	}

	public NetworkManagerClient getNetworkManagerClient() {
		return this.networkManagerClient;
	}
	
	
}
