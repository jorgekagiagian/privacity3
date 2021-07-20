package com.privacity.server.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForGrupo {

	@EmbeddedId
	private UserForGrupoId userForGrupoId; 
	
	private String usernameGrupoToShow;
}
