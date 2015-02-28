/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.packet;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client messages are prefixed with a '1' (For messages in response to a
 * server) and visa versa
 *
 * The address field specifies both the source and destination address in this
 * format- [SOURCE]:[DES]
 * The server id is always 'SERV'
 * Empty fields or just ":" means broadcast or a packet directed at the server
 *
 * @author St John Giddy @ Strawberry Studios (2015)
 */
public abstract class Packet {
    /* Packet type constants*/

    // Notify to end connection  with the client / server
    public static final int END_CONNECTION = 99;

    // Notify the client what the Client's username is
    public static final int CLIENT_USERNAME = 10;
    // Request the client to send desired client username
    public static final int REQUEST_USERNAME = 11;
    // Request the server the set the username as specified
    public static final int SET_USERNAME = 110;

    public static final int GET_UID = 115;
    public static final int UID = 15;

    // Get list of the users connected
    public static final int GET_USERLIST = 120;
    // DATA is the list of visble users connected
    public static final int USERLIST = 20;

    public static final int SEND_MESSAGE_TO_ROOM = 150;
    public static final int SEND_MESSAGE_TO_USER = 151;
    public static final int MESSAGE = 50;

    public static final int SERVER_INFO = 80;

    public static final int PING = 81;
    public static final int PONG = 181;
    
    /* RESERVED FUTURE CONSTANTS - TO BE IMPLEMENTED */
    private static final int GET_FILELIST = 180;
    private static final int FILELIST = 80;

    /*END RESERVED CONSTANTS*/
    /*Default encoding for all strings*/
    public static final String CHARSET = "UTF-8";

    private final Object rawPacket;

    protected Packet(Object rawPacket) {
        this.rawPacket = rawPacket;
    }

    public abstract int getIns();

    public abstract String getAddress();

    public abstract byte[] getData();

    public Object getRawPacket() {
        return rawPacket;
    }
    
    @Override
    public String toString(){
        try {
            return MessageFormat.format("[{0}][{1}][{2}]", this.getAddress(), this.getIns(), new String(this.getData(), Packet.CHARSET));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet.class.getName()).log(Level.SEVERE, null, ex);
            return MessageFormat.format("[{0}][{1}][{2}]", this.getAddress(), this.getIns(), new String(this.getData()));
        }
        
    }
}
