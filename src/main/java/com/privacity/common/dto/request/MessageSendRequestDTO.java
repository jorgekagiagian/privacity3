package com.privacity.common.dto.request;

import com.privacity.common.dto.MessageDetailDTO;

import lombok.Data;

@Data
public class MessageSendRequestDTO {
	private String idGrupo;
	private MessageDetailDTO[] messageDetails;
	
}
