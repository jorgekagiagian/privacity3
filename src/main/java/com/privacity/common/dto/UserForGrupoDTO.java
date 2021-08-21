package com.privacity.common.dto;

import com.privacity.common.annotations.PrivacityId;
import com.privacity.common.enumeration.GrupoRolesEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForGrupoDTO {
	@PrivacityId
	public String idGrupo; 
	
	public UsuarioDTO usuario;
	public String role;
	public AESDTO aesDTO;
	
}
