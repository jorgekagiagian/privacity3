package com.privacity.server.component.message;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.Grupo;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageId;

@Service
public class MessageUtilService {
	
	private MessageRepository messageRepository;

	public MessageUtilService(MessageRepository messageRepository) {
		super();
		this.messageRepository = messageRepository;

	}

	public Message getMessage(Grupo grupo, String idMessage) throws ValidationException {
		
		Message m;
		try {
			m = messageRepository.findById(new MessageId(grupo, Long.parseLong(idMessage))).get();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new ValidationException(ExceptionReturnCode.MESSAGE_MESSAGEID_BADFORMAT);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			throw new ValidationException(ExceptionReturnCode.MESSAGE_NOT_EXISTS);
		}
		
		return m;
	}
	
	public Long generateIdMessage() {
		return Long.parseLong ((new Date().getTime()+"") + RandomStringUtils.randomNumeric(6));
	}	
	

	
	
}
