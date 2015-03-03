/*
 * Copyright (C) 2015 Strawberry Studios
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.strawberrystudios.noskwl.server;

/**
 *
 * @author St John Giddy and Jamie Gregory @ Strawberry Studios (2015)
 */
import com.sun.javafx.font.FontConstants;
import java.io.*;
import java.net.*;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import net.strawberrystudios.noskwl.client.IllegalPortException;
import net.strawberrystudios.noskwl.packet.Packet;

public class Server extends Thread {

    public static final String VERSION = "0.1";
    public static final String BUILD
            = ResourceBundle.getBundle("version").getString("BUILD");

    public static final Logger logger = Logger.getLogger(Server.class.getName());

    private static Server instance;

    static {

//        LogManager.getLogManager().reset();
//        logger.setUseParentHandlers(false);

//        logger.addHandler(new ConsoleHandler());
    }

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
            return instance;
        } else {
            return instance;
        }
    }


    /*
     * TODO:
     * ClientWorker is HashMap'ed to a LinkedList //wtf this doesnt make sense
     * The LinkedList contains the following:
     * 0: Server-set username
     * 1: Client-set username
     */
    //  <HashMap of ClientWorker thread and the Client's username
//    private final ConcurrentHashMap<String, ClientWorker> clientMap = new ConcurrentHashMap<>();
    // (constant - connection unique) [UserID], [nickname]  User set (custom)
//    private final ConcurrentHashMap<String, String> nicknameMap = new ConcurrentHashMap<>();
//    private static final List<ClientWorker> clientList = new ArrayList();
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
        try {

            log(userID + " disconnected.");
            announce(userID + " disconnected.");
            clientManager.removeClient(userID);
        } catch (ItemNotFoundException ex) {
            log("Exception removing client: " + ex.toString());
        }
    }

    public synchronized void setClientWorkerUsername(String uuid, String username) {
        try {
            clientManager.modifyClientUsername(uuid, username);
        } catch (ClientDuplicateException | ItemNotFoundException ex) {
            log("Exception setting Client Username: " + ex.toString());
        }
    }

    /**
     * Parse a packet received from a client worker
     *
     * @param packet
     */
    public synchronized void parsePacket(Packet packet) {
//        Server.getInstance().log("Got a packet!");
        String uuid = packet.getAddress().split(":")[0];
        int command = packet.getIns();
        byte data[] = packet.getData();
        switch (command) {
            case Packet.MESSAGE:
                for (ClientWorker cw : clientManager.getAllWorkers()) {
                    cw.sendPacketToClient(clientManager.getUsername(uuid), Packet.MESSAGE, data);
                }
                break;
            case Packet.SET_USERNAME:
                String clientUsername = "";
                try {
                    clientUsername = new String(data, Packet.CHARSET);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                setClientWorkerUsername(uuid, clientUsername);
                break;
//            case Packet.GET_USERLIST: {
//                try {
//                    clientManager.getWorker(uuid).sendPacketToClient(
//                            Packet.USERLIST, clientManager.getCSVUserlist().getBytes());
//                } catch (ItemNotFoundException ex) {
//                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            break;
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
     */
    @Override
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
            String UID = "NoSkwlUser_" + (clientManager.getSize() + 1);
            try {
                clientSock = sockServ.accept();
                ClientWorker cw = new ClientWorker(clientSock);
                cw.setClientID(UID);
                try {
                    clientManager.addClient(cw, "", UID);
                } catch (ClientDuplicateException ex) {
                    log(ex.getLocalizedMessage());
                    continue;
                } catch (ServerFullException ex) {
                    cw.sendPacketToClient(Packet.SERVER_FULL, null);
                    continue;
                }
                executor.submit(cw);
                log("Client connected with UID: " + UID);
                announce(cw.getNickname()+" has joined the dark side.");
            } catch (SocketTimeoutException e) {
//                doMaintanance();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void doMaintanance() {
        counter++;
        // ping every one second or so, I can do this better with a timer.. TODO: ADD SERVER TIMER :D
        if (counter % 12 == 0) {
            for (ClientWorker cw : clientManager.getAllWorkers()) {
                if (cw != null) {
                    cw.ping();
                }
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
        } catch (IOException | NullPointerException e) {
        }
    }

    public void announce(String message) {
        for (ClientWorker cw : clientManager.getAllWorkers()) {
            cw.sendMessageToClient(message);
        }
        // push to all clients, notifying them that it is an info message
    }

    public void log(String str) {
//        logger.
        if (stdout != null) {
            logger.info(str);
//            stdout.println(str);
        }
    }
    
    public Object getClientManagerLock(){
        return ClientManager.lock;
    }

}
