/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.common;

/**
 * Client messages are prefixed with a '1' (For messages in respone to a server)
 * and visa versa
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
    
    // Get list of the users connected
    public static final int GET_USERLIST = 120;
    // DATA is the list of visble users connected
    public static final int USERLIST = 20;
    
    public static final int SEND_MESSAGE_TO_ROOM = 150;
    public static final int SEND_MESSAGE_TO_USER = 151;
    public static final int MESSAGE = 50;
    
    public static final int SERVER_BROADCAST = 80;
    
    /* RESERVED FUTURE CONSTANTS - TO BE IMPLEMENTED */
    
    private static final int GET_FILELIST = 180;
    private static final int FILELIST = 80;
    
    /*END RESERVED CONSTANTS*/
    
    
    private final Object rawPacket;
    
    protected Packet(Object rawPacket){
        this.rawPacket = rawPacket;
    }
    
    public abstract int getIns();
    public abstract String getAddress();
    public abstract byte[] getData();
    
    public Object getRawPacket(){
        return rawPacket;
    }
}
