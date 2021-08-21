package com.privacity.server.component.myaccount;

import org.springframework.stereotype.Service;

import com.privacity.common.config.ConstantValidation;
import com.privacity.common.dto.EncryptKeysDTO;
import com.privacity.common.dto.UsuarioDTO;
import com.privacity.common.dto.UsuarioInvitationCodeDTO;
import com.privacity.common.dto.response.MyAccountGenerateInvitationCodeResponseDTO;
import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.server.component.encryptkeys.EncryptKeysValidation;
import com.privacity.server.component.usuario.UsuarioService;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.EncryptKeys;
import com.privacity.server.security.UserRepository;
import com.privacity.server.security.Usuario;
import com.privacity.server.util.Mapper;
import com.privacity.server.util.UtilService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MyAccountValidationService {


	private MyAccountService myAccountService;
	private UsuarioService usuarioService;
	private EncryptKeysValidation encryptKeysValidation;
	private Mapper mapper;
	UserRepository userRepository;
	private UtilService utilService;
	
	public void save(UsuarioDTO request) throws ValidationException{
		
		if (request.getNickname() == null || request.getNickname().trim().equals("")) {
			throw new ValidationException(ExceptionReturnCode.USER_NICKNAME_IS_NULL);				
		}else if (request.getNickname().length() > ConstantValidation.USER_NICKNAME_MAX_LENGTH) {
			throw new ValidationException(ExceptionReturnCode.USER_NICKNAME_TOO_LONG);
		}
		request.setNickname(request.getNickname().trim());
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();
		usuarioLogged.setNickname(request.getNickname());
		myAccountService.save(usuarioLogged);
		
	}
	
	public MyAccountGenerateInvitationCodeResponseDTO invitationCodeGenerator(EncryptKeysDTO request) throws ValidationException{
		
		encryptKeysValidation.encryptKeysInvitationCodeDTO(request);
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();
		
		EncryptKeys encryptKeys = mapper.doit(request);
		
		return new MyAccountGenerateInvitationCodeResponseDTO(myAccountService.invitationCodeGenerator(usuarioLogged,encryptKeys,utilService.invitationCodeGenerator()));
	}
	
	public Boolean isInvitationCodeAvailable(String invitationCode) throws ValidationException{
		
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();

		Usuario usuarioInvitationCode = userRepository.findByUsuarioInvitationCode(invitationCode);
		
		if (usuarioInvitationCode == null ) return true;
		
		if (usuarioLogged.getIdUser().equals(usuarioInvitationCode.getIdUser())) return true;
		
		return false;
		
	}
	
	public MyAccountGenerateInvitationCodeResponseDTO saveCodeAvailable(UsuarioInvitationCodeDTO request) throws ValidationException{
		
		if (request.getInvitationCode() == null || request.getInvitationCode().equals("")) {
			throw new ValidationException(ExceptionReturnCode.MYACCOUNT_INVITATION_CODE_CANT_BE_EMPTY);
		}
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();

		Usuario usuarioInvitationCode = userRepository.findByUsuarioInvitationCode(request.getInvitationCode());
		

		if ( usuarioInvitationCode != null) {
			if (usuarioLogged.getIdUser().equals(usuarioInvitationCode.getIdUser())) {
				return new MyAccountGenerateInvitationCodeResponseDTO(request.getInvitationCode());
			}else {
				throw new ValidationException(ExceptionReturnCode.MYACCOUNT_INVITATION_CODE_NOT_AVAIBLE);		
			}
		}
		
		encryptKeysValidation.encryptKeysInvitationCodeDTO(request.getEncryptKeysDTO());
		
		EncryptKeys encryptKeys = mapper.doit(request.getEncryptKeysDTO());
		
		return new MyAccountGenerateInvitationCodeResponseDTO(myAccountService.invitationCodeGenerator(usuarioLogged,encryptKeys,request.getInvitationCode()));

		
			
	}
}
