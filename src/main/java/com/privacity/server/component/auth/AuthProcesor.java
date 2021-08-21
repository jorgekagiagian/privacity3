package com.privacity.server.component.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.privacity.common.dto.response.LoginDTOResponse;
import com.privacity.server.component.usuario.UsuarioService;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.EncryptKeys;
import com.privacity.server.security.ERole;
import com.privacity.server.security.JwtUtils;
import com.privacity.server.security.Role;
import com.privacity.server.security.RoleRepository;
import com.privacity.server.security.UserDetailsImpl;
import com.privacity.server.security.UserRepository;
import com.privacity.server.security.Usuario;
import com.privacity.server.util.Mapper;



@CrossOrigin(origins = "*", maxAge = 3600)
@Service
//@RequestMapping("/api/auth")
public class AuthProcesor {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	Mapper mapper;

	
	@Autowired
	UsuarioService usuarioService;
	
	public LoginDTOResponse login( Usuario loginRequest) throws ValidationException {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getUsuarioPassword().getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		//List<UsuarioSessionIdInfo> info = SocketSessionRegistry.getSessionIds(userDetails.getUsername());
		
		return new LoginDTOResponse(jwt, 
				 userDetails.getId(), 
				 userDetails.getNickname(), 
				 roles,
				 userDetails.getSessionEncrypt(),
				 mapper.doit(userDetails.getEncryptKeys()),
				 usuarioService.getUsuarioForUsername(loginRequest.getUsername()).getUsuarioInvitationCode().getInvitationCode()
				 );
	}

	public Boolean validateUsername(String username) {
		if (userRepository.existsByUsername(username)) {
			return true;
		}
		return false;
	}
	
	public void registerUser(Usuario usuario) throws ValidationException {
		

		// Create new user's account
		Usuario user = new Usuario(usuario.getUsername(),
							 encoder.encode(usuario.getUsuarioPassword().getPassword()));

		user.setNickname(usuario.getNickname());
		Set<Role> roles = new HashSet<>();

		Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
		roles.add(userRole);

		user.setRoles(roles);
		usuario.getUsuarioInvitationCode().setUsuario(user);
		user.setEncryptKeys(usuario.getEncryptKeys());
		user.setUsuarioInvitationCode(usuario.getUsuarioInvitationCode());
		userRepository.save(user);

	
	}
}
