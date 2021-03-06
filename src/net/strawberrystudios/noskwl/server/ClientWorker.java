/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.strawberrystudios.noskwl.old.packet.ObjectPacket;
import net.strawberrystudios.noskwl.old.packet.Packet;
import net.strawberrystudios.noskwl.old.packet.ObjectPacketFactory;
import net.strawberrystudios.noskwl.packets.PacketBean;
import org.json.JSONObject;

/**
 * <p>
 * Codes for connectedness (in the instruction field then data in []):
 * REQUEST-NAME // Server asks for client username and the client responds with:
 * NAME-[USERNAME] // Where USERNAME is the clients username MSG-[MESSAGE] a
 * normal message to be pushed out to all clients
 * </p>
 *
 * @author St John Giddy
 */
public class ClientWorker implements Runnable {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final Socket sock;
    private String clientUsername;
    private final ObjectPacketFactory pf = new ObjectPacketFactory();
    private String uuid;

    private int pingSeq = 0;

    public ClientWorker(Socket socket) throws IOException {
        this.sock = socket;
        setupStreams();

//        send
    }

    public String getNickname() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getClientID() {
        return uuid;
    }

    public void setClientID(String clientID) {
        this.uuid = clientID;
        pf.setUID(uuid);
    }

    //setup IO streams
    private synchronized void setupStreams() throws IOException {
        output = new ObjectOutputStream(sock.getOutputStream());
        output.flush();
        input = new ObjectInputStream(sock.getInputStream());

    }

    private synchronized void pushToServer(PacketBean packet) {
        Server.getInstance().parsePacket(packet);

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
                JSONObject p = input.readUTF();
               
                this.parsePacket(p);

            } catch (ClassNotFoundException e) {
            } catch (IOException c) {
                break;
            }
        } while (true);
        System.out.println("Client exited with name: " + this.uuid);

        this.shutdown();

    }

    private void parsePacket(PacketBean packet) throws UnsupportedEncodingException {
        
        
        String value = packet.getValue();
        
        switch (packet.getType()) {
            case MESSAGE:
                pushToServer(packet);
                break;
            case CONTROL:
                parseControlPacket(packet);
                
        }
    }

    
    public void sendPacketToClient(PacketBean packet) {
        try {
            JSONObject object = new JSONObject(packet);
            output.writeUTF(object.toString());
            output.flush();
            //showMessage("\n" + clientUsername + ": " + message);
        } catch (IOException e) {
            //chatWindow.append("\n Sending failure");
        }
    }

    public void sendPacketToClient(String addr, int ins, byte[] bytes) {
        String sourceAddr = addr.split(":")[0];
        try {
            output.writeObject(pf.getRawPacket(sourceAddr + ":" + this.uuid, ins, bytes));
            output.flush();
            //showMessage("\n" + clientUsername + ": " + message);
        } catch (IOException e) {
            //chatWindow.append("\n Sending failure");
        }
    }

    public void sendPongToClient(Packet p) {
        try {
            output.writeObject(pf.getRawPacket("SERV:" + this.uuid, Packet.PONG, p.getData()));
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
            Server.getInstance().removeClient(uuid);
        } catch (IOException ex) {
            Logger.getLogger(ClientWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void setUsername(String defaultUsername) {
        this.clientUsername = defaultUsername;
        sendSystemMessageToClient("Your username is now: " + defaultUsername);
    }

    private void showMessage(String str) {
        System.out.println(str);
    }

    void ping() {
        sendPacketToClient(Packet.PING, (pingSeq + "").getBytes());
        pingSeq++;
    }

    private void parseControlPacket(PacketBean packet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
