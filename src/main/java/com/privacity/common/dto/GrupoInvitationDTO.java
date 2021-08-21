package com.privacity.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoInvitationDTO {
	
	public UsuarioDTO usuarioInvitante;
	public String role;

	public AESDTO aesDTO;
	public String privateKey;
}
