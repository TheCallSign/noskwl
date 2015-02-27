/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl;

import net.strawberrystudios.noskwl.common.Client;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.strawberrystudios.noskwl.common.Server;

/**
 *
 * @author giddyc
 */
public class NoSkwlServer {

    public static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        int port = 7862;
        Server s = new Server(port);
        s.start();
        List<Client> clients = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Client c = new Client(new PrintWriter(System.out));
            c.setServer("127.0.0.1", port);

            clients.add(c);
            threads.add(new Thread(c));
        }
        for (Thread t : threads) {
            t.start();

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
            Logger.getLogger(NoSkwlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
