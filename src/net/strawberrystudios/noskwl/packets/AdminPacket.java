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
package net.strawberrystudios.noskwl.packets;

/**
 *
 * @author giddyc
 */
public class AdminPacket extends PacketBean {
    /**
     * Change_Username
     * Request_Username
     */
    String instruction;
    /**
     * If true, client has to comply or server will ignore client
     */
    boolean isForced;

    public boolean isIsForced() {
        return isForced;
    }

    public void setIsForced(boolean isForced) {
        this.isForced = isForced;
    }

    public String getType() {
        return instruction;
    }

    public void setType(String type) {
        this.instruction = type;
    }
}
