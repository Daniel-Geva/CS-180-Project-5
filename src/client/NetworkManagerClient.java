package client;

import datastructures.PushPacketHandler;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Runs the client for the Learning Management System
 * <p>
 * sends information to the server about what the changes the client has made and requests information the client needs as well as receiving information from the server.
 *
 * @author Daniel Geva, Isaac Fleetwood
 *
 * @version December 10, 2021
 *
 */

public class NetworkManagerClient {
    LearningManagementSystemClient lmsc;
    HashMap<RequestPacket, ResponsePacketHandler> packetQueue;
    final NameSetter nameSetter;
    Runnable onExit;
    boolean connected = false;

    Socket socket;
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;

    Queue<ResponsePacketHandler> queue = new LinkedList();
    ArrayList<PushPacketHandler> pushPacketHandlers = new ArrayList<>();

    public NetworkManagerClient (LearningManagementSystemClient lmsc) {
        this.lmsc = lmsc;
        this.packetQueue = new HashMap<>();
        this.nameSetter = new NameSetter();
    }

    public void init() {
        Thread outputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        synchronized (nameSetter) {
                            System.out.println("waiting");
                            nameSetter.wait();
                        }
                        System.out.println("done waiting");
                        socket = new Socket(nameSetter.getName(), 4040);
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        connected = true;
                        System.out.println("connected");
                        SwingUtilities.invokeLater(nameSetter.getSuccessRunnable());
                    } catch (IOException e) {
                        SwingUtilities.invokeLater(nameSetter.getErrorRunnable());
                    } catch (InterruptedException e) {
                        return;
                    }
                } while (!connected);

                while (true) {
                	synchronized(packetQueue) {
                		try {
							packetQueue.wait();
						} catch (InterruptedException e) {
							return;
						}
                	}
                    while (packetQueue.size() != 0) {
                        try {
                            RequestPacket request = packetQueue.keySet().iterator().next();
                            System.out.println("Sent " + request);
                            queue.add(packetQueue.get(request));
                            oos.writeObject(request);
                            packetQueue.remove(request);
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
                        while (!connected) {
                            synchronized(this) {
                                System.out.println("thread 2 waiting");
                            	this.wait(5000);
                            }
                        }
                        ois = new ObjectInputStream(socket.getInputStream());
                        System.out.println("thread 2 connected");
                        success = true;
                    } catch (IOException | InterruptedException e) {
                        nameSetter.getErrorRunnable().run();
                    }
                } while (!success);
                while (true) {
                    try {
                        Object responseObj = ois.readObject();
                        System.out.println("Response1");
                        if (!(responseObj instanceof ResponsePacket)) {
                            continue;
                        }
                        ResponsePacket response = (ResponsePacket) responseObj;
                        System.out.println("Response");
                        if (!response.getPush()) {
                            ResponsePacketHandler handler = queue.remove();
                            System.out.println(handler);
                            SwingUtilities.invokeLater(() -> {
                                handler.handlePacket(response);
                            });
                        } else {
                            for (PushPacketHandler handler : pushPacketHandlers) {
                                if (handler.canHandle(response)) {
                                    handler.handlePacket(response);
                                    return;
                                }
                            }
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        return;
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
    	synchronized(packetQueue) {
    		packetQueue.notifyAll();
    	}
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
        Runnable successRunnable;

        public void setErrorRunnable(Runnable errorRunnable) {
            this.errorRunnable = errorRunnable;
        }

        public Runnable getErrorRunnable() {
            return errorRunnable;
        }

        public Runnable getSuccessRunnable() {
			return successRunnable;
		}

		public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

		public void setSuccessRunnable(Runnable runnable) {
			this.successRunnable = runnable;
		}
    }


}
