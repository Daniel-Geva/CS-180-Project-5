package client;

import packets.request.RequestPacket;
import client.ResponsePacketHandler;
import packets.response.ResponsePacket;

import javax.swing.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class NetworkManagerClient {
	LearningManagementSystemClient lmsc;
	HashMap<RequestPacket, ResponsePacketHandler> packetQueue;
	final NameSetter nameSetter;
	Runnable onExit;

	Socket socket;
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;

	public NetworkManagerClient (LearningManagementSystemClient lmsc) {
		this.lmsc = lmsc;
		this.packetQueue = new HashMap<>();
		this.nameSetter = new NameSetter();
	}

	public void init() {
		Thread outputThread = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean success = false;
				do {
					try {
						synchronized (nameSetter) {
							nameSetter.wait();
						}
						socket = new Socket(nameSetter.getName(), 4040);
						oos = new ObjectOutputStream(socket.getOutputStream());
						success = true;
					} catch (IOException e) {
						nameSetter.getErrorRunnable().run();
					} catch (InterruptedException e) {
						return;
					}
				} while (!success);

				while (true) {
					while (packetQueue.size() != 0) {
						try {
							RequestPacket request = packetQueue.keySet().iterator().next();
							oos.writeObject(request);
						} catch (IOException e) {
							return;
						}
					}
				}

			}
		});

		Thread inputThread = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean success = false;
				do {
					try {
						ois = new ObjectInputStream(socket.getInputStream());
						success = true;
					} catch (IOException e) {
						nameSetter.getErrorRunnable().run();
					}
				} while (!success);

				while (true) {
					try {
						Object responseObj = ois.readObject();
						if (!(responseObj instanceof ResponsePacket)) {
							continue;
						}
						ResponsePacket response = (ResponsePacket) responseObj;
						ResponsePacketHandler handler = packetQueue.get(request);
						packetQueue.remove(request);
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								handler.handlePacket(response);
							}
						});
					} catch (IOException e) {
						return;
					} catch (ClassNotFoundException e) {

					}
				}
			}
		});

		inputThread.start();
		outputThread.start();
	}

	public ResponsePacketHandler sendPacket(RequestPacket requestPacket) {
		ResponsePacketHandler handler = new ResponsePacketHandler();
		this.packetQueue.put(requestPacket, handler);
		return handler;
	}

	public void setOnExit(Runnable onExit) {
		this.onExit = onExit;
	}

	public Runnable getOnExit() {
		return onExit;
	}

	class NameSetter {
		String name;
		Runnable errorRunnable;

		public void setErrorRunnable(Runnable errorRunnable) {
			this.errorRunnable = errorRunnable;
		}

		public Runnable getErrorRunnable() {
			return errorRunnable;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}


}
