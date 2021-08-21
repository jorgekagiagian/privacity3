package com.privacity.server.component.messagedetail;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.common.enumeration.MessageState;
import com.privacity.server.component.userforgrupo.UserForGrupoRepository;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageDetail;
import com.privacity.server.model.MessageDetailId;
import com.privacity.server.model.MessageId;
import com.privacity.server.security.Usuario;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MessageDetailUtil {
	
	private MessageDetailRepository messageDetailRepository;
	private UserForGrupoRepository userForGrupoRepository;


	
	public MessageDetail getMessageDetail(Message message, Usuario usuario) throws ValidationException {
		
		Optional<MessageDetail> mOptional;
		
		MessageDetailId id = new MessageDetailId();
		id.setMessage(message);
		id.setUserDestino(usuario);
		
		mOptional = messageDetailRepository.findById(id);
		
		if (!mOptional.isPresent()) {
			throw new ValidationException(ExceptionReturnCode.MESSAGEDETAIL_NOT_EXISTS);	
		}
		
		
		return mOptional.get();
	}
	
	public MessageDetail getMessageDetailValidateTimeMessage(Message message, Usuario usuario) throws ValidationException {
		
		MessageDetail r = getMessageDetail(message,usuario);
		if (message.isTimeMessage() 
				&& r.getState().equals(MessageState.DESTINY_READED)) {
			throw new ValidationException(ExceptionReturnCode.MESSAGEDETAIL_NOT_EXISTS_TIME_MESSAGE);
			
		}
		
		if (r.isLogicDeleted()) {
			throw new ValidationException(ExceptionReturnCode.MESSAGEDETAIL_IS_DELETED);			
		}
		return r;
	}
	
	public Set<MessageDetail> generateMessagesDetail(Message message) {
		Set<MessageDetail> r = new HashSet<MessageDetail>();
		List<Usuario> usuarios = userForGrupoRepository.findByUsuariosForGrupo(message.getMessageId().getGrupo().getIdGrupo());
		
		for ( Usuario destino : usuarios) {
						
			MessageDetail md = new MessageDetail();

			if ( destino.getUsername().equals(message.getUserCreation().getUsername())){
				md.setState(MessageState.MY_MESSAGE_SENT);
			}else {
				md.setState(MessageState.DESTINY_SERVER);
			}

			md.setMessageDetailId(new MessageDetailId(destino, message) );
 
			r.add(md);
		}
		
		return r;
	}

}
