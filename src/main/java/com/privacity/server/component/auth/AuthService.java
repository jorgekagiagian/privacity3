package com.privacity.server.component.auth;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.privacity.common.config.ConstantValidation;
import com.privacity.common.dto.request.LoginRequestDTO;
import com.privacity.common.dto.request.RegisterUserRequestDTO;
import com.privacity.common.dto.request.ValidateUsernameDTO;
import com.privacity.common.dto.response.LoginDTOResponse;
import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.server.component.encryptkeys.EncryptKeysValidation;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.EncryptKeys;
import com.privacity.server.model.UsuarioInvitationCode;
import com.privacity.server.security.Usuario;
import com.privacity.server.util.Mapper;
import com.privacity.server.util.UtilService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

	public final static String METHOD_ACTION_AUTH_LOGIN ="login"; 
	public final static String METHOD_ACTION_AUTH_REGISTER ="registerUser";
	public final static String METHOD_ACTION_AUTH_VALIDATE_USERNAME ="validateUsername";
	
	private AuthProcesor authService;
	private Mapper mapper;
	private EncryptKeysValidation encryptKeysValidation;
	private UtilService utilService;


	public Boolean validateUsername(ValidateUsernameDTO request) throws ValidationException {
		String username = request.getUsername();
		
		return validateUsername(username);
		
	}
	
	public Boolean validateUsername(String username) throws ValidationException {
		
		if ( username == null) {
			throw new ValidationException(ExceptionReturnCode.AUTH_USERNAME_IS_NULL);
		}

		if ( username.length() < 3) {
			throw new ValidationException(ExceptionReturnCode.AUTH_USERNAME_IS_TOO_SHORT);
		}
		
		
		return authService.validateUsername(username);
	}
	
	public LoginDTOResponse login( LoginRequestDTO request) throws ValidationException {
		Usuario usuario = new Usuario(request.getUsername(),request.getPassword());
		
		return authService.login(usuario);
	}
	public void registerUser(RegisterUserRequestDTO request) throws ValidationException {
		if (validateUsername(request.getUsername())) {
			throw new ValidationException(ExceptionReturnCode.AUTH_USERNAME_EXISTS);
		}
		Usuario usuario = new Usuario(request.getUsername(),request.getPassword());
		
		if ( request.getNickname() == null || request.getNickname().trim().equals("")) {
			
			if (request.getNickname().length() > ConstantValidation.USER_NICKNAME_MAX_LENGTH) {
				throw new ValidationException(ExceptionReturnCode.USER_NICKNAME_TOO_LONG);
			}
			
			usuario.setNickname(generateRamdonNickname());
		}else {
			usuario.setNickname(request.getNickname());	
		}
		
		encryptKeysValidation.encryptKeysDTO(request.getEncryptKeysDTO());
		
		encryptKeysValidation.encryptKeysInvitationCodeDTO(request.getInvitationCodeEncryptKeysDTO());
		
		EncryptKeys encrypt = mapper.doit(request.getEncryptKeysDTO());
		EncryptKeys invitationCodeEncrypt = mapper.doit(request.getInvitationCodeEncryptKeysDTO());
		
		usuario.setEncryptKeys(encrypt);
		usuario.setUsuarioInvitationCode(new UsuarioInvitationCode());
		usuario.getUsuarioInvitationCode().setEncryptKeys(invitationCodeEncrypt);
		usuario.getUsuarioInvitationCode().setInvitationCode(utilService.invitationCodeGenerator());
		authService.registerUser(usuario);
	}
	
	private String generateRamdonNickname() {
		return RandomStringUtils.randomAlphabetic(4).toUpperCase();
	}
}
