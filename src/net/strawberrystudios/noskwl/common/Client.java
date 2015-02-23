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
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.strawberrystudios.noskwl.NoSkwl;

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
    
    private final Writer textOut;
    private final PacketFactory pf = new PacketFactory();
    
    public Client(Writer writer) {
        textOut = writer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
    
    @Override
    public void run() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();
        } catch (EOFException e) {
            showMessage("\nConection ended.");
        } catch (IOException ex) {
            Logger.getLogger(NoSkwl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeCrap();
        }
    }

    public void setServer(String ip, int port){
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
        showMessage("\nStreams initialized\n");
    }

    private void whileChatting() throws IOException {
        do {
            try {
                Object packet = (String) input.readObject();
                parseMessage(new ObjectPacket(packet)); 
            } catch (ClassNotFoundException e) {
                showMessage("Invalid message.");
            }
        } while (true);
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
            textOut.append(s);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     private String parseMessage(Packet packet) throws UnsupportedEncodingException{
        int command = packet.getIns();
        byte data[] = packet.getData();
        switch(command){
            case Packet.MESSAGE:
                showMessage(new String(data, "UTF-8"));
        }
        return null;
    }
     
     //send messages to server
    public void sendMessageToAll(String message){
        try{
            output.writeObject(pf.getRawPacket("", Packet.MESSAGE, message.getBytes()));
            output.flush();
        }catch(IOException e){
            showMessage("\nUnexpected failure.");
        }
    }
    
    public void sendPacket(Object p){
        try {
            output.writeObject(p);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
