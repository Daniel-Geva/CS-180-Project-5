package Project Ideas.client;

public class ExampleMainClient {

	ExampleUIManager uiManager;
	ExampleNetworkManagerClient networkManagerClient;
	
	public static void main(String[] args)  {
		ExampleMainClient mainClient = new ExampleMainClient();
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

	public ExampleMainClient() {
		this.uiManager = new ExampleUIManager(this);
		this.networkManagerClient = new ExampleNetworkManagerClient(this);
	}

	public ExampleUIManager getExampleUIManager() {
		return this.uiManager;
	}

	public ExampleNetworkManagerClient getExampleNetworkManagerClient() {
		return this.networkManagerClient;
	}
	
	
}
