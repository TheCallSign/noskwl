/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.strawberrystudios.noskwl.packet;

/**
 *
 * @author St John
 */
public class PacketFactory {
    
    private String uuid;
    
    public PacketFactory(){
        this.uuid = "";
         
    }    
    
    public PacketFactory(String address){
        this.uuid = address;
    }

    public Object getRawPacket(String addr, int ins, byte[] data) { 
        Object packetArray[] = new Object[3];
        packetArray[0] = addr;
        packetArray[1] = ins;
        packetArray[2] = data;
        return (Object) packetArray;
    }
    
    public Object getRawPacket(int ins, byte[] data) { 
        Object packetArray[] = new Object[3];
        packetArray[0] = uuid+":SERV";
        packetArray[1] = ins;
        packetArray[2] = data;
        return (Object) packetArray;
    }

    public void setUID(String address) {
        this.uuid = address;
    }

    
}
