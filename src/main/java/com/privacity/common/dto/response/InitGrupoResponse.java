package com.privacity.common.dto.response;

import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.MessageDetailDTO;
import com.privacity.common.dto.UsuarioDTO;

import lombok.Data;

@Data
public class InitGrupoResponse {

	public UsuarioDTO[] usersDTO;
	public MessageDTO[] messagesDTO;
	public MessageDetailDTO[] messagesDetailState;
	
}
