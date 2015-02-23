/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import static java.lang.System.out;
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
    private final PacketFactory pf = new PacketFactory();
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
        pf.setAddress(userID);
    }

    //setup IO streams
    private synchronized void setupStreams() throws IOException {
        output = new ObjectOutputStream(sock.getOutputStream());
        output.flush();
        input = new ObjectInputStream(sock.getInputStream());

    }

    private synchronized void pushToServer(Packet packet)  {

        Server.parsePacket(packet);
        
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
        Object rawPacket[] = null;
        do {
            try {
                Object p = input.readObject();
                if (p instanceof Object[]) {
                    rawPacket = (Object[]) p;
                } else if (p instanceof String) {
                    showMessage("Got a string packet lolwut?");
                }
                this.parsePacket(new ObjectPacket(rawPacket));

            } catch (ClassNotFoundException | IOException c) {
                break;
            }
        } while (true);
        System.out.println("Client exited with name :" + this.clientUsername);

        this.shutdown();

    }

    private void parsePacket(Packet packet) throws UnsupportedEncodingException {
        
        if(!packet.getAddress().split(":", 2)[0].equals(this.userID)){
            this.sendSystemMessageToClient("UID OUT OF SYNC, SENDING YOUR UID");
            out.println("UID OUT OF SYNC");
            this.sendPacketToClient(Packet.UID, userID);
        }
        out.println("UID  IN SYNC");
        int command = packet.getIns();
        byte data[] = packet.getData();
        System.out.println("From "+this.userID+command);
        switch (command) {
            case Packet.MESSAGE:
                pushToServer(packet);
                break;
            case Packet.SET_USERNAME:
                clientUsername = new String(data, Packet.CHARSET);
                Server.setClientUsername(this, clientUsername);
                break;
            default:
                System.out.println("Strange packet recived from " + this.userID);
        }
        pushToServer(packet);
    }

    public void sendMessageToClient(String message) {
        this.sendPacketToClient(Packet.MESSAGE, message);
    }
    
    public void sendSystemMessageToClient(String message) {
        this.sendPacketToClient(Packet.SERVER_INFO, message);
    }
    
    public void sendPacketToClient(int ins, String message) {
        try {
            output.writeObject(pf.getRawPacket("SERV:"+this.userID, ins, message.getBytes()));
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

    private void showMessage(String str) {
       System.out.println(str);
    }
}
