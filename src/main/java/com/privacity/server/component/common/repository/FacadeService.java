package com.privacity.server.component.common.repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.privacity.server.component.common.ZipUtilService;
import com.privacity.server.component.grupo.GrupoUtilService;
import com.privacity.server.component.media.MediaUtilService;
import com.privacity.server.component.message.MessageUtilService;
import com.privacity.server.component.messagedetail.MessageDetailUtil;
import com.privacity.server.component.userforgrupo.UserForGrupoRepository;
import com.privacity.server.component.userforgrupo.UserForGrupoUtil;
import com.privacity.server.encrypt.PrivacityIdServices;
import com.privacity.server.security.SocketSessionRegistry;
import com.privacity.server.security.UserRepository;
import com.privacity.server.websocket.STOMPConnectEventListener;

import lombok.Getter;


@Getter
@Service
public class FacadeService {

	public FacadeService(@Lazy SimpMessagingTemplate simpMessagingTemplate,@Lazy  UserRepository userRepository,
			@Lazy PrivacityIdServices privacityIdServices, @Lazy SocketSessionRegistry socketSessionRegistry,
			@Lazy STOMPConnectEventListener stompConnectEventListener, @Lazy UserForGrupoRepository userForGrupoRepository,
			@Lazy UserForGrupoUtil userForGrupoUtil, @Lazy MessageUtilService messageUtilService,
			@Lazy MessageDetailUtil messageDetailUtilService, @Lazy GrupoUtilService grupoUtilService,
			@Lazy ZipUtilService zipUtilService, @Lazy MediaUtilService mediaUtilService) {
		super();
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.userRepository = userRepository;
		this.privacityIdServices = privacityIdServices;
		this.socketSessionRegistry = socketSessionRegistry;
		this.stompConnectEventListener = stompConnectEventListener;
		this.userForGrupoRepository = userForGrupoRepository;
		this.userForGrupoUtil = userForGrupoUtil;
		this.messageUtilService = messageUtilService;
		this.messageDetailUtilService = messageDetailUtilService;
		this.grupoUtilService = grupoUtilService;
		this.zipUtilService = zipUtilService;
		this.mediaUtilService = mediaUtilService;
	}
	private SimpMessagingTemplate simpMessagingTemplate;
	private UserRepository userRepository;
	private PrivacityIdServices privacityIdServices;

	public SocketSessionRegistry socketSessionRegistry;
    public STOMPConnectEventListener stompConnectEventListener;

	private UserForGrupoRepository userForGrupoRepository;
	private UserForGrupoUtil userForGrupoUtil;
	
	private MessageUtilService messageUtilService;
	private MessageDetailUtil messageDetailUtilService;
	private GrupoUtilService grupoUtilService;
	private ZipUtilService zipUtilService;
	private MediaUtilService mediaUtilService;
}
