package com.privacity.common.dto.request;

import com.privacity.common.dto.AESDTO;
import com.privacity.common.dto.GrupoDTO;

import lombok.Data;

@Data
public class GrupoNewRequestDTO {

	public GrupoDTO grupoDTO;
	
	// aca viene el par nuevo, el private encriptado por el publico del creador
	public AESDTO aesDTO;
}
