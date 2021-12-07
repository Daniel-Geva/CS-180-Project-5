package server;


import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import packets.request.*;
import packets.response.*;

/**
 * Runs the server for the Learning Management System
 * <p>
 * Waits for the user to request or send objects and sends them to the client accordingly
 *
 * @author Liam Kelly
 *
 * @version December 7, 2021
 *
 */


public class NetworkManagerServer {

    LearningManagementSystemServer lmsServer;

    public NetworkManagerServer(LearningManagementSystemServer lmsServer) {
        this.lmsServer = lmsServer;
    }
    /**
     * Runs when initialized
     *
     * Starts the server and waits for the user to send a request packet
     *
     */
    public void init() {
        try {
            @SuppressWarnings("resource")
            ServerSocket ss = new ServerSocket(8080);

            while(true) {
                final Socket socket = ss.accept();
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while(true) {
                            try {
                                Object obj = ois.readObject();
                                if(!(obj instanceof RequestPacket)) {
                                    System.out.println("Error. Non-RequestPacket sent through stream.");
                                    System.out.println("Not responding to that packet.");
                                    continue;
                                }
                                RequestPacket requestPacket = (RequestPacket) obj;
                                ResponsePacket response = requestPacket.serverHandle(lmsServer);

                                oos.writeObject(response);
                            } catch (EOFException e) {
                                // Client disconnected.
                                return;
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                continue;
                            } catch (IOException e) {
                                e.printStackTrace();
                                continue;
                            }

                        }
                    }

                });
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
