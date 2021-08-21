package com.privacity.server.component.encryptkeys;

import org.springframework.stereotype.Service;

import com.privacity.common.dto.AESDTO;
import com.privacity.common.dto.EncryptKeysDTO;
import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.server.exceptions.ValidationException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EncryptKeysValidation {

	public void encryptKeysDTO(EncryptKeysDTO e) throws ValidationException {
		if (e == null) {
			throw new ValidationException(ExceptionReturnCode.ENCRYPT_IS_NULL);			
		}
		
		if (e.getPrivateKey() ==null || e.getPrivateKey().trim().length() == 0) {
			throw new ValidationException(ExceptionReturnCode.ENCRYPT_PRIVATE_KEY_IS_NULL);
		}
		
		if (e.getPublicKey() ==null|| e.getPublicKey().trim().length() == 0) {
			throw new ValidationException(ExceptionReturnCode.ENCRYPT_PUBLIC_KEY_IS_NULL);
		}
		
		if (e.getPublicKeyNoEncrypt() ==null || e.getPublicKeyNoEncrypt().trim().length() == 0) {
			throw new ValidationException(ExceptionReturnCode.ENCRYPT_PUBLIC_KEY_NO_ENCRIPT_IS_NULL);
		}
	}
	
	public void encryptKeysInvitationCodeDTO(EncryptKeysDTO e) throws ValidationException {
		if (e == null) {
			throw new ValidationException(ExceptionReturnCode.ENCRYPT_IS_NULL);			
		}
		
		if (e.getPrivateKey() ==null || e.getPrivateKey().trim().length() == 0) {
			throw new ValidationException(ExceptionReturnCode.ENCRYPT_PRIVATE_KEY_IS_NULL);
		}
		
		if (e.getPublicKey() !=null) {
			throw new ValidationException(ExceptionReturnCode.ENCRYPT_PUBLIC_KEY_MUST_BE_NULL);
		}
		
		if (e.getPublicKeyNoEncrypt() ==null || e.getPublicKeyNoEncrypt().trim().length() == 0) {
			throw new ValidationException(ExceptionReturnCode.ENCRYPT_PUBLIC_KEY_NO_ENCRIPT_IS_NULL);
		}
	}
	
	public void aesValitadation(AESDTO aesdto) throws ValidationException {
		if (aesdto == null) {
			throw new ValidationException(ExceptionReturnCode.ENCRYPT_AES_IS_NULL);			
		}
		
		if (aesdto.getSecretKeyAES() == null ||  aesdto.getSecretKeyAES().trim().length() == 0) {
			throw new ValidationException(ExceptionReturnCode.ENCRYPT_AES_SECRET_KEY_IS_NULL);
		}
		
		if (aesdto.getSaltAES() ==null ||  aesdto.getSaltAES().trim().length() == 0) {
			throw new ValidationException(ExceptionReturnCode.ENCRYPT_AES_SALT_IS_NULL);
		}
	}
	
}
