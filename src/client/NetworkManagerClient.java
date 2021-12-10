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
                            nameSetter.wait(10000);
                        }
                        socket = new Socket(nameSetter.getName(), 4040);
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        connected = true;
                    } catch (IOException e) {
                        nameSetter.getErrorRunnable().run();
                    } catch (InterruptedException e) {
                        return;
                    }
                } while (!connected);

                while (true) {
                    while (packetQueue.size() != 0) {
                        try {
                            RequestPacket request = packetQueue.keySet().iterator().next();
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
                            wait(5000);
                        }
                        ois = new ObjectInputStream(socket.getInputStream());
                        success = true;
                    } catch (IOException | InterruptedException e) {
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
                        if (!response.getPush()) {
                            ResponsePacketHandler handler = queue.remove();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    handler.handlePacket(response);
                                }
                            });
                        } else {
                            for (PushPacketHandler handler : pushPacketHandlers) {
                                if (handler.canHandle(response)) {
                                    handler.handlePacket(response);
                                    return;
                                }
                            }
                        }

                    } catch (IOException e) {
                        return;
                    } catch (ClassNotFoundException e) {
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
