package com.privacity.common.dto;

import com.privacity.common.annotations.PrivacityId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoDTO{
	@PrivacityId
	public String idGrupo;
	
	public boolean grupoInvitation;
	public GrupoInvitationDTO grupoInvitationDTO;
	public UserForGrupoDTO[] usersForGrupoDTO;
	public String name;

	
	public GrupoDTO(String name) {
		super();
		this.name = name;
	}
	

}
