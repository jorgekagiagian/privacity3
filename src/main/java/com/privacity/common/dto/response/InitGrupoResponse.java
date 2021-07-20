package com.privacity.common.dto.response;

import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.MessageDetailDTO;
import com.privacity.common.dto.UserDTO;

import lombok.Data;

@Data
public class InitGrupoResponse {

	private UserDTO[] usersDTO;
	private MessageDTO[] messagesDTO;
	private MessageDetailDTO[] messagesDetailState;
	
}
