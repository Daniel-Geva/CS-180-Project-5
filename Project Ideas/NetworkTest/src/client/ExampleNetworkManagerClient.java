package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.SwingUtilities;

import packets.request.ExampleRequestPacket;
import packets.response.ExampleResponsePacket;

public class ExampleNetworkManagerClient {

	ExampleMainClient mainClient;
	HashMap<ExampleRequestPacket, ExampleResponsePacketHandler> packetQueue;
	
	public ExampleNetworkManagerClient(ExampleMainClient mainClient) {
		this.mainClient = mainClient;
		this.packetQueue = new HashMap<>();
	}
	
	public void init() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				Socket socket;
				ObjectInputStream ois = null;
				ObjectOutputStream oos = null;
				try {
					socket = new Socket("localhost", 8080);
					oos = new ObjectOutputStream(socket.getOutputStream());
					ois = new ObjectInputStream(socket.getInputStream());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				while(true) {
					while(packetQueue.size() != 0) {
						try {
							ExampleRequestPacket request = packetQueue.keySet().iterator().next();
							ExampleResponsePacketHandler handler = packetQueue.get(request);
							packetQueue.remove(request);
							oos.writeObject(request);
							Object responseObj = ois.readObject();
							if(!(responseObj instanceof ExampleResponsePacket)) {
								System.out.println("Error. Non-ResponsePacket received through stream.");
								System.out.println("Not responding to that packet.");
								continue;
							}
							ExampleResponsePacket response = (ExampleResponsePacket) responseObj;
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									handler.handlePacket(response);
								}
								
							});
						} catch (EOFException e) {
							System.out.println("The server disconnected.");
							return;
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		});
		thread.start();
	}

	public ExampleResponsePacketHandler sendPacket(ExampleRequestPacket requestPacket) {
		ExampleResponsePacketHandler handler = new ExampleResponsePacketHandler();
		this.packetQueue.put(requestPacket, handler);
		return handler;
	}

}
