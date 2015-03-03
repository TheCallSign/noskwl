/*
 * Copyright (C) 2015 giddyc
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.strawberrystudios.noskwl.tests;

import java.io.IOException;
import static java.lang.System.out;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.strawberrystudios.noskwl.packet.Data;
import net.strawberrystudios.noskwl.server.ClientDuplicateException;
import net.strawberrystudios.noskwl.server.ClientManager;
import net.strawberrystudios.noskwl.server.ClientWorker;
import net.strawberrystudios.noskwl.server.ItemNotFoundException;

/**
 *
 * @author giddyc
 */
public class UnitTest {

    public static void main(String[] args) {
//        Data <Integer> d = new Data<>(Integer.class, 32);
//        Data p = d;
//        out.println(d);
//        out.println(d.getType());
//        out.println(d.getData());
//        out.println("==============");
//        out.println(p);
//        out.println(p.getType());
////        out.println(p.getData());
//        ClientManager cm = new ClientManager(43);
//        ArrayList<Thread> threads = new ArrayList<>();
//        try {
//            out.println("ere?w333");
//
//            ServerSocket s = new ServerSocket(9999);
//            Thread serverThread = new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    
//                }
//            });
//            out.println("er222222");
//
////            ClientWorker cw1 = new ClientWorker(new Socket("localhost", 9999));
//            
//            out.println("ere3333212?");
//            threads.add(new Thread(cw1));
//            for(Thread t : threads){
//                t.start();
//            }
//            out.println("ere?");
//            cm.addClient(cw1, "Bob", "123");
//            cm.addClient(null, "Tuoo", "321");
//            cm.addClient(null, "Cmpoe", "213");
//            cm.addClient(null, "Dylan", "32123");

//            out.println(cm.getWorker("123"));
//        } catch (ClientDuplicateException | IOException | ItemNotFoundException ex) {
//            Logger.getLogger(UnitTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
