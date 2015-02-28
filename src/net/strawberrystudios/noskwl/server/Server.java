/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.server;

/**
 *
 * @author St John Giddy and Jamie Gregory @ Strawberry Studios (2015)
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.strawberrystudios.noskwl.IllegalPortException;
import net.strawberrystudios.noskwl.packet.Packet;

public class Server extends Thread {

    public static final String VERSION = "0.1";
    public static final String BUILD
            = ResourceBundle.getBundle("version").getString("BUILD");

    private static Server instance;


    /*
     * TODO:
     * ClientWorker is HashMap'ed to a LinkedList //wtf this doesnt make sense
     * The LinkedList contains the following:
     * 0: Server-set username
     * 1: Client-set username
     */
    //  <HashMap of ClientWorker thread and the Client's username
    private static final ConcurrentHashMap<String, ClientWorker> clientMap = new ConcurrentHashMap<>();
    // (constant - connection unique) [UserID], [nickname]  User set (custom)
    private static final ConcurrentHashMap<String, String> nicknameMap = new ConcurrentHashMap<>();
//    private static final List<ClientWorker> clientList = new ArrayList();

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
            return instance;
        } else {
            return instance;
        }
    }

    private ClientManager clientManager;
    
    private int maxClients;

    
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket sockServ;
    private Socket connection;

    private PrintStream stdout;

    private final ExecutorService executor;
    private int port;

    private int counter;

    /**
     * Creates a new instance of NoSkwl Server
     *
     * @param port Port to listen on
     */
    private Server() {

        port = 0;
        maxClients = 64;
        this.stdout = null;
        executor = Executors.newCachedThreadPool();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        }, "Shutdown-thread"));
    }

    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }

    public synchronized void removeClient(String userID) {
        clientMap.remove(userID);
    }

    public synchronized void setClientWorkerUsername(ClientWorker cw, String userID) {
        clientMap.put(userID, cw);
    }

    /**
     * Parse a packet received from a client worker
     *
     * @param packet
     */
    public synchronized void parsePacket(Packet packet) {
        Server.getInstance().log("Got a packet!");
        switch (packet.getIns()) {
            case Packet.MESSAGE:
                for (ClientWorker cw : clientMap.values()) {
                    try {
                        cw.sendMessageToClient(new String(packet.getData(), Packet.CHARSET));
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
        }
    }

    public void setStdout(PrintStream stdout) {
        this.stdout = stdout;
    }

    /**
     * Create a new thread to listen on.
     *
     * @param port Port to listen
     */
    public void listen(int port) {
        clientManager = new ClientManager(maxClients);
        this.port = port;
        this.start();
    }

    /**
     * DO NOT RUN THIS METHOD DIRECTLY TO START THE SERVER Use listen(int port)
     * instead. Running this will cause an exception.
     */@Override
    public void run() {
        if (this.port == 0) {
            try {
                throw new IllegalPortException("Port has not been set, "
                        + "please use server.listen(int) to start the server");
            } catch (IllegalPortException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
        log("NoSkwl Server version " + Server.VERSION + "."
                + Server.BUILD + " listening on port " + port);
        try {
            sockServ = new ServerSocket(port, 100);
            sockServ.setSoTimeout(5000);
            serverLoop();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //wait for connection, display connect info
    private void serverLoop() {
        while (true) {
            Socket clientSock;
            String UID = "NoSkwlUser_" + (clientMap.size() + 1);
            try {
                clientSock = sockServ.accept();
                ClientWorker cw = new ClientWorker(clientSock);
                cw.setClientID(UID);
                executor.submit(cw);
                clientMap.put(UID, cw);
                log("Client connected with UID: " + UID);
            } catch (SocketTimeoutException e) {
                doMaintanance();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void doMaintanance() {
        counter++;
        if (nicknameMap.values().contains("")) {
            for (String uid : nicknameMap.keySet()) {
                if (((String) nicknameMap.get(uid)).isEmpty()) {
                    nicknameMap.put(uid, clientMap.get(uid).getNickname());
                }
            }
        }
        // ping every one second or so, I can do this better with a timer.. TODO: ADD SERVER TIMER :D
        if (counter % 12 == 0) {
            for (ClientWorker cw : clientMap.values()) {
                cw.ping();
//                println("Pinged " + cw.getClientID());
            }
        }
    }

    //close streams & sockets
    private void shutdown() {
        try {
            executor.shutdown();
            output.close();
            input.close();
            connection.close();
            executor.shutdownNow();
        } catch (IOException e) {
        }
    }

    private void announce(String message) {
        for (ClientWorker cw : clientMap.values()) {
            cw.sendMessageToClient(message);
        }
        // push to all clients, notifying them that it is an info message
    }

    public void log(String str) {
        Logger logger = Logger.getLogger(Server.class.getName());
//        logger.
        if (stdout != null) {
            logger.info(str);
//            stdout.println(str);
        }
    }

}