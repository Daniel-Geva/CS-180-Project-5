package Project Ideas.NetworkTest.src.client;

import java.util.Scanner;

import packets.request.RequestPacketGetData;
import packets.request.RequestPacketSaveData;
import packets.response.ExampleResponsePacket;
import packets.response.ResponsePacketGetData;

public class ExampleUIManager {

	ExampleMainClient mainClient;
	Scanner scanner;
	
	public ExampleUIManager(ExampleMainClient mainClient) {
		this.mainClient = mainClient;
	}

	public void init() {
		this.scanner = new Scanner(System.in);
	}

	public void run() {
		System.out.println("Initializing.");
		this.requestData();
	}
	
	public void requestData() {
		System.out.println("Requesting data!");
		this.mainClient.getExampleNetworkManagerClient()
		.sendPacket(new RequestPacketGetData())
		.onReceiveResponse((ExampleResponsePacket response) -> {
			ResponsePacketGetData responseGetData = (ResponsePacketGetData) response;
			System.out.println("Data Received!");
			System.out.println(responseGetData.getData());
			System.out.println("Please enter the data to send!");
			String data = scanner.nextLine();
			this.sendData(data);
		});
	}
	
	public void sendData(String newData) {
		this.mainClient.getExampleNetworkManagerClient()
		.sendPacket(new RequestPacketSaveData(newData))
		.onReceiveResponse((ExampleResponsePacket response) -> {
			if(response.wasSuccess()) {
				System.out.println("Successfully sent the data!");
				this.requestData();
			}
		});
	}

}
