package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.SwingUtilities;

import packets.request.RequestPacket;
import packets.response.ResponsePacket;

public class NetworkManagerClient {

	MainClient mainClient;
	HashMap<RequestPacket, ResponsePacketHandler> packetQueue; 
	
	public NetworkManagerClient(MainClient mainClient) {
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
							RequestPacket request = packetQueue.keySet().iterator().next();
							ResponsePacketHandler handler = packetQueue.get(request);
							packetQueue.remove(request);
							oos.writeObject(request);
							Object responseObj = ois.readObject();
							if(!(responseObj instanceof ResponsePacket)) {
								System.out.println("Error. Non-ResponsePacket received through stream.");
								System.out.println("Not responding to that packet.");
								continue;
							}
							ResponsePacket response = (ResponsePacket) responseObj;
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

	public ResponsePacketHandler sendPacket(RequestPacket requestPacket) {
		ResponsePacketHandler handler = new ResponsePacketHandler();
		this.packetQueue.put(requestPacket, handler);
		return handler;
	}

}
