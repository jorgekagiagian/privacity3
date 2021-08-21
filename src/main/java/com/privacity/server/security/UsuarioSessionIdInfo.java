package com.privacity.server.security;

import java.security.PublicKey;

import com.privacity.common.dto.AESDTO;
import com.privacity.server.encrypt.RSA;
import com.privacity.server.main.SecretKeyPersonal;
import com.privacity.server.model.EncryptKeys;

import lombok.Data;

@Data
public class UsuarioSessionIdInfo {

	private String sessionId;
	private AESDTO sessionAESDTOToSend;
	private EncryptKeys encryptKeys;
	private RSA rsa;
	private PublicKey publicKey;
	private String publicKeyToSend;
	private String privateKeyToSend;
	private SecretKeyPersonal secretKeyPersonal;
}
