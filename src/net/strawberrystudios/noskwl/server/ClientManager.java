/*
 * Copyright (C) 2015 St John
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
package net.strawberrystudios.noskwl.server;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.strawberrystudios.noskwl.packet.Packet;

/**
 *
 * @author St John Giddy @ Strawberry Studios (2015)
 */
public class ClientManager {

    private final ClientWorker clientWorkerList[];
    private final String clientUsernameList[];
    private final String clientUUIDList[];

    // false means empty, true means occupied.
    private final boolean[] clientSlots;

    private int head;
    private int size;
    private final int maxSize;
    private int free;

    public ClientManager(int maxSize) {
        this.maxSize = maxSize;
        clientSlots = new boolean[maxSize];
        clientWorkerList = new ClientWorker[maxSize];
        clientUsernameList = new String[maxSize];
        clientUUIDList = new String[maxSize];
        head = 0;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getFree() {
        return free;
    }

    public int getSize() {
        return size;
    }

    public synchronized void addClient(ClientWorker cw, String clientUsername, String clientUUID) throws ClientDuplicateException {
        if (Arrays.asList(clientUsernameList).contains(clientUsername)
                || Arrays.asList(clientUsernameList).contains(clientUUID)) {
            throw new ClientDuplicateException();
        }
        refreshFreeSpace();
        head = getFirstOpenSpace();
        clientWorkerList[head] = cw;
        clientUsernameList[head] = clientUsername;
        clientUUIDList[head] = clientUUID;
        clientSlots[head] = true;
        size++;

    }

    public void modifyClientUsername(ClientWorker cw, String newClientUsername) throws ClientDuplicateException, ItemNotFoundException {
        if (Arrays.asList(clientUsernameList).contains(newClientUsername)) {
            throw new ClientDuplicateException();
        }
        boolean itemFound = false;
        for (int i = 0; i < maxSize; i++) {
            if (clientWorkerList[i] == cw) {
                clientUsernameList[i] = newClientUsername;
                cw.sendPacketToClient(Packet.CLIENT_USERNAME, newClientUsername.getBytes());
                itemFound = true;
            }
        }
        if (!itemFound) {
            throw new ItemNotFoundException();
        }
    }

    public synchronized void removeClient(ClientWorker cw) throws ItemNotFoundException {
        boolean itemFound = false;
        for (int i = 0; i < maxSize; i++) {
            if (clientWorkerList[i] == cw) {
                removeClientByIndex(i);
                itemFound = true;
            }
        }
        if (!itemFound) {
            throw new ItemNotFoundException();
        }
        size--;
    }

    public synchronized void removeClient(String clientUUID) throws ItemNotFoundException {
        boolean itemFound = false;
        for (int i = 0; i < maxSize; i++) {
            if (clientUUIDList[i].equals(clientUUID) && clientUUIDList[i] != null) {
                removeClientByIndex(i);
                itemFound = true;
            }
        }
        if (!itemFound) {
            throw new ItemNotFoundException();
        }
        size--;
    }

    public String findClientUUID(ClientWorker cw) throws ItemNotFoundException {
        for (int i = 0; i < maxSize; i++) {
            if (clientWorkerList[i] == cw) {
                return clientUUIDList[i];
            }
        }
        throw new ItemNotFoundException();
    }

    public ClientWorker findClient(String clientUUID) throws ItemNotFoundException {
        for (int i = 0; i < maxSize; i++) {
            if (clientUUIDList[i].equals(clientUUID) && clientUUIDList[i] != null) {
                return clientWorkerList[i];
            }
        }
        throw new ItemNotFoundException();
    }

    private void removeClientByIndex(int index) {
        clientWorkerList[index] = null;
        clientUsernameList[index] = null;
        clientUUIDList[index] = null;
        clientSlots[index] = false;
    }

    private void refreshFreeSpace() {
        this.free = maxSize;
        for (int i = 0; i < maxSize; i++) {
            if (clientWorkerList[i] != null) {
                clientSlots[i] = true;
                this.free--;
            }
        }
    }

    private int getFirstOpenSpace() {
        for (int i = 0; i < maxSize; i++) {
            if (!clientSlots[i]) {
                return i;
            }
        }
        return 0;
    }

    public ClientWorker[] getAllClients() {
        if (this.clientWorkerList != null) {
            return this.clientWorkerList;
        } else {
            return null;
        }
    }
}
