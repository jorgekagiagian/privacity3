package com.privacity.common.dto.response;

import java.util.List;

import com.privacity.common.annotations.PrivacityId;
import com.privacity.common.dto.AESDTO;
import com.privacity.common.dto.EncryptKeysDTO;

import lombok.Data;
@Data
public class LoginDTOResponse {
	public String token;
	public String type = "Bearer";
	@PrivacityId
	public String id;
	public String nickname;
	public List<String> roles;

	public String invitationCode;
	
	public AESDTO sessionAESDTO;
	public EncryptKeysDTO encryptKeysDTO;

	
	public LoginDTOResponse(String accessToken, Long id, 
			String nickname,List<String> roles, AESDTO sessionAESDTO,
			EncryptKeysDTO encryptKeysDTO,  String invitationCode) {
		this.token = accessToken;
		this.id = id+"";
		this.nickname = nickname;
		this.roles = roles;
		this.sessionAESDTO = sessionAESDTO;
		this.encryptKeysDTO = encryptKeysDTO;
		this.invitationCode = invitationCode;
		
	}

}
