package com.privacity.server.component.message;

import org.springframework.stereotype.Service;

import com.privacity.common.dto.GrupoDTO;
import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.MessageDetailDTO;
import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.common.enumeration.MessageState;
import com.privacity.server.component.grupo.GrupoUtilService;
import com.privacity.server.component.messagedetail.MessageDetailUtil;
import com.privacity.server.component.userforgrupo.UserForGrupoUtil;
import com.privacity.server.component.usuario.UsuarioService;
import com.privacity.server.exceptions.PrivacityException;
import com.privacity.server.exceptions.ProcessException;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.Grupo;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageDetail;
import com.privacity.server.security.Usuario;
import com.privacity.server.util.Mapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageValidationService {
	
	private UsuarioService usuarioService;
	private GrupoUtilService grupoUtilService;
	private MessageUtilService messageUtilService;
	private MessageService messageService;
	private MessageDetailUtil messageDetailUtil;
	private Mapper mapper;
	private UserForGrupoUtil userForGrupoUtil;
	
	public MessageDetailDTO deleteForMe(MessageDetailDTO request) throws ValidationException, ProcessException {
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();
		Grupo grupo = grupoUtilService.getGrupoById(request.getIdGrupo());
		Message message = messageUtilService.getMessage(grupo,request.getIdMessage());

		MessageDetail detail = messageDetailUtil.getMessageDetail(message, usuarioLogged);
		
		if (detail.isLogicDeleted()) {
			throw new ValidationException(ExceptionReturnCode.MESSAGEDETAIL_IS_DELETED);
		}
		messageService.deleteForMe(detail);
		
		return request;
		
	}
	
	public MessageDTO deleteForEveryone(MessageDetailDTO request) throws PrivacityException {
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();
		Grupo grupo = grupoUtilService.getGrupoById(request.getIdGrupo());
		Message message = messageUtilService.getMessage(grupo,request.getIdMessage());

		if (!usuarioLogged.getUsername().equals(message.getUserCreation().getUsername())) {
			throw new ValidationException(ExceptionReturnCode.MESSAGE_NOT_MESSAGE_CREATOR);
		}
		
		
		return messageService.deleteForEveryone(message);
		
	}
	
	public MessageDTO get(MessageDTO request) throws Exception {
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();
		Grupo grupo = grupoUtilService.getGrupoById(request.getIdGrupo());
		
		userForGrupoUtil.validateGrupoMember(usuarioLogged, grupo);
		

		Message m = messageUtilService.getMessage(grupo, request.getIdMessage());
		
		messageDetailUtil.getMessageDetailValidateTimeMessage(m, usuarioLogged);
		
		return messageService.get(m,usuarioLogged);
	}
	
	public MessageDTO[] getAllidMessageUnreadMessages() throws Exception {
		return messageService.getAllidMessageUnreadMessages();
	}
	
	public void emptyList(GrupoDTO grupo) throws Exception {
		messageService.emptyList(grupo.getIdGrupo());
	}
	
	public void deleteAllMyMessageForEverybodyByGrupo(String idGrupo) throws Exception {
		messageService.emptyList(idGrupo);
	}
	
	public MessageDetailDTO changeState(MessageDetailDTO request) throws Exception {
		
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();
		Grupo grupo = grupoUtilService.getGrupoById(request.getIdGrupo());
		userForGrupoUtil.validateGrupoMember(usuarioLogged, grupo);
		Message m = messageUtilService.getMessage(grupo, request.getIdMessage());
		MessageDetail md = messageDetailUtil.getMessageDetail(m, usuarioLogged);
		
		MessageState state = MessageState.valueOf(request.getEstado());
		
		return messageService.changeState(usuarioLogged, m, md, state);
	}
	
	public MessageDTO[] loadMessages(MessageDTO request) throws Exception {
		return messageService.loadMessages(request);
	}
	
	public MessageDTO send(MessageDTO request) throws Exception {
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();
		Grupo grupo = grupoUtilService.getGrupoById(request.getIdGrupo());
		userForGrupoUtil.validateGrupoMember(usuarioLogged, grupo);
		Message m = mapper.doit(request, usuarioLogged,true);

	
		return messageService.send(m);
	}

}
