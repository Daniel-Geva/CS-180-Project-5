package server;


import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import packets.request.RequestPacket;
import packets.response.ResponsePacket;

/**
 * Runs the server for the Learning Management System
 * <p>
 * Waits for the user to request or send objects and sends them to the client accordingly
 *
 * @author Liam Kelly
 * @author Sean Lee
 *
 * @version December 11, 2021
 *
 */


public class NetworkManagerServer {

    LearningManagementSystemServer lmsServer;

    public NetworkManagerServer(LearningManagementSystemServer lmsServer) {
        this.lmsServer = lmsServer;
    }

    /**
     * Runs when initialized
     * <p>
     * Starts the server and waits for the user to send a request packet
     */
    public void init() {
        try {
            @SuppressWarnings("resource")
            ServerSocket ss = new ServerSocket(4040);
            List<ArrayDeque<ResponsePacket>> stacks = new ArrayList<>();

            while (true) {
            	final ArrayDeque<ResponsePacket> stack = new ArrayDeque<ResponsePacket>();
            	synchronized (stacks) {
            		stacks.add(stack);
            	}
                final Socket socket = ss.accept();
                final ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                final ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (true) {
                            try {
                            	// Read the packet.
                                Object obj = ois.readObject();
                                if (!(obj instanceof RequestPacket)) {
                                    continue;
                                }
                                // Should always be RequestPacket, so cast it.
                                RequestPacket requestPacket = (RequestPacket) obj;
                                
                                // Handle the packet and get corresponding response.
                                ResponsePacket response = requestPacket.serverHandle(lmsServer);
                                
                                // Send the packet to the user that requested is as a normal (non-push) packet.
                                boolean push = response.getPush();
                                response.setPush(false);
                                synchronized (oos) {
                                	oos.writeObject(response);
                                	oos.reset();
                                }
                                response.setPush(push);
                                
                                // If it is to be pushed, send it to everyone else.
                                if (response.getPush()) {
                                    synchronized (stacks) {
                                    	for (ArrayDeque<ResponsePacket> extStack: stacks) {
                                    		synchronized (extStack) {
                                                extStack.push(response);
                                                extStack.notifyAll();
                                    		}
                                    	}
                                    }
                                }
                                
                                
                            } catch (EOFException e) {
                                return;
                            } catch (ClassNotFoundException e) {
                                continue;
                            } catch (IOException e) {
                                continue;
                            }

                        }
                    }

                });

	            Thread thread2 = new Thread(new Runnable() {
	                @Override
	                public void run() {
	                    while (true) {
	                        synchronized (stack) {
	                        	try {
									stack.wait();
								} catch (InterruptedException e1) { return; }
	                            while (!stack.isEmpty()) {
	                                try {
	                                    synchronized (oos) {
	                                    	oos.writeObject(stack.pop());
	                                    	oos.reset();
	                                    }
	                                } catch (IOException e) { 
	                                	return;
	                                }
	                            }
	                        }
	                    }
	                }
	            });
                thread.start();
                thread2.start();
            }

        } catch (IOException e) {
            return;
        }

    }
}
