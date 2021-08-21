package com.privacity.server.component.grupo;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.privacity.common.dto.GrupoDTO;
import com.privacity.common.dto.request.GrupoAddUserRequestDTO;
import com.privacity.common.dto.request.GrupoInvitationAcceptRequestDTO;
import com.privacity.common.dto.request.GrupoNewRequestDTO;
import com.privacity.common.dto.response.InitGrupoResponse;
import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.common.enumeration.GrupoRolesEnum;
import com.privacity.server.component.encryptkeys.EncryptKeysValidation;
import com.privacity.server.component.grupoinvitation.GrupoInvitationRepository;
import com.privacity.server.component.message.MessageRepository;
import com.privacity.server.component.message.MessageService;
import com.privacity.server.component.messagedetail.MessageDetailRepository;
import com.privacity.server.component.userforgrupo.UserForGrupoRepository;
import com.privacity.server.component.usuario.UsuarioService;
import com.privacity.server.exceptions.PrivacityException;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.AES;
import com.privacity.server.model.Grupo;
import com.privacity.server.model.GrupoInvitation;
import com.privacity.server.model.GrupoInvitationId;
import com.privacity.server.model.UserForGrupo;
import com.privacity.server.model.UserForGrupoId;
import com.privacity.server.security.UserRepository;
import com.privacity.server.security.Usuario;
import com.privacity.server.util.Mapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GrupoValidationService {

	GrupoRepository grupoRepository;

	UserForGrupoRepository userForGrupoRepository;

	UserRepository userRepository;
	
	GrupoService grupoService;
	
	MessageRepository messageRepository;

	MessageDetailRepository messageDetailRepository;

	MessageService messageService;
		
	UsuarioService	usuarioService;
	
	GrupoUtilService grupoUtilService;
	
	private Mapper mapper;

	EncryptKeysValidation encryptKeysValidation;

	private GrupoInvitationRepository grupoInvitationRepository;



	public void removeMe(GrupoDTO request) throws PrivacityException {
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();

		Grupo grupo = grupoRepository.findById(Long.parseLong(request.getIdGrupo())).get();
		
		Usuario usuarioSystem = usuarioService.getUsuarioSystem();
		
		Optional<UserForGrupo> ufg = userForGrupoRepository.findById(new UserForGrupoId(usuarioLogged, grupo));
		
		if (!ufg.isPresent() ) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_USER_IS_NOT_IN_THE_GRUPO);				
		}
		
		grupoService.removeMe(usuarioLogged,grupo,usuarioSystem,ufg.get());
		
	}
	
	public GrupoDTO acceptInvitation(GrupoInvitationAcceptRequestDTO request) throws PrivacityException {
		Usuario usuarioInvitado = usuarioService.getUsuarioLoggedValidate();
		Usuario usuarioInvitante = usuarioService.getUsuarioById(request.idUsuarioInvitante);
		Grupo g = grupoUtilService.getGrupoById(request.getIdGrupo());
		grupoUtilService.validateRoleAdmin(usuarioInvitante, g);
		grupoUtilService.isSomeUser(usuarioInvitado,usuarioInvitante);
		encryptKeysValidation.aesValitadation(request.getAesDTO());		
		AES aes = mapper.doit(request.getAesDTO());
		
		Optional<GrupoInvitation> gi = grupoInvitationRepository.findById(new GrupoInvitationId(usuarioInvitado, usuarioInvitante, g));
		if (!gi.isPresent()) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_USER_NOT_EXISTS_INVITATION_CODE);
		}

		
		return grupoService.acceptInvitation(gi.get(), aes);
	}

	
	public void sentInvitation(GrupoAddUserRequestDTO request) throws PrivacityException {
		
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();

		Grupo g = grupoUtilService.getGrupoById(request.getIdGrupo());

		grupoUtilService.validateRoleAdmin(usuarioLogged, g);
		
		Usuario usuarioInvitationCode = userRepository.findByUsuarioInvitationCode(request.getInvitationCode());

		if ( usuarioInvitationCode == null) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_USER_NOT_EXISTS_INVITATION_CODE); 
		}
		
		grupoUtilService.isSomeUser(usuarioLogged,usuarioInvitationCode);
		
		
		// valido que el usuario no este en el grupo
		{
		 
			Optional<UserForGrupo> ufg = userForGrupoRepository.findById(new UserForGrupoId(usuarioInvitationCode, g));
			
			if (ufg.isPresent()) {
				throw new ValidationException(ExceptionReturnCode.GRUPO_USER_IS_IN_THE_GRUPO);				
			}
		}

		GrupoRolesEnum role = grupoUtilService.getGrupoRoleEnum(request.getRole());

		// valido que no haya ya sido invitado
		Optional<GrupoInvitation> gi = grupoInvitationRepository.findById(new GrupoInvitationId(usuarioInvitationCode, usuarioLogged, g));
		if (gi.isPresent()) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_INVITATION_EXIST_INVITATION);
		}
		//
		encryptKeysValidation.aesValitadation(request.getAesDTO());		
		AES aes = mapper.doit(request.getAesDTO());
		 
		grupoService.sentInvitation(g, role , usuarioLogged,usuarioInvitationCode, aes);
		
	}
	
	public GrupoDTO newGrupo(GrupoNewRequestDTO request) throws Exception {
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();
		
		Grupo g = new Grupo();
		g.setIdGrupo(grupoUtilService.generateIdGrupo());
		g.setName(request.getGrupoDTO().getName());
		

		encryptKeysValidation.aesValitadation(request.getAesDTO());		

		
		 AES aes = mapper.doit(request.getAesDTO());
		
		
		return grupoService.newGrupo(usuarioLogged, g, aes);
	}
	
	public GrupoDTO[] listarMisGrupos() throws Exception {
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();
		return grupoService.listarMisGrupos(usuarioLogged);
	}
	
	public InitGrupoResponse initGrupo(String request) throws Exception {
		Usuario usuarioLogged = usuarioService.getUsuarioLoggedValidate();

		Grupo g = grupoUtilService.getGrupoById(request);

		Optional<UserForGrupo> ufg = userForGrupoRepository.findById(new UserForGrupoId(usuarioLogged, g));
		
		if (!ufg.isPresent() ) {
			throw new ValidationException(ExceptionReturnCode.GRUPO_USER_IS_NOT_IN_THE_GRUPO);				
		}
		
		return grupoService.initGrupo(usuarioLogged, g);
	}
}
