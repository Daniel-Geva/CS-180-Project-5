package client;

import java.util.Scanner;

import packets.request.RequestPacketGetData;
import packets.request.RequestPacketSaveData;
import packets.response.ResponsePacket;
import packets.response.ResponsePacketGetData;

public class UIManager {

	MainClient mainClient;
	Scanner scanner;
	
	public UIManager(MainClient mainClient) {
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
		this.mainClient.getNetworkManagerClient()
		.sendPacket(new RequestPacketGetData())
		.onReceiveResponse((ResponsePacket response) -> {
			ResponsePacketGetData responseGetData = (ResponsePacketGetData) response;
			System.out.println("Data Received!");
			System.out.println(responseGetData.getData());
			System.out.println("Please enter the data to send!");
			String data = scanner.nextLine();
			this.sendData(data);
		});
	}
	
	public void sendData(String newData) {
		this.mainClient.getNetworkManagerClient()
		.sendPacket(new RequestPacketSaveData(newData))
		.onReceiveResponse((ResponsePacket response) -> {
			if(response.wasSuccess()) {
				System.out.println("Successfully sent the data!");
				this.requestData();
			}
		});
	}

}
