package com.privacity.server.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "aes")
@AllArgsConstructor
@NoArgsConstructor
public class AES implements Serializable{
	
	private static final long serialVersionUID = -4076483467987157286L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length=4096, name = "secret_key_aes")
	private String secretKeyAES; 
	@Column(length=4096, name = "salt_aes")
	private String saltAES;

	
}
