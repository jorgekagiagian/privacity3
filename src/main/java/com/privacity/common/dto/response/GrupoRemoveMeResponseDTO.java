package com.privacity.common.dto.response;

import com.privacity.common.dto.GrupoDTO;
import com.privacity.common.dto.UserForGrupoDTO;
import com.privacity.common.dto.UsuarioDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoRemoveMeResponseDTO{
	
	public GrupoDTO grupoDTO;
	public UsuarioDTO usuariosDTO;

}