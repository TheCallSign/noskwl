/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.tests;

import net.strawberrystudios.noskwl.Client;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.strawberrystudios.noskwl.Server;

/**
 *
 * @author giddyc
 */
public class MultiClientTest {


    public static void main(String[] args) throws InterruptedException {
        int port = 7862;
        Server s = Server.getInstance();
        
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Client c = new Client(new PrintWriter(System.out));
            c.setServer("127.0.0.1", port);

            clients.add(c);
        }
        
        while (true) {
            for (Client c : clients) {

                synchronized (c) {
                    c.wait();
                }

                c.sendMessageToAll("Hello from Client " + c.getUsername());
                Thread.sleep(1000);
            }
        }

    }

    private static synchronized void waitForClientConnection(Thread t) {
        try {
            t.wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
