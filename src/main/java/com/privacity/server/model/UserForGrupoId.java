package com.privacity.server.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.privacity.server.security.Usuario;

import lombok.Data;

@Data
@Embeddable
public class UserForGrupoId implements Serializable {

    private static final long serialVersionUID = 1L;



    @OneToOne
    @JoinColumn(name = "id_grupo")
//    @PrimaryKeyJoinColumn(name="id_grupo")
		private Grupo grupo;
	

	@ManyToOne
	@JoinColumn(name = "idUser")
	private Usuario user;



    public UserForGrupoId() {

    }

	public UserForGrupoId(Usuario user, Grupo grupo) {
		super();
		this.user = user;
		this.grupo = grupo;

	}

	public UserForGrupoId(Usuario user) {
		super();
		this.user = user;
	}

	
}
