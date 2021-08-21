package com.privacity.common.dto.request;

import com.privacity.common.dto.EncryptKeysDTO;

import lombok.Data;

@Data
public class RegisterUserRequestDTO {
	
	public String username;
	public String nickname;
	public String password;

	public EncryptKeysDTO encryptKeysDTO;
	public EncryptKeysDTO invitationCodeEncryptKeysDTO;
}
