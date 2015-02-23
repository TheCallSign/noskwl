/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl;

import net.strawberrystudios.noskwl.common.Server;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import net.strawberrystudios.noskwl.common.Client;
import net.strawberrystudios.noskwl.NoSkwl;
import net.strawberrystudios.noskwl.common.ObjectPacket;
import net.strawberrystudios.noskwl.common.Packet;
import static net.strawberrystudios.noskwl.common.Packet.*;
import net.strawberrystudios.noskwl.common.PacketFactory;

/**
 *
 * @author St John
 */
public class ClientTest {

    public static void main(String[] args) {
        ClientTest sarah = new ClientTest();
//        ServerTest sally = new ServerTest();
        sarah.logger = Logger.getLogger(sarah.getClass().getName());
        int num = (int)(Math.random()*100);
        sarah.becomeClient();
        PacketFactory pf = new PacketFactory();
//        sally.becomeClient();
        sarah.sendPacket(pf.getRawPacket("", MESSAGE, ("Hello, I am client "+num).getBytes()));
//        sarah.whileChatting();
//        sarah.sendMessage("NAME-NicknameMe");
        
        sarah.sendPacket(pf.getRawPacket("", MESSAGE, "I am also really cool".getBytes()));
        while(true){
        try {
            sarah.whileChatting();
        } catch (IOException ex) {
            Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }}
//        sarah.closeCrap();
    }
    
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private final String remoteIP  = "127.0.0.1";
    private final int remotePort = 7862;
    private Socket connection;
    private Server server;
    private Logger logger;
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

    //while chatting with server
    private void whileChatting() throws IOException {
            try {
                Packet p = (Packet) input.readObject();
                parsePacket(p);
            } catch (ClassNotFoundException e) {
                showMessage("Invalid message.");
            }
    }
    @SuppressWarnings("fallthrough")
    private String parsePacket(Packet packet) {
//        System.out.println(packet);
        
        int command = packet.getIns();
        String data = "";
        try {
            data = new String(packet.getData(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println(command);
        switch (command) {
            case Packet.SERVER_BROADCAST:
                data = "Server Infomation: " + data;
            case Packet.MESSAGE:
                showMessage(data);
                break;
            
        }
        return null;
    }

    //close streams and sockets
    private void closeCrap(){
        showMessage("\nDisconnecting...");
        try {
            output.close();
            output.flush();
            input.close();
            connection.close();
        } catch (IOException e) {
        }
    }

    //send messages to server
    public void sendPacket(Object p){
        try {
            output.writeObject(p);
            output.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //update chat window
    private void showMessage(final String s) {
        System.out.println(s);
    }
    private void becomeClient() {
        try {
            connectToServer();
            setupStreams();
            whileChatting();
        } catch (EOFException e) {
            showMessage("\nConection ended.");
        } catch (IOException ex) {
            Logger.getLogger(NoSkwl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
        }

    }

    

    
}
