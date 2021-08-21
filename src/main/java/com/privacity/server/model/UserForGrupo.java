package com.privacity.server.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.privacity.common.enumeration.GrupoRolesEnum;

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
	
	private GrupoRolesEnum role;


    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name="id_aes")
    private AES aes;

    private String nicknameGrupo;

}
