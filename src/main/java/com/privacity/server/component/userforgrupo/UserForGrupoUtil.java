package com.privacity.server.component.userforgrupo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.server.component.common.repository.FacadeService;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.Grupo;
import com.privacity.server.model.UserForGrupo;
import com.privacity.server.model.UserForGrupoId;
import com.privacity.server.security.Usuario;

import lombok.AllArgsConstructor;

@Service

public class UserForGrupoUtil {
	
	@Autowired
	private FacadeService facadeRepository;
	
	public boolean validateGrupoMember(Usuario u, Grupo g ) throws ValidationException {
		
		if ( !facadeRepository.getUserForGrupoRepository().findById(new UserForGrupoId(u, g)).isPresent() ) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_USER_IS_NOT_IN_THE_GRUPO);	
		}
		
		return true;
	}
	
	public String getNicknameForGrupo(Grupo g, Usuario u) {
		
		Optional<UserForGrupo> ufcO = facadeRepository.getUserForGrupoRepository().findById(new UserForGrupoId(u, g));
		
		if (!ufcO.isPresent()) return u.getNickname();
		
		if ( ufcO.get().getNicknameGrupo() == null ) {
			return u.getNickname();
		}
		return ufcO.get().getNicknameGrupo();
			
	}
}
