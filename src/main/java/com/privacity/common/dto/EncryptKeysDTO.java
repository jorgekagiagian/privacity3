package com.privacity.common.dto;

import lombok.Data;

@Data
public class EncryptKeysDTO {
	
	public String publicKey; 
	public String privateKey;

	public String publicKeyNoEncrypt; 
}
