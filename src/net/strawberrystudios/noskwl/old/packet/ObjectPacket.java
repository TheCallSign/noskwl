/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.old.packet;

import java.util.Arrays;

/**s
 *
 * @author St John Giddy @ Strawberry Studios (2015)
 */
public class ObjectPacket extends Packet {

    private final Object packetArray[] = new Object[3];

    
    public ObjectPacket(Object rawPacket) {
        super(rawPacket);
        if(rawPacket instanceof Object[]) {
            Object[] arr = (Object[]) rawPacket;
            packetArray[0] = arr[0];
            packetArray[1] = arr[1];
            packetArray[2] = arr[2];
        } else if (rawPacket instanceof Packet) {
            Packet p = (Packet) rawPacket;
            packetArray[0] = p.getAddress();
            packetArray[1] = p.getIns();
            packetArray[2] = p.getData();
        } else throw new IllegalArgumentException("Object is not recognised");
    }

    
    
    @Override
    public String getAddress() {
        return (String) packetArray[0];
    }

    @Override
    public int getIns() {
        return (int) packetArray[1];
    }

    @Override
    public byte[] getData() {
        return (byte[]) packetArray[2];
    }
    
    
    
}
