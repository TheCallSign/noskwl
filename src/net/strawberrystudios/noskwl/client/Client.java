/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import static java.lang.System.out;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.strawberrystudios.noskwl.server.ClientWorker;
import net.strawberrystudios.noskwl.packet.ObjectPacket;
import net.strawberrystudios.noskwl.packet.Packet;
import net.strawberrystudios.noskwl.packet.PacketFactory;

/**
 *
 * @author St John Giddy @ Strawberry Studios (2015)
 */
public class Client implements Runnable {

    public static final String VERSION = "0.1.3";
    public static final Logger logger = Logger.getLogger(Client.class.getName());

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String remoteIP;
    private int remotePort;
    private Socket connection;
    private String username;
    private String uid;

    private final Writer textOut;
    private final PacketFactory pf;

    private final Queue packetQueue;
    private PrintStream stdout;

    public Client(Writer writer) {
        this.packetQueue = new ConcurrentLinkedQueue();
        textOut = writer;
        pf = new PacketFactory();
    }

    public PacketFactory getPacketFactory() {
        return pf;
    }

    public String getUid() {
        return uid;
    }

    public void setStdout(PrintStream stdout) {
        this.stdout = stdout;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.sendPacket(pf.getRawPacket(Packet.SET_USERNAME, username.getBytes()));
    }

    @Override
    public void run() {
        try {
            connectToServer();
            setupStreams();
            clientLoop();
        } catch (EOFException e) {
            println("\nConection ended.");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeCrap();
        }
    }

    public void setServer(String ip, int port) {
        remoteIP = ip;
        remotePort = port;
    }

    //conect to server
    private void connectToServer() throws IOException {

        println("Connecting to server...\n");
        connection = new Socket(InetAddress.getByName(remoteIP), remotePort);
        println("Connected to: " + connection.getInetAddress().getHostName());

    }

    //setup IO streams
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        println("Streams initialized");
        synchronized (this) {
            this.notifyAll();
        }
    }

    private void clientLoop() throws IOException {
//        this.connection.setSoTimeout(1000);
        do {
            try {
                if (this.uid == null) {
                    getUIDFromServer();
                }
                Object p = null;
                try {
                    p = input.readObject();
                } catch (SocketTimeoutException ex) {
                    continue;
                }
                Object packet[] = null;
                if (p instanceof Object[]) {
//                    out.println(this.uid+":I GOTTA PACKET!");
                    packet = (Object[]) p;
                } else if (p instanceof String) {
                    System.out.println("GOT STRING PACKET WTF: " + (String) p + "FROM CLIENT UID" + this.getUsername());
                    return;
                }
                parseMessage(new ObjectPacket(packet));
            } catch (ClassNotFoundException e) {
                println("Invalid message.");
            }
        } while (true);
    }

    private String parseMessage(Packet packet) throws UnsupportedEncodingException {
//        System.out.println("GOT COMMAND : "+packet.getIns());

        int command = packet.getIns();
        byte data[] = packet.getData();
        switch (command) {
            case Packet.MESSAGE:
                println(new String(data, "UTF-8"));
                break;
            case Packet.UID:
                pf.setUID(uid);
                this.uid = new String(data, "UTF-8");
//                out.println("UID: "+this.uid);
                break;
            case Packet.SERVER_INFO:
                println("System infomation: " + new String(data, "UTF-8"));
                break;
            case Packet.PING:
                sendPacket(pf.getRawPacket(Packet.PONG, null));
        }
        return null;
    }

    //close streams and sockets
    private void closeCrap() {
        println("\nDisconnecting...");
        try {
            output.close();
            output.flush();
            input.close();
            connection.close();
        } catch (IOException e) {
        }
    }

    //send messages to server
    public synchronized void sendMessageToAll(String message) {
        if (!message.equals("")) {
            this.sendPacket(pf.getRawPacket(Packet.MESSAGE, message.getBytes()));
        }

    }

    public synchronized void sendPacket(Object p) {
        try {
            output.writeObject(p);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, null, ex);
            println("Send packet failure, connection probably lost");
        }
    }

    private synchronized void getUIDFromServer() {
        this.sendPacket(pf.getRawPacket("", Packet.GET_UID, null));

    }

    private void println(String str) {
//        out.println(stdout);
        if (stdout != null) {

            ((PrintStream) stdout).println(str + "\n");

        } 
        if(textOut != null){
            try {
                textOut.append(str + "\n");
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
