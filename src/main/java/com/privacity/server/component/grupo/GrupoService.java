package com.privacity.server.component.grupo;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.privacity.common.config.Constant;
import com.privacity.common.dto.GrupoDTO;
import com.privacity.common.dto.GrupoInvitationDTO;
import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.MessageDetailDTO;
import com.privacity.common.dto.ProtocoloDTO;
import com.privacity.common.dto.UserForGrupoDTO;
import com.privacity.common.dto.UsuarioDTO;
import com.privacity.common.dto.response.GrupoRemoveMeResponseDTO;
import com.privacity.common.dto.response.InitGrupoResponse;
import com.privacity.common.enumeration.GrupoRolesEnum;
import com.privacity.server.component.grupoinvitation.GrupoInvitationRepository;
import com.privacity.server.component.grupoinvitation.GrupoInvitationUtil;
import com.privacity.server.component.message.MessageRepository;
import com.privacity.server.component.message.MessageService;
import com.privacity.server.component.messagedetail.MessageDetailRepository;
import com.privacity.server.component.userforgrupo.UserForGrupoRepository;
import com.privacity.server.component.usuario.UsuarioService;
import com.privacity.server.exceptions.PrivacityException;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.AES;
import com.privacity.server.model.Grupo;
import com.privacity.server.model.GrupoInvitation;
import com.privacity.server.model.GrupoInvitationId;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageDetail;
import com.privacity.server.model.UserForGrupo;
import com.privacity.server.model.UserForGrupoId;
import com.privacity.server.security.UserRepository;
import com.privacity.server.security.Usuario;
import com.privacity.server.util.Mapper;
import com.privacity.server.websocket.WebSocketSender;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GrupoService  {
	private GrupoUtilService grupoUtilService;
	private UsuarioService	usuarioService;
	private GrupoRepository grupoRepository;
	private UserForGrupoRepository userForGrupoRepository;
	private UserRepository userRepository;
	private MessageRepository messageRepository;

	private MessageDetailRepository messageDetailRepository;
	private MessageService messageController;
	private WebSocketSender webSocketSender;
	private Mapper mapper;
	private GrupoInvitationRepository grupoInvitationRepository;
	private GrupoInvitationUtil grupoInvitationUtil;
	



	public GrupoDTO newGrupo(Usuario u, Grupo g, AES aesdto) throws Exception {
		
		//aesGrupoRepository.save(g.getAesGrupo());
		
		//g.setEncryptPublicKey(encrypt.getPublicKey());
		grupoRepository.save(g);
		
		
		UserForGrupo ug = new UserForGrupo();
		ug.setUserForGrupoId( new UserForGrupoId(u, g));
		ug.setRole(GrupoRolesEnum.ADMIN);
//		aesdto.setUserForGrupoId(null);
		ug.setAes(aesdto);
		userForGrupoRepository.save(ug);
		
		
		return getGrupo(g,u);
	
	}  
	

	private GrupoDTO getGrupo(String idGrupo, Usuario logU) throws ValidationException {
		return getGrupo(Long.parseLong(idGrupo),logU);
	}
	private GrupoDTO getGrupo(Long idGrupo, Usuario logU) throws ValidationException {
		Grupo g = grupoUtilService.getGrupoById(idGrupo);
		return getGrupo(g,logU);
	}
	private GrupoDTO getGrupo(Grupo grupo, Usuario logU) throws ValidationException {

		List<UserForGrupo> ufgList = userForGrupoRepository.findByForGrupo(grupo.getIdGrupo());

		UserForGrupoDTO[] arr = new UserForGrupoDTO[ufgList.size()];
		int i = 0;
		for (UserForGrupo ufgElement : ufgList ) {
			ufgElement.getUserForGrupoId().getUser();
			
			UserForGrupoDTO ufgElementDTO = mapper.doit(ufgElement,logU);
			arr[i] = ufgElementDTO;
			i++;
		}
	
		GrupoDTO r = mapper.doit(grupo);
		r.setUsersForGrupoDTO(arr);
		return r;
	}
	
	public void  sentInvitation(Grupo g, GrupoRolesEnum role, Usuario logU,Usuario usuarioInvitationCode, AES aes) throws PrivacityException {
		GrupoInvitation gi = new GrupoInvitation();
		gi.setAes(aes);
		gi.setRole(GrupoRolesEnum.ADMIN);
		gi.setGrupoInvitationId(new GrupoInvitationId(usuarioInvitationCode, logU, g));
		gi.setPrivateKey(usuarioInvitationCode.getEncryptKeys().getPrivateKey());
		grupoInvitationRepository.save(gi);
		
		GrupoDTO ginfo = getGrupo(gi.getGrupoInvitationId().getGrupo(), gi.getGrupoInvitationId().getUsuarioInvitado());
		ginfo.setGrupoInvitation(true);
		ginfo.setGrupoInvitationDTO(new GrupoInvitationDTO(
				mapper.doit(gi.getGrupoInvitationId().getUsuarioInvitante()), 
				gi.getRole().name(),
				mapper.doit(gi.getAes()),
				gi.getPrivateKey()
				));

		// enviar invitacion
		
		ProtocoloDTO p = webSocketSender.buildProtocoloDTO(
				Constant.PROTOCOLO_COMPONENT_GRUPOS,
				Constant.PROTOCOLO_ACTION_GRUPO_INVITATION_RECIVED, 
				ginfo);
		

			webSocketSender.sender(gi.getGrupoInvitationId().getUsuarioInvitado() ,p);

		
		
	}
	
	public GrupoDTO acceptInvitation(GrupoInvitation gi, AES aes) throws PrivacityException  {
		
		
		UserForGrupo ug = new UserForGrupo();
		ug.setUserForGrupoId( new UserForGrupoId(gi.getGrupoInvitationId().getUsuarioInvitado(), gi.getGrupoInvitationId().getGrupo()));
		ug.setRole(gi.getRole());
		ug.setAes(aes);
		
		
		
		userForGrupoRepository.save(ug);
		
		// aviso a todos para q lo agreguen al grupo menos al invitado
		//GrupoDTO dto = getGrupo(gi.getGrupoInvitationId().getGrupo(),gi.getGrupoInvitationId().getUsuarioInvitado());
		{
//			ProtocoloDTO p = webSocketSender.buildProtocoloDTO(
//											Constant.PROTOCOLO_COMPONENT_GRUPOS,
//					                        Constant.PROTOCOLO_ACTION_GRUPO_ADDUSER_ADDME,
//					                        dto);
//	
//			webSocketSender.sender(gi.getGrupoInvitationId().getUsuarioInvitado(), p);
		}
		
		//ACA DEBE INFORMAR A TODOS LOS SUSCRIPTORES EL INGRESO DEL NUEVO MIEMBRO
		{

			MessageDTO mensajeD = webSocketSender.buildSystemMessage(gi.getGrupoInvitationId().getGrupo(), "SE HA AGREGADO EL USUARIO : " + gi.getGrupoInvitationId().getUsuarioInvitado().getNickname() + " AL GRUPO " + gi.getGrupoInvitationId().getGrupo().getName() + " POR " + gi.getGrupoInvitationId().getUsuarioInvitante().getNickname());
			Message mensaje = mapper.doit(mensajeD, usuarioService.getUsuarioSystem());
			messageController.sendNormal(mensaje);
			
			grupoInvitationRepository.delete(gi);
		}
		{


			GrupoDTO dto = getGrupo(grupoUtilService.getGrupoById(ug.getUserForGrupoId().getGrupo().getIdGrupo()), ug.getUserForGrupoId().getUser());
			return dto; 
		}
	}  	


	
	public GrupoDTO[] listarMisGrupos(Usuario u) throws Exception {
		
		UserForGrupo ug = new UserForGrupo();
		
		ug.setUserForGrupoId( new UserForGrupoId(u));
		
		List<GrupoInvitation> lgi = grupoInvitationRepository.findByGrupoInvitationIdUsuarioInvitado(u);
		
		List<UserForGrupo> l = userForGrupoRepository.findByUserForGrupoIdUser(u.getIdUser());
		
		GrupoDTO[] r = new GrupoDTO[l.size() + lgi.size()];
		int i =0;
		for ( GrupoInvitation e : lgi ) {
			GrupoDTO ginfo = getGrupo(e.getGrupoInvitationId().getGrupo(), u);
			ginfo.setGrupoInvitation(true);
			ginfo.setGrupoInvitationDTO(new GrupoInvitationDTO(
					mapper.doit(e.getGrupoInvitationId().getUsuarioInvitante()), 
					e.getRole().name(),
					mapper.doit(e.getAes()),
					e.getPrivateKey()
					));
			r[i] = ginfo;
			i ++;
		}
		
		for ( UserForGrupo e : l ) {
			GrupoDTO ginfo = getGrupo(e.getUserForGrupoId().getGrupo(), u);
			ginfo.setGrupoInvitation(false);
			r[i] = ginfo;
			i ++;
		}

		return r;
		
		
	}

	public InitGrupoResponse initGrupo(Usuario u, Grupo g) throws Exception {

		List<Usuario> usuarios = userForGrupoRepository.findByUsuariosForGrupo(g.getIdGrupo());

		InitGrupoResponse response = new InitGrupoResponse();
		response.setUsersDTO(new UsuarioDTO[usuarios.size()]); 
	
		for (int i = 0 ; i < usuarios.size() ; i++ ) {
			
			
			response.getUsersDTO()[i] = mapper.doit(usuarios.get(i));

		}
		
		
		List<Message> mensajes = messageRepository.findByMessageIdGrupoIdGrupo(g.getIdGrupo());

		MessageDTO[] mensajesDTO = new MessageDTO[mensajes.size()];
		
		
		for (int i = 0 ; i < mensajes.size() ; i++ ) {
			Message m = mensajes.get(i);
	    	
			MessageDTO mensaje = new MessageDTO();
	    	
			mensaje.setIdGrupo(m.getMessageId().getGrupo().getIdGrupo().toString());
			mensaje.setIdMessage(m.getMessageId().getIdMessage().toString());
			mensaje.setText(m.getText());
			mensaje.setUsuarioCreacion(mapper.doit(m.getUserCreation()));
	    	
	    	List<MessageDetail> detalles = messageDetailRepository.findByMessageUser(m.getMessageId().getIdMessage(),u.getIdUser());
	    	
			mensaje.setMessagesDetailDTO(new MessageDetailDTO[detalles.size()]);

			int j=0;
	    	for (MessageDetail d : detalles) {
	    		MessageDetailDTO dto = new MessageDetailDTO();
	    		//dto.setIdMessageDetail(d.getMessageDetailId().getIdMessageDetail().toString());
	    		
	    		dto.setEstado(d.getState().name());
	    		
				dto.setUsuarioDestino(mapper.doit(d.getMessageDetailId().getUserDestino()));
				
	    		//dto.setUserDestino(d.getMessageDetailId().getUserDestino().getUsername());
	    		
	    		
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


	private void removeMeAnonimo(Usuario usuarioLogged, Grupo grupo) throws PrivacityException {
		List<Message> l = messageRepository.findByMessageIdGrupoUserAnonimo(grupo, usuarioLogged);
		
		for (Message m : l) {
			messageController.deleteForEveryone(m);
		}
	}


	public void removeMe(Usuario usuarioLogged, Grupo grupo, Usuario usuarioSystem, UserForGrupo userForGrupo) throws PrivacityException {
		removeMeAnonimo(usuarioLogged, grupo);
		
		//mediaRepository.deleteAllMyMediaByGrupo(grupo, usuarioLogged);
		
		messageDetailRepository.deleteAllMyMessagesDetailByGrupo(grupo, usuarioLogged);
		
		messageRepository.deleteAllMyMessagesByGrupo(grupo, usuarioLogged);
		
		userForGrupoRepository.delete(userForGrupo);

		List<Usuario> usuarios = userForGrupoRepository.findByUsuariosForGrupo(grupo.getIdGrupo());
		//avisar q se fue, borrar todos los mensajes y sacarlo del grupo
		
		
		GrupoRemoveMeResponseDTO r = new GrupoRemoveMeResponseDTO();
		
		GrupoDTO grupoRemove = new GrupoDTO();
		grupoRemove.setIdGrupo(grupo.getIdGrupo()+"");
		
		r.setGrupoDTO(grupoRemove);
		
		UsuarioDTO usuarioRemove = new UsuarioDTO();
		usuarioRemove.setIdUsuario(usuarioLogged.getIdUser()+"");
		r.setUsuariosDTO(usuarioRemove);
		


		
		ProtocoloDTO p = webSocketSender.buildProtocoloDTO(
				Constant.PROTOCOLO_COMPONENT_GRUPOS,
				Constant.PROTOCOLO_ACTION_GRUPO_REMOVE_USER, 
				grupoRemove);
		
		for (Usuario usuarioToAvisarRemove : usuarios){
			webSocketSender.sender(usuarioToAvisarRemove ,p);
		}
		//ACA DEBE INFORMAR A TODOS LOS SUSCRIPTORES EL INGRESO DEL NUEVO MIEMBRO
	

			MessageDTO mensaje = webSocketSender.buildSystemMessage(grupo, "USUARIO " + usuarioLogged.getNickname() + " HA DEJADO EL GRUPO " + grupo.getName());
			messageController.sendNormal(mapper.doit(mensaje, usuarioSystem));

	} 
	
	
    	
}
