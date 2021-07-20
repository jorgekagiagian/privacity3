package com.privacity.common.dto;

import lombok.Data;


@Data
public class MessageDTO{

	private String idMessage;

	private String idGrupo;
	
	private String usernameCreation;
	
	private MessageDetailDTO[] MessagesDetailDTO;

//	public void setIdMessage(Long idMessage) {
//		this.idMessage = idMessage+"";
//		
//	}
//	public void setIdMessage(String idMessage) {
//		this.idMessage = idMessage;
//		
//	}

}