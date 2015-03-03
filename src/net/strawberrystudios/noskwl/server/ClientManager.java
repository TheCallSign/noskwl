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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import net.strawberrystudios.noskwl.packet.Packet;
import sun.misc.ConditionLock;

/**
 *
 * @author St John Giddy @ Strawberry Studios (2015)
 */
public class ClientManager {

    public static final Object lock = new Object();

    /**
     * <UUID, ClientWorker>
     */
    private final Map<String, ClientWorker> workerMap;
    /**
     * <UUID, Username>
     */
    private final Map<String, String> usernameMap;
    /**
     * <Username, UUID>
     */
    private final Map<String, String> cachedUsernameMap;

    private int size;
    private final int maxSize;

    public ClientManager(int maxSize) {
        this.maxSize = maxSize;
        workerMap = new HashMap<>();
        usernameMap = new HashMap<>();
        cachedUsernameMap = new HashMap<>();
    }

    public ClientManager() {
        this.maxSize = 0;
        workerMap = new HashMap<>();
        usernameMap = new HashMap<>();
        cachedUsernameMap = new HashMap<>();
    }

    public int getMaxSize() {
        return maxSize;
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
        if (clientUsername.isEmpty()) {
            clientUsername = clientUUID + "U";
        }
        usernameMap.put(clientUUID, clientUsername);
        cachedUsernameMap.put(clientUsername, clientUUID);
        size++;
//        synchronized(lock){
//            lock.notifyAll();
//        }

    }

    public void modifyClientUsername(String uid, String newClientUsername) throws ClientDuplicateException, ItemNotFoundException {
        if (usernameMap.containsValue(newClientUsername)) {
            throw new ClientDuplicateException();
        }

        updateMap(uid, newClientUsername);

        getWorker(uid).sendPacketToClient(Packet.CLIENT_USERNAME, newClientUsername.getBytes());
        getWorker(uid).setClientUsername(newClientUsername);
    }

    public synchronized void removeClient(String uuid) throws ItemNotFoundException {
        if (!usernameMap.containsKey(uuid)) {
            throw new ItemNotFoundException();
        }
        removeFromMaps(uuid);
        size--;
    }

    public synchronized Collection<ClientWorker> getAllWorkers() {
        return workerMap.values();
    }

    public String getUUIDFromUsername(String username) {
        return cachedUsernameMap.get(username);
    }

    private void updateMap(String uuid, String username) {
        usernameMap.put(uuid, username);
        cachedUsernameMap.put(username, uuid);
    }

    private void updateMap(String uuid, ClientWorker cw) {
        workerMap.put(uuid, cw);
    }

    private  void removeFromMaps(String uuid) {
        cachedUsernameMap.remove(usernameMap.get(uuid));
        usernameMap.remove(uuid);
        workerMap.remove(uuid);
    }

    public synchronized ClientWorker getWorker(String uuid) throws ItemNotFoundException {
        if (!workerMap.containsKey(uuid)) {
            throw new ItemNotFoundException();
        }
        return workerMap.get(uuid);
    }

    public synchronized String getUsername(String uuid) {
        return usernameMap.get(uuid);
    }

    public synchronized String getCSVUserlist() {
        StringBuilder sb = new StringBuilder();
        for (String username : this.cachedUsernameMap.values()) {
            sb.append(username).append(",");
        }
        return sb.toString();
    }
}
