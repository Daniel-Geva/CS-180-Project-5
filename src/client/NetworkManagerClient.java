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

    public NetworkManagerClient (LearningManagementSystemClient lmsc) {
        this.lmsc = lmsc;
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
                    socket = new Socket("localHost", 8080);
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    ois = new ObjectInputStream(socket.getInputStream());
                } catch (UnknownHostException e) {
                    e.printStackTrace(); //TODO: change the error handling and host name
                } catch (IOException e) {
                    e.printStackTrace();
                }

                while (true) {
                    while (packetQueue.size() != 0) {
                        try {
                           RequestPacket request = packetQueue.keySet().iterator().next();
                           ResponsePacketHandler handler = packetQueue.get(request);
                           packetQueue.remove(request);
                           oos.writeObject(request);
                           Object responseObj = ois.readObject();
                           if (!(responseObj instanceof ResponsePacket)) { //TODO: fix printlns
                               System.out.println("Error. Non-response packet received through stream.");
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
                        } catch (EOFException e) { //TODO: replace error handling
                            System.out.println("The server disconnected");
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

    public String connect(String ip)  { //TODO: make it do something
        return null;
    }

    public boolean connectionWasSuccess() { //TODO: make it do something
        return false;
    }

}
