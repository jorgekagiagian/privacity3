package com.privacity.server.component.grupo;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.common.enumeration.GrupoRolesEnum;
import com.privacity.server.component.usuario.UsuarioService;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.Grupo;
import com.privacity.server.security.Usuario;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GrupoUtilService {

	private GrupoRepository grupoRepository;
	private UsuarioService usuarioService;
	
	public Grupo getGrupoById(String idGrupo) throws ValidationException {
		return getGrupoById(Long.parseLong(idGrupo));
	}
	public Grupo getGrupoById(Long idGrupo) throws ValidationException {
		
		Optional<Grupo> g;
		try {
			g = grupoRepository.findById(idGrupo);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new ValidationException(ExceptionReturnCode.GRUPO_GRUPOID_BADFORMAT);
		} 
		
		if (!g.isPresent()) {
			// TODO Auto-generated catch block
			throw new ValidationException(ExceptionReturnCode.GRUPO_NOT_EXISTS);
		}
		
		return g.get();
	}
	
	public Long generateIdGrupo() {
		return Long.parseLong ((new Date().getTime()+"") + RandomStringUtils.randomNumeric(6));
	}
	
	public void validateRoleAdmin(Usuario u, Grupo g ) throws ValidationException {
		GrupoRolesEnum rol = usuarioService.getRoleForGrupo(u, g);
		
		if ( !rol.equals(GrupoRolesEnum.ADMIN)) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_USER_NOT_HAVE_PERMITION_ON_THIS_GRUPO_TO_ADD_MEMBERS);	
		}
	}
	


	public GrupoRolesEnum getGrupoRoleEnum(String role) throws ValidationException {
		GrupoRolesEnum r;
		try {
			r = GrupoRolesEnum.valueOf(role);	
		}catch (Exception e) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_ROLE_NOT_EXISTS);
		}
		return r;
		
	}
	
	public void isSomeUser(Usuario usuario1, Usuario usuario2) throws ValidationException {
		if (usuario1.getIdUser().equals(usuario2.getIdUser())){
			throw new ValidationException(ExceptionReturnCode.GRUPO_USER_INVITATION_CANT_BE_THE_SAME);
		}
		
	}
	
}
