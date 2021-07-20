package com.privacity.server.component.grupo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.privacity.common.dto.GrupoDTO;
import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.MessageDetailDTO;
import com.privacity.common.dto.UserDTO;
import com.privacity.common.dto.request.GrupoAddUserRequestDTO;
import com.privacity.common.dto.request.GrupoSaveRequestDTO;
import com.privacity.common.dto.response.InitGrupoResponse;
import com.privacity.server.component.message.MessageDetailRepository;
import com.privacity.server.component.message.MessageRepository;
import com.privacity.server.component.userforgrupo.UserForGrupoRepository;
import com.privacity.server.model.Grupo;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageDetail;
import com.privacity.server.model.UserForGrupo;
import com.privacity.server.model.UserForGrupoId;
import com.privacity.server.security.UserRepository;
import com.privacity.server.security.Usuario;

@RestController
@RequestMapping(path = "/secure/grupo") 
public class GrupoController  {

	GrupoRepository grupoRepository;

	UserForGrupoRepository userForGrupoRepository;

	UserRepository userRepository;
	
	
	MessageRepository messageRepository;


	MessageDetailRepository messageDetailRepository;

	
	public GrupoController(GrupoRepository grupoRepository, UserForGrupoRepository userForGrupoRepository,
			UserRepository userRepository, MessageRepository messageRepository,
			MessageDetailRepository messageDetailRepository) {
		super();
		this.grupoRepository = grupoRepository;
		this.userForGrupoRepository = userForGrupoRepository;
		this.userRepository = userRepository;
		this.messageRepository = messageRepository;
		this.messageDetailRepository = messageDetailRepository;
	}


	public GrupoDTO save(GrupoSaveRequestDTO request) throws Exception {
	    Authentication auth = SecurityContextHolder
	            .getContext()
	            .getAuthentication();
	    UserDetails userDetail = (UserDetails) auth.getPrincipal();
	    
		Usuario u = userRepository.findByUsername(userDetail.getUsername()).get();
		
		Grupo g = new Grupo();
		g.setIdGrupo(generate());
		g.setName(request.getName());
		
		grupoRepository.save(g);
		
		UserForGrupo ug = new UserForGrupo();
		ug.setUserForGrupoId( new UserForGrupoId(u, g));
		
		userForGrupoRepository.save(ug);
		
		GrupoDTO gd = new GrupoDTO();
		gd.setIdGrupo(g.getIdGrupo().toString());
		gd.setName(g.getName());

		gd.setUsersDTO(new UserDTO[1]);
		gd.getUsersDTO()[0] = new UserDTO();
		gd.getUsersDTO()[0].setUsername(u.getUsername());
		gd.getUsersDTO()[0].setUsernameToShow(u.getUsernameToShow());
		
		
       return gd;

	}  
	
	
	public UserDTO addUser(GrupoAddUserRequestDTO request) throws Exception {
	    
		Usuario u = userRepository.findByUsername(request.getUsername()).get();
		
		Grupo g = grupoRepository.findById(Long.parseLong(request.getIdGrupo())).get();
		UserForGrupo ug = new UserForGrupo();
		ug.setUserForGrupoId( new UserForGrupoId(u, g));
		
		userForGrupoRepository.save(ug);
		
		UserDTO r = new UserDTO();
		r.setUsername(u.getUsername());
		r.setUsernameToShow(u.getUsernameToShow());
		
       return r; 

	}  	

	public List<GrupoDTO> listarMisGrupos() throws Exception {
	    Authentication auth = SecurityContextHolder
	            .getContext()
	            .getAuthentication();
	    UserDetails userDetail = (UserDetails) auth.getPrincipal();
		Usuario u = userRepository.findByUsername(userDetail.getUsername()).get();
		
		UserForGrupo ug = new UserForGrupo();
		ug.setUserForGrupoId( new UserForGrupoId(u));
		
		List<UserForGrupo> l = userForGrupoRepository.findByUserForGrupoIdUser(u.getIdUser());
		List<GrupoDTO> r = new ArrayList<GrupoDTO>();
		for ( UserForGrupo e : l ) {
			GrupoDTO gd = new GrupoDTO();
			gd.setIdGrupo(e.getUserForGrupoId().getGrupo().getIdGrupo().toString());
			gd.setName(e.getUserForGrupoId().getGrupo().getName());
			
			r.add(gd);
			
			
			List<Usuario> usuarios = userForGrupoRepository.findByUsersForGrupo(Long.parseLong(gd.getIdGrupo()));
			
			gd.setUsersDTO(new UserDTO[usuarios.size()]); 
			int i = 0;	
			for (Usuario userList : usuarios ) {
				
				UserDTO userDTO = new UserDTO();
				//userDTO.setId(userList.getUserId().toString());
				userDTO.setUsername(userList.getUsername());
				userDTO.setUsernameToShow(userList.getUsernameToShow());
				gd.getUsersDTO()[i] = userDTO;
				i ++ ;
			}
		}
		
		return r;
		
		
	}
	public static long generate() {
		return Long.parseLong ((new Date().getTime()+"") + RandomStringUtils.randomNumeric(6)); 
	}

	public InitGrupoResponse initGrupo(String request) throws Exception {
		Usuario u = this.getUser();
		Grupo g = grupoRepository.findById(Long.parseLong(request)).get(); 

		List<Usuario> usuarios = userForGrupoRepository.findByUsersForGrupo(g.getIdGrupo());

		InitGrupoResponse response = new InitGrupoResponse();
		response.setUsersDTO(new UserDTO[usuarios.size()]); 
	
		for (int i = 0 ; i < usuarios.size() ; i++ ) {
			
			UserDTO userDTO = new UserDTO();
			userDTO.setUsername(usuarios.get(i).getUsername());
			userDTO.setUsernameToShow(usuarios.get(i).getUsernameToShow());
			
			response.getUsersDTO()[i] = userDTO;

		}
		
		
		List<Message> mensajes = messageRepository.findByMessageIdGrupoIdGrupo(g.getIdGrupo());

		MessageDTO[] mensajesDTO = new MessageDTO[mensajes.size()];
		
		
		for (int i = 0 ; i < mensajes.size() ; i++ ) {
			Message m = mensajes.get(i);
	    	
			MessageDTO mensaje = new MessageDTO();
	    	
			mensaje.setIdGrupo(m.getMessageId().getGrupo().getIdGrupo().toString());
			mensaje.setIdMessage(m.getMessageId().getIdMessage().toString());
			mensaje.setUsernameCreation(m.getUserCreation().getUsername());
	    	
	    	List<MessageDetail> detalles = messageDetailRepository.findByMessageUser(m.getMessageId().getIdMessage(),u.getIdUser());
	    	
			mensaje.setMessagesDetailDTO(new MessageDetailDTO[detalles.size()]);

			int j=0;
	    	for (MessageDetail d : detalles) {
	    		MessageDetailDTO dto = new MessageDetailDTO();
	    		dto.setIdMessageDetail(d.getMessageDetailId().getIdMessageDetail().toString());
	    		dto.setText(d.getText());
	    		dto.setEstado(d.getState().name());
	    		dto.setUserDestino(d.getMessageDetailId().getUserDestino().getUsername());
	    		dto.setIdGrupo(m.getMessageId().getGrupo().getIdGrupo().toString());
	    		dto.setIdMessage(m.getMessageId().getIdMessage().toString());
	    		
	    		mensaje.getMessagesDetailDTO()[j] = dto;
				j++;
	  
	    	}
	    	
	    	mensajesDTO[i] = mensaje;
	    	
		}
		response.setMessagesDTO(mensajesDTO);
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
    	
}
