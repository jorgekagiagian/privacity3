package com.privacity.server.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.privacity.server.model.EncryptKeys;
import com.privacity.server.model.UsuarioInvitationCode;

import lombok.Data;

@Data
@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
		})
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idUser;

	
	private String username;

	private String nickname;

    @OneToOne(mappedBy = "usuario", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UsuarioPassword usuarioPassword;

    @OneToOne( cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="id_encrypt_keys")
    private EncryptKeys encryptKeys;

    @OneToOne(mappedBy = "usuario", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UsuarioInvitationCode usuarioInvitationCode;   
    
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public Usuario() {
	}

	public Usuario(String username, String password) {
		this.username = username;
		usuarioPassword = new UsuarioPassword(this, password);
		
	}


}
