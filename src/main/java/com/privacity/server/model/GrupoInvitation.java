package com.privacity.server.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.privacity.common.enumeration.GrupoRolesEnum;

import lombok.Data;

@Entity
@Data
public class GrupoInvitation implements Serializable{
	
	private static final long serialVersionUID = -2737865014106237360L;

	@EmbeddedId
	private GrupoInvitationId grupoInvitationId;
	
    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name="id_aes")
	private AES aes; 
    
	private GrupoRolesEnum role;
	
	@Lob
	private String privateKey;
	
 

}
