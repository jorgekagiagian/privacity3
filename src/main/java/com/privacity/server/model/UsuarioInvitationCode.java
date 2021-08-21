package com.privacity.server.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.privacity.server.security.Usuario;

import lombok.Data;

@Data
@Entity
@Table(	name = "user_invitation_code", 
uniqueConstraints = { 
	@UniqueConstraint(columnNames = "invitationCode"),
})
public class UsuarioInvitationCode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8724044696278202403L;

	@Id
	@OneToOne
    private Usuario usuario;
	
	
	private String invitationCode;
	
	

    @OneToOne( cascade = CascadeType.ALL)
    @JoinColumn(name="id_encrypt_keys")
    private EncryptKeys encryptKeys;



	@Override
	public String toString() {
		return "UsuarioInvitationCode [usuario=" + usuario.getIdUser() + ", invitationCode=" + invitationCode + ", encryptKeys="
				+ encryptKeys + "]";
	}


}
