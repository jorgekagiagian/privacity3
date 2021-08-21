package com.privacity.server.websocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.privacity.server.security.SocketSessionRegistry;

/**
 * Created by baiguantao on 2017/8/4.
 * STOMP Monitoring category
 * Used for session registration and key value acquisition
 */
public class STOMPConnectEventListener  implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        //login get from browser
        String agentId = event.getUser().getName();
        String sessionId = sha.getSessionId();
        webAgentSessionRegistry.registerSessionId(agentId,sessionId);
    }
} 