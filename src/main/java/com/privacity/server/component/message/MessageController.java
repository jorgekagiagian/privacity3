package com.privacity.server.component.message;

import java.util.Date;
import java.util.HashSet;
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
import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.MessageDetailDTO;
import com.privacity.common.dto.ProtocoloDTO;
import com.privacity.common.dto.request.MessageSendRequestDTO;
import com.privacity.common.enumeration.MessageState;
import com.privacity.server.component.grupo.GrupoController;
import com.privacity.server.component.grupo.GrupoRepository;
import com.privacity.server.component.userforgrupo.UserForGrupoRepository;
import com.privacity.server.model.Grupo;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageDetail;
import com.privacity.server.model.MessageDetailId;
import com.privacity.server.model.MessageId;
import com.privacity.server.security.UserRepository;
import com.privacity.server.security.Usuario;

@Controller    // This means that this class is a Controller
@RequestMapping(path = "/secure/message") // This means URL's start with /demo (after Application path)
public class MessageController {

	@Autowired
	UserForGrupoRepository userForGrupoRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MessageRepository messageRepository;

	@Autowired
	MessageDetailRepository messageDetailRepository;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	
	private GrupoRepository grupoRepository;
	
	public MessageController(GrupoRepository grupoRepository) {
		super();
		this.grupoRepository = grupoRepository;
	}
	
	public MessageDTO get(MessageDTO request) throws Exception {
		Usuario u = getUser();
		Message m = messageRepository.findByMessageIdIdMessage(
				Long.parseLong(request.getIdMessage())
				);
		
		
		MessageDTO response = new MessageDTO();
		response.setIdGrupo(request.getIdGrupo());
		response.setUsernameCreation(m.getUserCreation().getUsername());
		response.setIdMessage(m.getMessageId().getIdMessage()+"");
		response.setMessagesDetailDTO(new MessageDetailDTO[1]);
		Set<MessageDetail> details = m.getMessagesDetail();
		
		response.setMessagesDetailDTO(new MessageDetailDTO[details.size()]);
		int i=0;
		for ( MessageDetail d : details) {
			
			response.getMessagesDetailDTO()[i] = new MessageDetailDTO();
			if ( u.getUsername().equals(d.getMessageDetailId().getUserDestino())) {
				response.getMessagesDetailDTO()[i].setText(d.getText());	
			}
			
			response.getMessagesDetailDTO()[i].setIdGrupo(request.getIdGrupo());
			response.getMessagesDetailDTO()[i].setIdMessage(m.getMessageId().getIdMessage()+"");
			response.getMessagesDetailDTO()[i].setIdMessageDetail(d.getMessageDetailId().getIdMessageDetail()+"");
			response.getMessagesDetailDTO()[i].setUserDestino(d.getMessageDetailId().getUserDestino().getUsername());
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

	public void changeState(MessageDetailDTO request) throws Exception {
		Usuario u = getUser();
		MessageState state = MessageState.valueOf(request.getEstado());
	
		messageDetailRepository.updateState (u.getIdUser() , 
				Long.parseLong(request.getIdGrupo()), 
				Long.parseLong(request.getIdMessage()),
				Long.parseLong(request.getIdMessageDetail()),
				state);
		

		List<Usuario> usuariosDestino = messageDetailRepository.findByUpdateState(u.getIdUser() , 
				Long.parseLong(request.getIdGrupo()), 
				Long.parseLong(request.getIdMessage()));
		
		MessageDetailDTO r = new MessageDetailDTO();
		r.setEstado(state.name());
		r.setIdGrupo(request.getIdGrupo());
		r.setIdMessage(request.getIdMessage());
		r.setIdMessageDetail(request.getIdMessageDetail());
		
		ProtocoloDTO p = new ProtocoloDTO();
		p.setComponent(Constant.PROTOCOLO_COMPONENT_MESSAGE);
		p.setAction(Constant.PROTOCOLO_ACTION_MESSAGE_CHANGE_STATE);
		p.setObjectDTO(new Gson().toJson(r));
		
		for (Usuario destino : usuariosDestino) {
			if (!u.getUsername().equals(destino.getUsername())){
				simpMessagingTemplate.convertAndSendToUser(destino.getUsername(), "/topic/reply" , new Gson().toJson(p));	
			}
			
		}
		
	}
	public MessageDTO[] loadMessages(MessageDTO request) throws Exception {
		return null;
	}
	
	public MessageDTO send(MessageSendRequestDTO request) throws Exception {

	    Usuario u = getUser();
		
		
		Message m = new Message();
		m.setDateCreation(new Date());
		//m.setText(request.getText());
		m.setUserCreation(u);
		
		Grupo g = grupoRepository.findById(Long.parseLong(request.getIdGrupo())).get(); 
		
		MessageId idm = new MessageId();
		idm.setGrupo(g);
		idm.setIdMessage(GrupoController.generate());
		m.setMessageId(idm);
		m.setUserCreation(u);
		
		messageRepository.save(m);
		
		m.setMessagesDetail(new HashSet<MessageDetail>());
		
		for ( int i = 0 ; i < request.getMessageDetails().length ; i++ ) {
			
			MessageDetailDTO destino = request.getMessageDetails()[i];
			
			Usuario userDestino = userRepository.findByUsername(destino.getUserDestino()).get();
			
			
			MessageDetail md = new MessageDetail();
			if ( userDestino.getUsername().equals(u.getUsername())){
				md.setState(MessageState.MY_MESSAGE_SENT);
			}else {
				md.setState(MessageState.DESTINY_SERVER);
			}
		
			
			md.setText(destino.getText());
			Long id = GrupoController.generate();
			md.setMessageDetailId(new MessageDetailId(userDestino, m,id) );
			
			messageDetailRepository.save(md);
			m.getMessagesDetail().add(md);
		}
		
		MessageDTO response = new MessageDTO();
		response.setIdGrupo(request.getIdGrupo());
		response.setUsernameCreation(m.getUserCreation().getUsername());
		response.setIdMessage(m.getMessageId().getIdMessage()+"");
		response.setMessagesDetailDTO(new MessageDetailDTO[1]);
		Set<MessageDetail> details = m.getMessagesDetail();
		
		response.setMessagesDetailDTO(new MessageDetailDTO[details.size()]);
		int i=0;
		for ( MessageDetail d : details) {
			
			response.getMessagesDetailDTO()[i] = new MessageDetailDTO();
			//if ( u.getUsername().equals(d.getMessageDetailId().getUserDestino())) {
				response.getMessagesDetailDTO()[i].setText(d.getText());	
			//}
			
			response.getMessagesDetailDTO()[i].setIdGrupo(request.getIdGrupo());
			response.getMessagesDetailDTO()[i].setIdMessage(m.getMessageId().getIdMessage()+"");
			response.getMessagesDetailDTO()[i].setIdMessageDetail(d.getMessageDetailId().getIdMessageDetail()+"");
			response.getMessagesDetailDTO()[i].setUserDestino(d.getMessageDetailId().getUserDestino().getUsername());
			response.getMessagesDetailDTO()[i].setEstado(d.getState().toString());

			i++;
		}

		ProtocoloDTO p = new ProtocoloDTO();
		p.setComponent(Constant.PROTOCOLO_COMPONENT_MESSAGE);
		p.setAction(Constant.PROTOCOLO_ACTION_MESSAGE_RECIVIED);
		p.setObjectDTO(new Gson().toJson(response));

		for ( int k = 0 ; k < request.getMessageDetails().length ; k++ ) {
			if (!request.getMessageDetails()[k].getUserDestino().equals(u.getUsername())){
				simpMessagingTemplate.convertAndSendToUser(request.getMessageDetails()[k].getUserDestino(), "/topic/reply" , new Gson().toJson(p));	
			}
		}
		
		return response;

    }	
	public Usuario getUser() {
		Authentication auth = SecurityContextHolder
	            .getContext()
	            .getAuthentication();
	    UserDetails userDetail = (UserDetails) auth.getPrincipal();
	    
		Usuario u = userRepository.findByUsername(userDetail.getUsername()).get();
		return u;
	}    
    
//	@PostMapping(path = "/listar/todosMisMensajes") // Map ONLY GET Requests
//    public ResponseEntity<List<MessageDTOResponse>> listAllMensajes() throws Exception {
//
//		Usuario u = getUser();
//		
//    List<MessageDTOResponse> r = new ArrayList<MessageDTOResponse>();
//    			
//    Iterable<Message> l = messageRepository.findAll();
//    Iterator<Message> i = l.iterator();
//    while (i.hasNext()) {
//    	Message m = i.next();
//    	MessageDTOResponse e = new MessageDTOResponse();
//    	e.setIdGrupo(m.getMessageId().getGrupo().getIdGrupo().toString());
//    	e.setIdMessage(m.getMessageId().getIdMessage().toString());
//    	//e.setText(m.getText());
//    	e.setUsername(m.getUserCreation().getUsername());
//    	
//    	List<MessageDetail> detalles = messageDetailRepository.findByMessageUser(m.getMessageId().getIdMessage(),u.getIdUser());
//    	
//		e.setMessagesDetailDTO(new MessageDetailDTO[detalles.size()]);
//		int j=0;
//    	for (MessageDetail d : detalles) {
//    		MessageDetailDTO dto = new MessageDetailDTO();
//    		dto.setIdMessageDetail(d.getMessageDetailId().getIdMessageDetail().toString());
//    		dto.setText(d.getText());
//    		dto.setEstado(d.getEstado());
//    		dto.setUserDestino(d.getUserDestino().getUsername());
//    		dto.setIdGrupo(m.getMessageId().getGrupo().getIdGrupo().toString());
//    		dto.setIdMessage(m.getMessageId().getIdMessage().toString());
//    		
//    		e.getMessagesDetailDTO()[j] = dto;
//			j++;
//    	}
//    	
//    	r.add(e);
//    }
//    	
//   	 return ResponseEntity.ok().headers(new HttpHeaders()).body(r);
//   }   

}
