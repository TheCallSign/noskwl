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

import static java.lang.System.out;
import net.strawberrystudios.noskwl.old.packet.JSONPacket;
import net.strawberrystudios.noskwl.packets.PacketBean;
import net.strawberrystudios.noskwl.packets.MessagePacket;
import org.json.JSONObject;

/**
 *
 * @author giddyc
 */
public class UnitTest {

    public static void main(String[] args) {

        PacketBean data = new MessagePacket();
        data.setValue("HAIII");
        JSONObject packet = new JSONObject(data);
        out.println(packet.toString(1));
        
    }
}
