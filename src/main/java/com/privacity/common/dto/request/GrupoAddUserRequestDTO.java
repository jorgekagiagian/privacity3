package com.privacity.common.dto.request;

import com.privacity.common.annotations.PrivacityId;
import com.privacity.common.dto.AESDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoAddUserRequestDTO{
	
	@PrivacityId
	public String idGrupo;
	public String invitationCode;
	public String role;
	public AESDTO aesDTO;
}