package com.privacity.common.dto;

import com.privacity.common.annotations.PrivacityId;

import lombok.Data;

@Data
public class UsuarioDTO{
	@PrivacityId
	public String idUsuario;
	public String nickname;
	
//	public UsuarioInvitationCodeDTO usuarioInvitationCodeDTO;
	public EncryptKeysDTO encryptKeysDTO;

}
