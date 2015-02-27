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
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
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
        int port = 7862;
        Server s = new Server(port);
        s.start();
        Client sarah = new Client(new PrintWriter(System.out));
        sarah.setServer("127.0.0.1", 7862);
        Thread t = new Thread(sarah);
//        ServerTest sally = new ServerTest();
        int num = (int)(Math.random()*100);
        PacketFactory pf = new PacketFactory();
//        sally.becomeClient();
        synchronized (sarah){
            try {
                t.start();
                sarah.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        sarah.sendPacket(pf.getRawPacket("", MESSAGE, ("Hello, I am client "+num).getBytes()));
//        sarah.whileChatting();
//        sarah.sendMessage("NAME-NicknameMe");
        
        sarah.sendPacket(pf.getRawPacket("", MESSAGE, "I am also really cool".getBytes()));
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.print("Enter: ");
            pf.getRawPacket(Packet.MESSAGE, in.nextLine().getBytes());
        }
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
   
    

    
}
