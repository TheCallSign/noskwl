/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.tests;

import net.strawberrystudios.noskwl.server.Server;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.strawberrystudios.noskwl.client.Client;
import net.strawberrystudios.noskwl.packet.Packet;
import static net.strawberrystudios.noskwl.packet.Packet.MESSAGE;
import net.strawberrystudios.noskwl.packet.ObjectPacketFactory;
import net.strawberrystudios.noskwl.server.Server;

/**
 *
 * @author St John
 */
public class SingleClientTest {

    public static void main(String[] args) {
        int port = 7862;
//        Server s = Server.getInstance();
//        s.setStdout(System.out);
//        s.listen(port);
        Client cli = new Client(new PrintWriter(System.out));
//        cli.setServer("127.0.0.1", 7862);
        cli.setServer("10.52.161.111", 7862);
        cli.setStdout(System.out);
        Thread t = new Thread(cli);
//        ServerTest sally = new ServerTest();
        int num = (int)(Math.random()*100);
//        sally.becomeClient();
        synchronized (cli){
            try {
                t.start();
                cli.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SingleClientTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        cli.sendPacket(cli.getPacketFactory().getRawPacket(MESSAGE, ("Hello, I am client "+num).getBytes()));
        
        cli.sendPacket(cli.getPacketFactory().getRawPacket(MESSAGE, "I am also really cool".getBytes()));
        Scanner in = new Scanner(System.in);
        while(true){
            
            cli.sendPacket(cli.getPacketFactory().getRawPacket(Packet.MESSAGE, in.nextLine().getBytes()));
            
        }
    }
    
}
