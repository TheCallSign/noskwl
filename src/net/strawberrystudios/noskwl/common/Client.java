/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.common;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import net.strawberrystudios.noskwl.NoSkwl;
import net.strawberrystudios.noskwl.NoSkwlServer;

/**
 *
 * @author St John Giddy @ Strawberry Studios (2015)
 */
public class Client implements Runnable {

    public static final String VERSION = "0.1.3";

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String remoteIP;
    private int remotePort;
    private Socket connection;
    private String username;
    private String uid;
    private final Writer textOut;
    private final PacketFactory pf = new PacketFactory();

    private final Queue packetQueue;
    public Client(Writer writer) {
        this.packetQueue = new ConcurrentLinkedQueue();
        textOut = writer;
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
            showMessage("\nConection ended.");
        } catch (IOException ex) {
            Logger.getLogger(NoSkwl.class.getName()).log(Level.SEVERE, null, ex);
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

        showMessage("Connecting to server...\n");
        connection = new Socket(InetAddress.getByName(remoteIP), remotePort);
        showMessage("Connected to: " + connection.getInetAddress().getHostName());
        
    }

    //setup IO streams
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("Streams initialized");
        synchronized(this){
            this.notifyAll();
        }
    }

    private void clientLoop() throws IOException {
//        this.connection.setSoTimeout(1000);
        do {
            try {
                if(this.uid == null){
                    getUserID();
                }
                Object p = null;
                try{
                     p = input.readObject();
                } catch(SocketTimeoutException ex){
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
                showMessage("Invalid message.");
            }
        } while (true);
    }

     private String parseMessage(Packet packet) throws UnsupportedEncodingException {
//        System.out.println("GOT COMMAND : "+packet.getIns());
        if(this.uid == null){
            this.uid = packet.getAddress().split(":")[0]; // FIX THIS SHIT
        }
        int command = packet.getIns();
        byte data[] = packet.getData();
        switch (command) {
            case Packet.MESSAGE:
                showMessage(new String(data, "UTF-8"));
                break;
            case Packet.UID:
                this.uid =  new String(data, "UTF-8");
                System.out.println("UID SET!");
                break;
            case Packet.SERVER_INFO:
                showMessage("System infomation: "+new String(data, "UTF-8"));
                break;
        }
        return null;
    }
    
    //close streams and sockets
    private void closeCrap() {
        showMessage("\nDisconnecting...");
        try {
            output.close();
            output.flush();
            input.close();
            connection.close();
        } catch (IOException e) {
        }
    }

    private void showMessage(final String s) {
        try {
            textOut.append("CLI:"+s+"\n");
            textOut.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   

    //send messages to server
    public synchronized void sendMessageToAll(String message) {
        this.sendPacket(pf.getRawPacket("", Packet.MESSAGE, message.getBytes()));

    }

     
    public synchronized void sendPacket(Object p) {
        try {
            output.writeObject(p);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, null, ex);
            showMessage("Send packet failure, connection probably lost");
        }
    }

    private synchronized void getUserID() {
        this.sendPacket(pf.getRawPacket("", Packet.GET_UID, null));
        
    }

}
