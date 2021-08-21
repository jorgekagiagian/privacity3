package com.privacity.server.main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.privacity.common.config.Constant;
import com.privacity.common.dto.ProtocoloDTO;
import com.privacity.common.dto.request.LoginRequestDTO;
import com.privacity.common.dto.request.RegisterUserRequestDTO;
import com.privacity.common.dto.request.ValidateUsernameDTO;
import com.privacity.server.component.auth.AuthService;
import com.privacity.server.component.common.ControllerBase;
import com.privacity.server.encrypt.PrivacityIdServices;


@RestController
@RequestMapping(path = "/unsecure")
public class AuthController extends ControllerBase{

	@Value("${privacity.security.encrypt.ids}")
	private boolean encryptIds;
	
	@SuppressWarnings("unused")
	private AuthService authService;

	private PrivacityIdServices privacityIdServices;
	
	public AuthController(AuthService authValidationService, PrivacityIdServices privacityIdServices) throws Exception {
		this.authService = authValidationService;
		this.privacityIdServices = privacityIdServices;
		getMapaController().put(Constant.PROTOCOLO_COMPONENT_AUTH, authService);
		getMapaMetodos().put(Constant.PROTOCOLO_ACTION_AUTH_LOGIN, authService.getClass().getMethod(AuthService.METHOD_ACTION_AUTH_LOGIN, LoginRequestDTO.class));
		getMapaMetodos().put(Constant.PROTOCOLO_ACTION_AUTH_REGISTER, authService.getClass().getMethod(AuthService.METHOD_ACTION_AUTH_REGISTER, RegisterUserRequestDTO.class));
		getMapaMetodos().put(Constant.PROTOCOLO_ACTION_AUTH_VALIDATE_USERNAME, authService.getClass().getMethod(AuthService.METHOD_ACTION_AUTH_VALIDATE_USERNAME, ValidateUsernameDTO.class));
		
	}

	@PostMapping("/auth")
	public ProtocoloDTO in(@RequestBody ProtocoloDTO request) throws Exception {
		
		return super.in(request);

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
