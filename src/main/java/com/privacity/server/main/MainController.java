package com.privacity.server.main;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.privacity.common.config.Constant;
import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.MessageDetailDTO;
import com.privacity.common.dto.ProtocoloDTO;
import com.privacity.common.dto.request.GrupoAddUserRequestDTO;
import com.privacity.common.dto.request.GrupoSaveRequestDTO;
import com.privacity.common.dto.request.MessageSendRequestDTO;
import com.privacity.server.component.grupo.GrupoController;
import com.privacity.server.component.message.MessageController;


@RestController
@RequestMapping(path = "/secure")
public class MainController {


	@Autowired
	GrupoController grupoController;
	
	@Autowired
	MessageController messageController;
	

	Map<String,Method> mapaMetodos = new HashMap<String,Method>();
	Map<String,Object> mapaController = new HashMap<String,Object>();
//	Map<String,ProtocoloDTO> mapaProtocolo = new HashMap<String,ProtocoloDTO>();
	public MainController() throws Exception {
	}
				

	@PostMapping("/main")
	public ProtocoloDTO in(@RequestBody ProtocoloDTO request) throws Exception {
		
		mapaController.put("/grupo", grupoController);
		mapaController.put("/message", messageController);
		
		mapaMetodos.put("/grupo/save", grupoController.getClass().getMethod("save", GrupoSaveRequestDTO.class));
		mapaMetodos.put("/grupo/listar/misGrupos", grupoController.getClass().getMethod("listarMisGrupos"));
		mapaMetodos.put("/grupo/initGrupo", grupoController.getClass().getMethod("initGrupo", String.class));
		mapaMetodos.put("/grupo/addUser", grupoController.getClass().getMethod("addUser", GrupoAddUserRequestDTO.class));
		
		mapaMetodos.put("/message/send", messageController.getClass().getMethod("send", MessageSendRequestDTO.class));
		mapaMetodos.put("/message/emptyList", messageController.getClass().getMethod("emptyList", String.class));
		mapaMetodos.put(Constant.PROTOCOLO_ACTION_MESSAGE_GET_ALL_ID_MESSAGE_UNREAD, messageController.getClass().getMethod("getAllidMessageUnreadMessages"));		
		mapaMetodos.put(Constant.PROTOCOLO_ACTION_MESSAGE_GET_MESSAGE, messageController.getClass().getMethod("get", MessageDTO.class));
		mapaMetodos.put(Constant.PROTOCOLO_ACTION_MESSAGE_CHANGE_STATE, messageController.getClass().getMethod("changeState", MessageDetailDTO.class));
		
		mapaMetodos.put("/message/get/loadMessages", messageController.getClass().getMethod("loadMessages", MessageDTO.class));
	
//		mapaProtocolo.put("/grupo/save", new ProtocoloDTO());
//		mapaProtocolo.put("/grupo/listar/misGrupos", new ProtocoloDTO());
//		mapaProtocolo.put("/grupo/initGrupo", new ProtocoloDTO());
//		mapaProtocolo.put("/grupo/addUser", new ProtocoloDTO());
//		
//		mapaProtocolo.put("/message/send", new ProtocoloDTO());
//		mapaProtocolo.put("/message/emptyList", new ProtocoloDTO());
//		
//		mapaProtocolo.put("/message/changeState", new ProtocoloDTO(""));

		
		// tomo el dto a ejecutar
		Object objetoRetorno;
		if ( mapaMetodos.get(request.getAction()).getParameterTypes().length == 0) {
			 objetoRetorno = mapaMetodos.get(request.getAction()).invoke(mapaController.get(request.getComponent()));

		}else {
			Object dtoObject =  getDTOObject(request.getObjectDTO(),mapaMetodos.get(request.getAction()).getParameterTypes()[0]);
			 objetoRetorno = mapaMetodos.get(request.getAction()).invoke(mapaController.get(request.getComponent()), dtoObject);
			
		}
		
		// ejecuto el dto y obtengo la salida

		
//		EncriptadoService eee = new EncriptadoService();
//		fakeDTO.setName(eee.encriptarPGPOutIn(publicKeyPrivacity, "anda !!"));
//		Object objetoRetorno= fakeDTO;
		

		
		
		// armo la devolucion
		String retornoJson = new Gson().toJson(objetoRetorno);
		
		ProtocoloDTO p = new ProtocoloDTO();
		p.setComponent(request.getComponent());
		p.setAction(request.getAction());
		//p.setMensajeRespuesta();
		p.setObjectDTO(retornoJson);
		p.setPeticionId(request.getPeticionId());

		
		return p;
		
//		RSA t  = new RSA();
//
//

		
//		{
//			byte[] enc = t.encryptFilePublic("xxxxxxxxxxxxxxxxxxxxxxx".getBytes(), publicKeyUsuario);
//			String encode = Base64.getEncoder().encodeToString(enc);
//			 System.out.println(encode);
//		    
//			 byte[] des = t.decryptFilePrivate(enc, privateKeyPrivacity);
//			 System.out.println(new String(des, StandardCharsets.UTF_8));
//		}	    
		//desencriptar la llave publica
		//TODO
		// desencriptar objectDTO
	    
//	    privacityIdServices.getAESDecrypt(request.getObjectDTO(), request.getKeyCodePrivacity());
//		byte[] des = t.decryptFilePrivate(request.getKeyCodePrivacity().getBytes(), privateKeyPrivacity);
		
//		privacityIdServices.transformarDesencriptar(request,privateKeyPrivacity);
		//transformar el dto json a objecto
		
//		GrupoSaveRequestDTO fakeDTO = new GrupoSaveRequestDTO();
//		fakeDTO.setName("nombre");
//		



//		
//		privacityIdServices.transformarDesencriptar(dtoObject,privateKeyPrivacity);
		//((GrupoSaveRequestDTO)dtoObject).setName("123");
		//TODO
		//Action salvar listar etc

//		
		
		//tomar dto
		

//		privacityIdServices.transformarEncriptar(objetoRetorno,publicKeyUsuario);
//		
//		String retornoJson = new Gson().toJson(objetoRetorno);
//		
//		ProtocoloDTO p = new ProtocoloDTO();
//		p.setComponent("/grupo");
//		p.setAction("/grupo/save");
//		p.setMensajeRespuesta("El grupo fue creado");
//		p.setObjectDTO(retornoJson);
//		p.setPeticionId(request.getPeticionId());
//		
//		privacityIdServices.transformarEncriptar(p,publicKeyUsuario);
//		
//		return p;

	}

	


	private Object getDTOObject(String objectDTO, Class clazz) {
		Gson gson = new Gson();
		return gson.fromJson(objectDTO, clazz);
	}
	


}
