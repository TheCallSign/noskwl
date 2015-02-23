/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.common;

/**
 *
 * @author St John
 */
public class PacketFactory {
    
    private String address;
    
    public PacketFactory(){
         
    }    
    
    public PacketFactory(String address){
        this.address = address;
    }

    public Object getRawPacket(String addr, int ins, byte[] data) { 
        Object packetArray[] = new Object[3];
        packetArray[0] = this.address+":"+addr;
        packetArray[1] = ins;
        packetArray[2] = data;
        return (Object) packetArray;
    }
    
    public Object getRawPacket(int ins, byte[] data) { 
        Object packetArray[] = new Object[3];
        packetArray[0] = this.address+":";
        packetArray[1] = ins;
        packetArray[2] = data;
        return (Object) packetArray;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
}
