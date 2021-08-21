package com.privacity.common.dto.request;

import com.privacity.common.annotations.PrivacityId;
import com.privacity.common.dto.AESDTO;

import lombok.Data;

@Data
public class GrupoInvitationAcceptRequestDTO {
	@PrivacityId
	public String idGrupo;
	@PrivacityId
	public String idUsuarioInvitante;
	@PrivacityId
	public String idUsuarioInvitado;
	public AESDTO aesDTO;
}
