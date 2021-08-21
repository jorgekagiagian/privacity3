package com.privacity.server.component.usuario;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.common.enumeration.GrupoRolesEnum;
import com.privacity.server.component.userforgrupo.UserForGrupoRepository;
import com.privacity.server.exceptions.ProcessException;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.Grupo;
import com.privacity.server.model.UserForGrupo;
import com.privacity.server.model.UserForGrupoId;
import com.privacity.server.security.UserRepository;
import com.privacity.server.security.Usuario;

@Service
public class UsuarioService {

	private UserRepository userRepository;
	private UserForGrupoRepository userForGrupoRepository;
	
	public UsuarioService(UserRepository userRepository, UserForGrupoRepository userForGrupoRepository) {
		super();
		this.userRepository = userRepository;
		this.userForGrupoRepository = userForGrupoRepository;

	}

	private Usuario getUsuarioLogged() {
		Authentication auth = SecurityContextHolder
	            .getContext()
	            .getAuthentication();
	    UserDetails userDetail = (UserDetails) auth.getPrincipal();
	    
		Usuario u = userRepository.findByUsername(userDetail.getUsername()).get();
		return u;
	}    
	
	public Usuario getUsuarioLoggedValidate() throws ValidationException {
	    
		Usuario u = getUsuarioLogged();
		
		if ( u.getIdUser() == null) {
			ValidationException e = new ValidationException(ExceptionReturnCode.USER_USER_NOT_LOGGER);
			
			throw e; 
		}
		return u;
	} 
	
	public Usuario getUsuarioSystem() throws ProcessException {
		Optional<Usuario> uSystemO = userRepository.findByUsername("SYSTEM");
		
		if ( uSystemO == null || uSystemO.get() == null) {
			throw new ProcessException(ExceptionReturnCode.USER_USER_SYSTEM_NOT_EXISTS); 
		}
		return uSystemO.get();
	}
	public Usuario getUsuarioById(String idUsuario) throws ValidationException {
		return getUsuarioById(Long.parseLong(idUsuario));
	}
	public Usuario getUsuarioById(Long idUsuario) throws ValidationException {
		Optional<Usuario> usuario = userRepository.findById(idUsuario);
		
		if ( usuario == null || !usuario.isPresent()) {
			throw new ValidationException(ExceptionReturnCode.USER_NOT_EXISTS); 
		}
		return usuario.get();
	}
	
	public GrupoRolesEnum getRoleForGrupo(Usuario usuario, Grupo grupo) throws ValidationException {
	    
		Optional<UserForGrupo> ufg = userForGrupoRepository.findById(new UserForGrupoId(usuario, grupo));
		
		if (!ufg.isPresent() ) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_USER_IS_NOT_IN_THE_GRUPO);				
		}
		
		return ufg.get().getRole();
	} 
	
	public Usuario getUsuarioForUsername(String usuario) throws ValidationException {
	    
		Optional<Usuario> u = userRepository.findByUsername(usuario);
		
		if (!u.isPresent() ) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_USER_IS_NOT_IN_THE_GRUPO);				
		}
		 
		return u.get();
	} 
}
