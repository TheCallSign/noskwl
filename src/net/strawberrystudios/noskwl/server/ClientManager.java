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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.strawberrystudios.noskwl.packet.Packet;

/**
 *
 * @author St John Giddy @ Strawberry Studios (2015)
 */
public class ClientManager {

    /**
     * <UUID, ClientWorker>
     */
    private final Map<String, ClientWorker> workerMap;
    /**
     * <UUID, Username>
     */
    private final Map<String, String> usernameMap;

    private int size;
    private final int maxSize;
    private int free;

    public ClientManager(int maxSize) {
        this.maxSize = maxSize;
        workerMap = new HashMap<>();
        usernameMap = new HashMap<>();
    }

    public ClientManager() {
        this.maxSize = 0;
        workerMap = new HashMap<>();
        usernameMap = new HashMap<>();
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

    public synchronized void addClient(ClientWorker cw, String clientUsername, String clientUUID) throws ClientDuplicateException, ServerFullException {
        if (!(size < maxSize && maxSize != 0)) {
            throw new ServerFullException();
        }
            if (workerMap.containsKey(clientUUID)) {
                throw new ClientDuplicateException();
            }
        
        workerMap.put(clientUUID, cw);
        usernameMap.put(clientUUID, clientUsername);
        size++;

    }

    public void modifyClientUsername(String uid, String newClientUsername) throws ClientDuplicateException, ItemNotFoundException {
        if (usernameMap.containsValue(newClientUsername)) {
            throw new ClientDuplicateException();
        }
        if (!usernameMap.containsKey(uid)) {
            throw new ItemNotFoundException();
        }
        updateMap(uid, newClientUsername);
        getWorker(uid).sendPacketToClient(Packet.CLIENT_USERNAME, newClientUsername.getBytes());
    }

    public synchronized void removeClient(String uuid) throws ItemNotFoundException {
        if (!usernameMap.containsKey(uuid)) {
            throw new ItemNotFoundException();
        }
        removeFromMaps(uuid);
        size--;
    }

    public Collection<ClientWorker> getAllWorkers() {
        return workerMap.values();
    }

    private void updateMap(String uuid, String username) {
        usernameMap.put(uuid, username);
    }

    private void updateMap(String uuid, ClientWorker cw) {
        workerMap.put(uuid, cw);
    }

    private void removeFromMaps(String uuid) {
        usernameMap.remove(uuid);
        workerMap.remove(uuid);
    }

    public ClientWorker getWorker(String uuid) throws ItemNotFoundException {
        if (!workerMap.containsKey(uuid)) {
            throw new ItemNotFoundException();
        }
        return workerMap.get(uuid);
    }

    public String getUsername(String uuid) {
        return usernameMap.get(uuid);
    }
}
