package com.privacity.server.component.encryptkeys;

import org.springframework.stereotype.Service;

import com.privacity.common.dto.EncryptKeysDTO;
import com.privacity.common.dto.request.PublicKeyByInvitationCodeRequestDTO;
import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.common.enumeration.GrupoRolesEnum;
import com.privacity.server.component.grupo.GrupoUtilService;
import com.privacity.server.component.usuario.UsuarioService;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.EncryptKeys;
import com.privacity.server.model.Grupo;
import com.privacity.server.security.UserRepository;
import com.privacity.server.security.Usuario;
import com.privacity.server.util.Mapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EncryptKeysService {

	private UsuarioService	usuarioService;
	private Mapper mapper;
	private UserRepository userRepository;
	private GrupoUtilService grupoUtilService;

	
	public EncryptKeysDTO getPublicKeyByCodigoInvitacion(PublicKeyByInvitationCodeRequestDTO request) throws ValidationException {
	
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();

		Grupo g = grupoUtilService.getGrupoById(request.getIdGrupo());

		GrupoRolesEnum rol = usuarioService.getRoleForGrupo(usuarioLogged, g);
		
		if ( !rol.equals(GrupoRolesEnum.ADMIN)) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_USER_NOT_HAVE_PERMITION_ON_THIS_GRUPO_TO_ADD_MEMBERS);	
		}
		
		
		if ( request.getInvitationCode() == null || request.getInvitationCode().trim().length() == 0) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_USER_INVITATION_CODE_IS_NULL); 
		}

		
		Usuario usuarioInvitationCode = userRepository.findByUsuarioInvitationCode(request.getInvitationCode());

		if ( usuarioInvitationCode == null) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_USER_NOT_EXISTS_INVITATION_CODE); 
		}
		
		 EncryptKeys e = usuarioInvitationCode.getEncryptKeys();
		 
		 EncryptKeysDTO r = mapper.doitPublicKeyNoEncrypt(e);
				
		return r;
			 
		 }

}
