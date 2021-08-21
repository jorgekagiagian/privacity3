package com.privacity.server.main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.privacity.common.config.Constant;
import com.privacity.common.dto.EncryptKeysDTO;
import com.privacity.common.dto.GrupoDTO;
import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.MessageDetailDTO;
import com.privacity.common.dto.ProtocoloDTO;
import com.privacity.common.dto.UsuarioDTO;
import com.privacity.common.dto.UsuarioInvitationCodeDTO;
import com.privacity.common.dto.request.GrupoAddUserRequestDTO;
import com.privacity.common.dto.request.GrupoInvitationAcceptRequestDTO;
import com.privacity.common.dto.request.GrupoNewRequestDTO;
import com.privacity.common.dto.request.PublicKeyByInvitationCodeRequestDTO;
import com.privacity.common.dto.request.RequestEncryptDTO;
import com.privacity.server.component.common.ControllerBase;
import com.privacity.server.component.encryptkeys.EncryptKeysService;
import com.privacity.server.component.grupo.GrupoValidationService;
import com.privacity.server.component.message.MessageValidationService;
import com.privacity.server.component.myaccount.MyAccountValidationService;
import com.privacity.server.component.usuario.UsuarioService;
import com.privacity.server.encrypt.CryptSessionRegistry;
import com.privacity.server.encrypt.PrivacityIdServices;
import com.privacity.server.security.UserDetailsImpl;


@RestController
@RequestMapping(path = "/secure")

public class MainController extends ControllerBase{

	@Value("${privacity.security.encrypt.ids}")
	private boolean encryptIds;
	
	private PrivacityIdServices privacityIdServices;
	private GrupoValidationService grupoValidationService;
	private MessageValidationService messageValidationService;
	private EncryptKeysService encryptKeysValidationService;
	private MyAccountValidationService myAccountValidationService;
	private UsuarioService	usuarioService;
	
	public MainController(GrupoValidationService grupoValidationService,
			MessageValidationService messageValidationService, MyAccountValidationService myAccountValidationService,
			UsuarioService	usuarioService,EncryptKeysService encryptKeysValidationService,
			PrivacityIdServices privacityIdServices) throws Exception {
		super();
		this.usuarioService	= usuarioService;
		this.grupoValidationService = grupoValidationService;
		this.messageValidationService = messageValidationService;
		this.myAccountValidationService = myAccountValidationService;
		this.privacityIdServices = privacityIdServices;
		this.encryptKeysValidationService=encryptKeysValidationService;
		
		getMapaController().put(Constant.PROTOCOLO_COMPONENT_ENCRYPT_KEYS, encryptKeysValidationService);
		getMapaController().put("/grupo", grupoValidationService);
		getMapaController().put("/message", messageValidationService);
		getMapaController().put("/myAccount", myAccountValidationService);
		
		getMapaMetodos().put(Constant.PROTOCOLO_ACTION_ENCRYPT_KEYS_GET, EncryptKeysService.class.getMethod("getPublicKeyByCodigoInvitacion", PublicKeyByInvitationCodeRequestDTO.class));
//		getMapaMetodos().put(Constant.PROTOCOLO_ACTION_ENCRYPT_KEYS_CREATE, EncryptKeysValidationService.class.getMethod("create", EncryptKeysDTO.class));
		
		getMapaMetodos().put("/grupo/newGrupo", GrupoValidationService.class.getMethod("newGrupo", GrupoNewRequestDTO.class));
		getMapaMetodos().put("/grupo/listar/misGrupos", GrupoValidationService.class.getMethod("listarMisGrupos"));
		getMapaMetodos().put("/grupo/initGrupo", GrupoValidationService.class.getMethod("initGrupo", String.class));
		getMapaMetodos().put("/grupo/sentInvitation", GrupoValidationService.class.getMethod("sentInvitation", GrupoAddUserRequestDTO.class));
		
		getMapaMetodos().put("/grupo/acceptInvitation", GrupoValidationService.class.getMethod("acceptInvitation", GrupoInvitationAcceptRequestDTO.class));
		
		
		getMapaMetodos().put("/grupo/removeMe", GrupoValidationService.class.getMethod("removeMe", GrupoDTO.class));
		
		
		//getMapaMetodos().put("/message/send", MessageValidationService.class.getMethod("send", MessageDTO.class));
		//getMapaMetodos().put("/message/sendAnonimo", messageController.getClass().getMethod("sendAnonimo", MessageDTO.class));
		getMapaMetodos().put("/message/emptyList", MessageValidationService.class.getMethod("emptyList", GrupoDTO.class));
		
		getMapaMetodos().put("/message/deleteForMe", MessageValidationService.class.getMethod("deleteForMe", MessageDetailDTO.class));
		getMapaMetodos().put("/message/deleteForEveryone", MessageValidationService.class.getMethod("deleteForEveryone", MessageDetailDTO.class));
		
		getMapaMetodos().put(Constant.PROTOCOLO_ACTION_MESSAGE_GET_ALL_ID_MESSAGE_UNREAD, MessageValidationService.class.getMethod("getAllidMessageUnreadMessages"));		
		getMapaMetodos().put(Constant.PROTOCOLO_ACTION_MESSAGE_GET_MESSAGE, MessageValidationService.class.getMethod("get", MessageDTO.class));
		getMapaMetodos().put(Constant.PROTOCOLO_ACTION_MESSAGE_CHANGE_STATE, MessageValidationService.class.getMethod("changeState", MessageDetailDTO.class));
		
		getMapaMetodos().put("/message/get/loadMessages", MessageValidationService.class.getMethod("loadMessages", MessageDTO.class));
		getMapaMetodos().put("/myAccount/invitationCodeGenerator", MyAccountValidationService.class.getMethod("invitationCodeGenerator", EncryptKeysDTO.class));
		getMapaMetodos().put("/myAccount/isInvitationCodeAvailable", MyAccountValidationService.class.getMethod("isInvitationCodeAvailable", String.class));
		getMapaMetodos().put("/myAccount/saveCodeAvailable", MyAccountValidationService.class.getMethod("saveCodeAvailable", UsuarioInvitationCodeDTO.class));
		
		getMapaMetodos().put("/myAccount/save", MyAccountValidationService.class.getMethod("save", UsuarioDTO.class));

	}



		

	@PostMapping("/main")
	public ResponseEntity<String> inMain(@RequestBody String request) throws Exception {
		RequestEncryptDTO requestDTO = new Gson().fromJson(request, RequestEncryptDTO.class);
		
		request = requestDTO.getRequest();
		
		Authentication auth = SecurityContextHolder
	            .getContext()
	            .getAuthentication();
		UserDetailsImpl u = (UserDetailsImpl) auth.getPrincipal();
	    
		SecretKeyPersonal c = CryptSessionRegistry.getInstance().getSessionIds(u.getUsername()).getSecretKeyPersonal();
		
		String requestDesencriptado = c.getAESDecrypt(request);
		
		ProtocoloDTO p = new Gson().fromJson(requestDesencriptado, ProtocoloDTO.class);
		
		ProtocoloDTO retornoFuncion = super.in(p);
		String retornoFuncionJson = new Gson().toJson(retornoFuncion);
		System.out.println(">>" + retornoFuncionJson);
		String retornoFuncionEncriptado = c.getAES(retornoFuncionJson);
		
		System.out.println("ENCRIPTADO >>" + retornoFuncionEncriptado);
		return ResponseEntity.ok().body(new Gson().toJson(retornoFuncionEncriptado));

	}

	@Override
	public PrivacityIdServices getPrivacityIdServices() {
		// TODO Auto-generated method stub
		return this.privacityIdServices;
	}

	@Override
	public boolean getEncryptIds() {
		return encryptIds;
	}

}
