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
import net.strawberrystudios.noskwl.common.Server;

/**
 *
 * @author giddyc
 */
public class NoSkwlServer {
    public static void main(String[] args) throws InterruptedException {
        int port = 7862;
        Server s = new Server(port);
        s.start();
        Client c = new Client(new PrintWriter(System.out));
        c.setServer("127.0.0.1", port);
        Thread t = new Thread(c);
        t.start();
        while(t.isAlive()){
            c.sendMessageToAll("Hello!!");
            Thread.sleep(1000);
        }
    }
}
