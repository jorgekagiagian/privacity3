package com.privacity.common.dto;

import lombok.Data;

@Data
public class GrupoDTO{
	private String idGrupo;
	
	//@PrivacityPGPOut
	//@PrivacityPGPIn
	private String name;
	
	private UserDTO[] usersDTO;
	private int messageUnread;
	//public HashMap<String, UserDTO> mapa;


	
	public Long getIdGrupoLong() {
		return Long.parseLong(idGrupo);
	}
	

}
