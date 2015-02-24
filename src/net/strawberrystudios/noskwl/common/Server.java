/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.common;

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
import net.thecallsign.common.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread implements Runnable {

    public static final String VERSION = "0.1";
    public static final String BUILD
            = ResourceBundle.getBundle("version").getString("BUILD");

    private static Stack messageStack;
    /*
     * TODO:
     * ClientWorker is HashMap'ed to a LinkedList //wtf this doesnt make sense
     * The LinkedList contains the following:
     * 0: Server-set username
     * 1: Client-set username
     */
    //  <HashMap of ClientWorker thread and the Client's username
    private static final ConcurrentHashMap<String, ClientWorker> clientMap = new ConcurrentHashMap();
    // (constant - connection unique) [UserID], [nickname]  User set (custom)
    private static final ConcurrentHashMap<String, String> nicknameMap = new ConcurrentHashMap();
//    private static final List<ClientWorker> clientList = new ArrayList();

    public static synchronized void removeClient(String userID) {
        clientMap.remove(userID);
    }

    public static synchronized void setClientUsername(ClientWorker cw, String userID) {
        clientMap.put(userID, cw);
    }

    /**
     * Parse a packet received from a client worker
     *
     * @param packet
     */
    public static synchronized void parsePacket(Packet packet) {
        System.out.println("SERVER GOT MESSAGE PUSH: "+packet.getIns());
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

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket sockServ;
    private Socket connection;

    private final ExecutorService executor;
    private final int port;

    /**
     * Creates a new instance of NoSkwl Server
     *
     * @param port Port to listen on
     */
    public Server(int port) {
        executor = Executors.newCachedThreadPool();
        this.port = port;
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                shutdown();
            }
        }, "Shutdown-thread"));
    }

    //set up and run server
    @Override
    public void run() {
        try {
            System.out.println("NoSkwl Server version " + Server.VERSION + "." + Server.BUILD);
            sockServ = new ServerSocket(port, 100);
            sockServ.setSoTimeout(5000);
            while (true) {
                try {

                    while (true) {
                        try {
                            waitForConnection();
                        } catch (EOFException eofException) {
                            System.out.println("\n Sever ended the connection"); // look at jamie's spelling
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);

                } finally {

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //wait for connection, display connect info
    private synchronized void waitForConnection() throws IOException {
        Socket clientSock;
        String UID = "NoSkwlUser_" + (clientMap.size() + 1);
        try {
            clientSock = sockServ.accept();
        } catch (SocketTimeoutException e) {
            doMaintanance();
            announce("Hello mah preps!");
            return;
        }
        ClientWorker cw = new ClientWorker(clientSock);
        cw.setUsername(UID);
        executor.submit(cw);
        clientMap.put(UID, cw);
        System.out.println("Client connected with UID: " + UID);
        //showMessage("Connected to" + connection.getInetAddress().getHostName() + "\n");
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

    private void doMaintanance() {
        if(nicknameMap.values().contains("")){
            for(String uid : nicknameMap.keySet()){
                if(((String) nicknameMap.get(uid)).equals("")){
                    nicknameMap.put(uid, clientMap.get(uid).getNickname());
                }
            }
        }
    }

}
