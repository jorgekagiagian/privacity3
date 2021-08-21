package com.privacity.server.component.myaccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.privacity.server.component.encryptkeys.EncryptKeysRepository;
import com.privacity.server.component.userforgrupo.UserForGrupoRepository;
import com.privacity.server.model.EncryptKeys;
import com.privacity.server.model.UsuarioInvitationCode;
import com.privacity.server.security.UserRepository;
import com.privacity.server.security.Usuario;
import com.privacity.server.util.UtilService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MyAccountService {


	UserForGrupoRepository userForGrupoRepository;


	UserRepository userRepository;
	EncryptKeysRepository encryptKeysRepository;
	//UsuarioInvitationCodeRepository usuarioInvitationCodeRepository;


	@Autowired
	private UtilService utilService;

	public void save(Usuario u){

		userRepository.save(u);
		
	}
	
	public String invitationCodeGenerator(Usuario u, EncryptKeys encryptKeys, String code){

		
		UsuarioInvitationCode uic;

		long oldEncryptKeysId = u.getUsuarioInvitationCode().getEncryptKeys().getId();
		
		uic = u.getUsuarioInvitationCode();
		
		uic.setEncryptKeys(encryptKeys);
		uic.setInvitationCode(code);
		//usuarioInvitationCodeRepository.save(uic);
		
		u.setUsuarioInvitationCode(uic);

		userRepository.save(u);
		
		encryptKeysRepository.deleteById(oldEncryptKeysId);
		
		return code;
	}  
	


}
