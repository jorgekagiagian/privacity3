package com.privacity.server.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_password")
public class UsuarioPassword implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2648617924882203314L;

	@Id
	@OneToOne
	//@Column(name="id_usuario")
    private Usuario usuario;
	
	private String password;

	@Override
	public String toString() {
		return "";
	}
}
