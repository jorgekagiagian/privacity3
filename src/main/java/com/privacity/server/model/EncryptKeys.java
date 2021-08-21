package com.privacity.server.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "encrypt_keys")
public class EncryptKeys implements Serializable{
	
	private static final long serialVersionUID = -4076483467987157286L;

   

	@Id
	@GeneratedValue
    private Long id;
	
	@Lob
	private String publicKey; 
	@Lob
	private String privateKey;

	@Lob
	private String publicKeyNoEncrypt; 	
}
