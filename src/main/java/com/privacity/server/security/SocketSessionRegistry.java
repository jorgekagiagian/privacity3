package com.privacity.server.security;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by baiguantao on 2017/8/4.
 * User session record class
 */

public class SocketSessionRegistry{
    //this map save every session
    //This collection stores session
    private final ConcurrentMap<String, Set<String>> userSessionIds = new ConcurrentHashMap();
    private final Object lock = new Object();

    public SocketSessionRegistry() {
    }

    /**
     *
     * Get session Id
     * @param user
     * @return
     */
    public Set<String> getSessionIds(String user) {
        Set set = (Set)this.userSessionIds.get(user);
        return set != null?set: Collections.emptySet();
    }

    /**
     * Get all session s
     * @return
     */
    public ConcurrentMap<String, Set<String>> getAllSessionIds() {
        return this.userSessionIds;
    }

    /**
     * register session
     * @param user
     * @param sessionId
     */
    public void registerSessionId(String user, String sessionId) {
        Assert.notNull(user, "User must not be null");
        Assert.notNull(sessionId, "Session ID must not be null");
        Object var3 = this.lock;
        synchronized(this.lock) {
            Object set = (Set)this.userSessionIds.get(user);
            if(set == null) {
                set = new CopyOnWriteArraySet();
                this.userSessionIds.put(user, (Set<String>) set);
            }

            ((Set)set).add(sessionId);
        }
    }

    public void unregisterSessionId(String userName, String sessionId) {
        Assert.notNull(userName, "User Name must not be null");
        Assert.notNull(sessionId, "Session ID must not be null");
        Object var3 = this.lock;
        synchronized(this.lock) {
            Set set = (Set)this.userSessionIds.get(userName);
            if(set != null && set.remove(sessionId) && set.isEmpty()) {
                this.userSessionIds.remove(userName);
            }

        }
    }
}