package com.privacity.server.component.message;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.privacity.common.config.Constant;
import com.privacity.common.dto.MediaDTO;
import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.MessageDetailDTO;
import com.privacity.common.dto.ProtocoloDTO;
import com.privacity.common.enumeration.MessageState;
import com.privacity.server.component.common.ZipUtilService;
import com.privacity.server.component.grupo.GrupoRepository;
import com.privacity.server.component.media.MediaUtilService;
import com.privacity.server.component.messagedetail.MessageDetailRepository;
import com.privacity.server.component.userforgrupo.UserForGrupoRepository;
import com.privacity.server.exceptions.PrivacityException;
import com.privacity.server.exceptions.ProcessException;
import com.privacity.server.model.Media;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageDetail;
import com.privacity.server.security.UserRepository;
import com.privacity.server.security.Usuario;
import com.privacity.server.util.Mapper;
import com.privacity.server.websocket.WebSocketSender;


@Controller    // This means that this class is a Controller
@RequestMapping(path = "/secure/message") // This means URL's start with /demo (after Application path)
public class MessageService {

	@Autowired
	UserForGrupoRepository userForGrupoRepository;
	
	@Autowired
	ZipUtilService zipUtilService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MessageRepository messageRepository;

	private MessageUtilService messageUtilService;

	
	@Autowired
	MessageDetailRepository messageDetailRepository;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	private MediaUtilService mediaUtilService;
	
	private GrupoRepository grupoRepository;
	
	private Mapper mapper;
	
	private WebSocketSender webSocketSender;
	

	
	public MessageService(GrupoRepository grupoRepository,WebSocketSender webSocketSender,
			MessageUtilService messageUtilService,
			MediaUtilService mediaUtilService,
			Mapper mapper) {
		super();
		this.grupoRepository = grupoRepository;
		this.webSocketSender = webSocketSender;
		this.messageUtilService = messageUtilService;
		this.mediaUtilService = mediaUtilService;
		this.mapper = mapper;
	}
	
	public MessageDTO get(Message m, Usuario u) throws Exception {

		if (m.isAnonimo()) {
			return getAnonimo(m,u);
		}else {
			return getNormal(m);
		}
	}
	
	private MessageDTO getAnonimo(Message m, Usuario u) throws Exception {
		
	
		
		MessageDTO response = new MessageDTO();
		response.setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
		response.setText(m.getText());
		

		response.setUsuarioCreacion(mapper.doit(m.getUserCreation()));
		
		response.setIdMessage(m.getMessageId().getIdMessage()+"");
		response.setMessagesDetailDTO(new MessageDetailDTO[1]);
		
		Set<MessageDetail> details = m.getMessagesDetail();
		
		response.setBlackMessage(m.isBlackMessage());
		response.setAnonimo(m.isAnonimo());
		response.setTimeMessage(m.isTimeMessage());
		response.setSystemMessage(m.isSystemMessage());
		
		Media media = m.getMedia();
		MediaDTO mediaDTO = mapper.doit(media,true);
		response.setMediaDTO(mediaDTO);
		
		if (u.getUsername().equals(m.getUserCreation())) {
			
			response.setMessagesDetailDTO(new MessageDetailDTO[details.size()]);
			int i=0;
			for ( MessageDetail d : details) {
				
				response.getMessagesDetailDTO()[i] = new MessageDetailDTO();

				
				response.getMessagesDetailDTO()[i].setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
				response.getMessagesDetailDTO()[i].setIdMessage(m.getMessageId().getIdMessage()+"");

				response.getMessagesDetailDTO()[i].setUsuarioDestino(mapper.doit(d.getMessageDetailId().getUserDestino()));
				
				response.getMessagesDetailDTO()[i].setEstado(d.getState().toString());

				i++;
			}

			return response;
		}else {
			response.setUsuarioCreacion(null);
			response.setMessagesDetailDTO(new MessageDetailDTO[1]);
			//int i=0;
			for ( MessageDetail d : details) {
				
				if (d.getMessageDetailId().getUserDestino().getIdUser().equals(u.getIdUser())){
					response.getMessagesDetailDTO()[0] = new MessageDetailDTO();
					response.getMessagesDetailDTO()[0].setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
					response.getMessagesDetailDTO()[0].setIdMessage(m.getMessageId().getIdMessage()+"");
					response.getMessagesDetailDTO()[0].setUsuarioDestino(mapper.doit(d.getMessageDetailId().getUserDestino()));
					response.getMessagesDetailDTO()[0].setEstado(d.getState().toString());
				
				}
				//i++;
			}

			return response;
			
		}

	}
	
	private MessageDTO getNormal(Message m) throws Exception {
	

		
		MessageDTO response = new MessageDTO();
		response.setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
		response.setText(m.getText());
		response.setSecretKeyPersonal(m.isSecretKeyPersonal());
		response.setIdMessage(m.getMessageId().getIdMessage()+"");
		response.setMessagesDetailDTO(new MessageDetailDTO[1]);
		response.setBlackMessage(m.isBlackMessage());
		response.setAnonimo(m.isAnonimo());
		response.setTimeMessage(m.isTimeMessage());
		response.setSystemMessage(m.isSystemMessage());

		response.setUsuarioCreacion(mapper.doitForGrupo(m.getMessageId().getGrupo(), m.getUserCreation()));
		Set<MessageDetail> details = m.getMessagesDetail();
		
		response.setMessagesDetailDTO(new MessageDetailDTO[details.size()]);
		
		Media media = m.getMedia();
		MediaDTO mediaDTO = mapper.doit(media,true);
		response.setMediaDTO(mediaDTO);
		
		int i=0;
		for ( MessageDetail d : details) {
			
			response.getMessagesDetailDTO()[i] = new MessageDetailDTO();
//			if ( u.getUsername().equals(d.getMessageDetailId().getUserDestino())) {
//				response.getMessagesDetailDTO()[i].setText(d.getText());	
//			}
			
			response.getMessagesDetailDTO()[i].setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
			response.getMessagesDetailDTO()[i].setIdMessage(m.getMessageId().getIdMessage()+"");
			//response.getMessagesDetailDTO()[i].setIdMessageDetail(d.getMessageDetailId().getIdMessageDetail()+"");
			
			response.getMessagesDetailDTO()[i].setUsuarioDestino(mapper.doit(d.getMessageDetailId().getUserDestino()));

			response.getMessagesDetailDTO()[i].setEstado(d.getState().toString());

			i++;
		}

		return response;
	}

	public MessageDTO[] getAllidMessageUnreadMessages() throws Exception {
		Usuario u = getUser();
		List<MessageDetail> l = messageDetailRepository.getAllidMessageUnreadMessages(u.getIdUser());
		
		MessageDTO[] r = new MessageDTO[l.size()];
		for (int i= 0 ; i < l.size() ; i++) {
			MessageDTO dto = new MessageDTO();
			dto.setIdMessage(l.get(i).getMessageDetailId().getMessage().getMessageId().getIdMessage().toString());
			dto.setIdGrupo(l.get(i).getMessageDetailId().getMessage().getMessageId().getGrupo().getIdGrupo().toString());
			
			r[i] = dto;
		}
		
		return r;
	}
	
	public void emptyList(String idGrupo) throws Exception {
		Usuario u = getUser();
		
		messageDetailRepository.deleteAllMessageDetailByGrupoAndUser(Long.parseLong(idGrupo), u.getIdUser());
		
	}
	
	public void deleteAllMyMessageForEverybodyByGrupo(String idGrupo) throws Exception {
		Usuario u = getUser();
		
		messageDetailRepository.deleteAllMyMessageDetailForEverybodyByGrupo(Long.parseLong(idGrupo), u.getIdUser());
		messageRepository.deleteAllMyMessageForEverybodyByGrupo(Long.parseLong(idGrupo), u.getIdUser());
	}

	public MessageDetailDTO changeState(Usuario u, Message m, MessageDetail md, MessageState state) throws Exception {

	
		if (m.isTimeMessage() && state.equals(MessageState.DESTINY_READED)) {
			md.setLogicDeleted(true);
		}
		md.setState(state);
		
		messageDetailRepository.save(md);
		
		MessageDetailDTO r = mapper.doit(md);



		
		ProtocoloDTO p = webSocketSender.buildProtocoloDTO(
				Constant.PROTOCOLO_COMPONENT_MESSAGE,
                Constant.PROTOCOLO_ACTION_MESSAGE_CHANGE_STATE,
                r);
		

		webSocketSender.senderToGrupo(u, m.getMessageId().getGrupo(), p);	

		return r;
		
	}
	public MessageDTO[] loadMessages(MessageDTO request) throws Exception {
		return null;
	}
	
	public MessageDTO send(Message request) throws Exception {
		
		if (request.isAnonimo()) {
			return sendAnonimo(request);
		}else {
			return sendNormal(request);
		}
		
	}
	private MessageDTO sendAnonimo(Message m) throws Exception {

		Set<MessageDetail> details = m.getMessagesDetail();

		m.setMessagesDetail(null);
		Media media = m.getMedia();
		m.setMedia(null);
		messageRepository.save(m);
		
		//messageRepository.insertMedia(media.getData(), media.getMediaType().ordinal(), media.getMessage().getMessageId().getGrupo().getIdGrupo(), media.getMessage().getMessageId().getIdMessage());
		messageDetailRepository.saveAll(details);
		
		//messageRepository.save(m);
		
		new Runnable() {

			@Override
			public void run() {
				m.setMedia(media);
				messageRepository.save(m);
				
			}}.run();
		

		m.setMessagesDetail(details);

		for (MessageDetail md : details) {
			
			new Runnable() {

				@Override
				public void run() {
					try {
						MessageDTO response = mapper.doitAnonimoToSend(m, md.getMessageDetailId().getUserDestino().getIdUser());
						
						
						ProtocoloDTO p = webSocketSender.buildProtocoloDTO(
								Constant.PROTOCOLO_COMPONENT_MESSAGE,
								Constant.PROTOCOLO_ACTION_MESSAGE_RECIVIED);
						
						p.setMessageDTO(response);
				
				webSocketSender.sender(response, p,m.getUserCreation().getIdUser());
				
			} catch (ProcessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PrivacityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			
				}}.run();
		}
		return mapper.doitWithOutTextAndMedia(m);

    }
	
	public void deleteForMe(MessageDetail detail) throws ProcessException {
	
		detail.setLogicDeleted(true);
		messageDetailRepository.save(detail);
	
	}
	

	public MessageDTO sendNormal(Message m) throws PrivacityException {
		Set<MessageDetail> details = m.getMessagesDetail();

		m.setMessagesDetail(null);
		Media media = m.getMedia();
		m.setMedia(null);
		messageRepository.save(m);
		
		//messageRepository.insertMedia(media.getData(), media.getMediaType().ordinal(), media.getMessage().getMessageId().getGrupo().getIdGrupo(), media.getMessage().getMessageId().getIdMessage());
		messageDetailRepository.saveAll(details);
		
		//messageRepository.save(m);
		
		new Runnable() {

			@Override
			public void run() {
				m.setMedia(media);
				messageRepository.save(m);
				
			}}.run();
		

		m.setMessagesDetail(details);
		
		new Runnable() {

			@Override
			public void run() {
				try {
					MessageDTO response = mapper.doit(m);

					
					ProtocoloDTO p = webSocketSender.buildProtocoloDTO(
							Constant.PROTOCOLO_COMPONENT_MESSAGE,
							Constant.PROTOCOLO_ACTION_MESSAGE_RECIVIED);
					
					p.setMessageDTO(response);
					String protocolo = new Gson().toJson(p);
					
					webSocketSender.sender(m, protocolo);
				} catch (PrivacityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}}.run();
		
		return mapper.doitWithOutTextAndMedia(m);

    }	
	
	public MessageDTO deleteForEveryone(Message message) throws PrivacityException {
		
		{
			MessageDTO mRemove = new MessageDTO();
			
			mRemove.setIdGrupo(message.getMessageId().getGrupo().getIdGrupo()+"");
			mRemove.setIdMessage(message.getMessageId().getIdMessage()+"");
			
			ProtocoloDTO p = webSocketSender.buildProtocoloDTO(
					Constant.PROTOCOLO_COMPONENT_MESSAGE,
					"/message/deleteForEveryone");
			
			p.setMessageDTO(mRemove);
			
			for (MessageDetail md : message.getMessagesDetail()){
				
				if (!md.getMessageDetailId().getUserDestino().getUsername().equals(message.getUserCreation().getUsername())) {
					webSocketSender.sender(md.getMessageDetailId().getUserDestino().getUsername() ,p );	
				}
				
			}
		}
		messageDetailRepository.deleteByMessageDetailIdMessage(message.getMessageId().getGrupo().getIdGrupo(), message.getMessageId().getIdMessage());
		//mediaRepository.deleteAllMyMediaByGrupoMessage(message.getMessageId().getGrupo().getIdGrupo(), message.getMessageId().getIdMessage());
		messageRepository.delete(message);

		MessageDTO mRemoveReturn = new MessageDTO();
		
		mRemoveReturn.setIdGrupo(message.getMessageId().getGrupo().getIdGrupo()+"");
		mRemoveReturn.setIdMessage(message.getMessageId().getIdMessage()+"");
		
		return mRemoveReturn;

	}    	


	private Usuario getUser() {
		Authentication auth = SecurityContextHolder
	            .getContext()
	            .getAuthentication();
	    UserDetails userDetail = (UserDetails) auth.getPrincipal();
	    
		Usuario u = userRepository.findByUsername(userDetail.getUsername()).get();
		return u;
	}    
}
