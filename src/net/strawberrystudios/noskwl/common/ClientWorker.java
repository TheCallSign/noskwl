/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Codes for connectedness (in the instruction field then data in []): 
 * REQUEST-NAME // Server asks for client username and the client responds
 * with: NAME-[USERNAME] // Where USERNAME is the clients username 
 * MSG-[MESSAGE] a normal message to be pushed out to all clients
 * </p>
 *
 * @author St John Giddy
 */
public class ClientWorker implements Runnable {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final Socket sock;
    private String clientUsername;
    private PacketFactory pf = new PacketFactory();
    private String userID;

    public ClientWorker(Socket socket) throws IOException {
        this.sock = socket;
        setupStreams();
        
//        send
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getClientID() {
        return userID;
    }
    public void setClientID(String clientID) {
        this.userID = clientID;
    }

    //setup IO streams
    private synchronized void setupStreams() throws IOException {
        output = new ObjectOutputStream(sock.getOutputStream());
        output.flush();
        input = new ObjectInputStream(sock.getInputStream());

    }

    private synchronized void pushToServer(String message) {

        Server.sendToClients(message);
        
    }

    private void setupAuth() {
        try {
            output.writeObject(pf.getRawPacket("", Packet.REQUEST_USERNAME, null));
            output.flush();
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        String packet = "";
        do {
            try {
                if (clientUsername == null) {
                    setupAuth();
                }
                Object in = input.readObject();
                if (in instanceof String) {
                    packet = (String) in;
                }
                System.out.println(packet);
                parsePacket(packet);

            } catch (ClassNotFoundException | IOException c) {
                break;
            }
        } while (!packet.equals("CLI-END"));
        System.out.println("Client exited with name :" + this.clientUsername);

        this.shutdown();

    }

    private void parsePacket(String packet) {
        if(!packet.contains("-")){
            System.out.println("Strange packet recived from UID" + this.userID);
            return;
        }
        String command = packet.substring(0, packet.indexOf('-'));
        String data = packet.substring(packet.indexOf('-')+1);
        System.out.println(command);
        switch (command) {
            case "MSG":
                pushToServer(data);
                break;
            case "NAME":
                clientUsername = data;
                Server.setClientUsername(this, clientUsername);
                break;
            default:
                System.out.println("Strange packet recived from " + this.userID);
        }
        pushToServer(packet);
    }

    public void sendMessageToClient(String message) {
        this.sendPacketToClient("MSG", message);
    }
    
    public void sendSystemMessageToClient(String message) {
        this.sendPacketToClient("SERV", message);
    }
    
    public void sendPacketToClient(String ins, String message) {
        try {
            output.writeObject(pf.getRawPacket("", Packet.MESSAGE, message.getBytes()));
            output.flush();
            //showMessage("\n" + clientUsername + ": " + message);
        } catch (IOException e) {
            //chatWindow.append("\n Sending failure");
        }
    }

    private void shutdown() {
        try {
            output.close();
            input.close();
            Server.removeClient(userID);
        } catch (IOException ex) {
            Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setUsername(String defaultUsername) {
        this.clientUsername = defaultUsername;
        sendSystemMessageToClient("Your username is now: "+defaultUsername);
    }
}
