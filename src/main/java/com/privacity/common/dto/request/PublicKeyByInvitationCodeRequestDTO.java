package com.privacity.common.dto.request;

import com.privacity.common.annotations.PrivacityId;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublicKeyByInvitationCodeRequestDTO {

	@PrivacityId
	public String idGrupo;
	public String invitationCode;
	
}
