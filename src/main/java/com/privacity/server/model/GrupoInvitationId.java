package com.privacity.server.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.privacity.common.enumeration.GrupoRolesEnum;
import com.privacity.server.security.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GrupoInvitationId  implements Serializable {

	private static final long serialVersionUID = -1657143766873819117L;

	@ManyToOne
	@JoinColumn(name = "idUserInvitado")
    private Usuario usuarioInvitado;

	@ManyToOne
	@JoinColumn(name = "idUserInvitante")
    private Usuario usuarioInvitante;

	@ManyToOne
	@JoinColumn(name = "idGrupo")
    private Grupo grupo;

	//private Long idGrupoInvitation;
	
	





    
 }