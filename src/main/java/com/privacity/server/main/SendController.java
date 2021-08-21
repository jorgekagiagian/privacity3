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
import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.ProtocoloDTO;
import com.privacity.common.dto.request.RequestEncryptDTO;
import com.privacity.server.component.common.ControllerBase;
import com.privacity.server.component.message.MessageValidationService;
import com.privacity.server.encrypt.CryptSessionRegistry;
import com.privacity.server.encrypt.PrivacityIdServices;
import com.privacity.server.security.UserDetailsImpl;


@RestController
@RequestMapping(path = "/secure")

public class SendController extends ControllerBase{

	@Value("${privacity.security.encrypt.ids}")
	private boolean encryptIds;
	private PrivacityIdServices privacityIdServices;
	private MessageValidationService messageValidationService;
	




		

	public SendController(PrivacityIdServices privacityIdServices, MessageValidationService messageValidationService) {
		super();
		this.privacityIdServices = privacityIdServices;
		this.messageValidationService = messageValidationService;
	}


	@PostMapping("/send")
	public ResponseEntity<String> inMessage(@RequestBody String request) throws Exception {
		RequestEncryptDTO requestDTO = new Gson().fromJson(request, RequestEncryptDTO.class);
		
		request = requestDTO.getRequest();
		
		Authentication auth = SecurityContextHolder
	            .getContext()
	            .getAuthentication();
		UserDetailsImpl u = (UserDetailsImpl) auth.getPrincipal();
	    
		SecretKeyPersonal c = CryptSessionRegistry.getInstance().getSessionIds(u.getUsername()).getSecretKeyPersonal();
		
		String requestDesencriptado = c.getAESDecrypt(request);
		
		ProtocoloDTO p = new Gson().fromJson(requestDesencriptado, ProtocoloDTO.class);
		
		ProtocoloDTO retornoFuncion = this.in(p);
		
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object getDTOObject(String objectDTO, Class clazz) {
		System.out.println("objectDTO:" + objectDTO + "clazz:" + clazz.getName());
		Gson gson = new Gson();
		return gson.fromJson(objectDTO, clazz);
	}

	public ProtocoloDTO in(@RequestBody ProtocoloDTO request) throws Exception {

		System.out.println("<<" + request.toString());

		ProtocoloDTO p = new ProtocoloDTO();

		// tomo el dto a ejecutar
		MessageDTO objetoRetorno=null;
		MessageDTO dtoObject=null;

		try {

			
				 dtoObject =  request.getMessageDTO();
				
				if(getEncryptIds()) {
					getPrivacityIdServices().transformarDesencriptarOut(dtoObject);
					
				}
				
				objetoRetorno = messageValidationService.send(dtoObject);
					
				
			



			//				if(getEncryptIds()) {
			//					if ( mapaMetodos.get(request.getAction()).getParameterTypes().length != 0) {
			//						 getPrivacityIdServices().transformarDesencriptarOut(dtoObject);
			//					}
			//				}

		} catch (Exception e) {
			e.printStackTrace();
			p.setCodigoRespuesta(e.getCause().getMessage());
		} 

	

			if(getEncryptIds()) {
				getPrivacityIdServices().transformarEncriptarOut(objetoRetorno);
			}

	//	if(getEncryptIds()) {
	//		objetoRetorno = getPrivacityIdServices().transformarDesencriptarOut(getMapaMetodos().get(request.getAction()).invoke(getMapaController().get(request.getComponent()), dtoObject));
	//	}else {
	//		objetoRetorno = getMapaMetodos().get(request.getAction()).invoke(getMapaController().get(request.getComponent()), dtoObject);
	//	}	
	// armo la devolucion
			
    
	p.setComponent(request.getComponent());
	p.setAction(request.getAction());
	p.setMessageDTO(objetoRetorno);
	p.setPeticionId(request.getPeticionId());

	System.out.println(">>" + p.toString());
	return p;

}	





}
