package com.privacity.server.component.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.ProtocoloDTO;
import com.privacity.server.encrypt.PrivacityIdServices;

public abstract class ControllerBase {

	private Map<String,Method> mapaMetodos = new HashMap<String,Method>();
	private Map<String,Object> mapaController = new HashMap<String,Object>();

	protected Map<String, Method> getMapaMetodos() {
		return mapaMetodos;
	}

	protected Map<String, Object> getMapaController() {
		return mapaController;
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
		Object objetoRetorno=null;
		Object dtoObject=null;

		try {

			if ( getMapaMetodos().get(request.getAction()).getParameterTypes().length == 0) {
				
				objetoRetorno = getMapaMetodos().get(request.getAction()).invoke(getMapaController().get(request.getComponent()));

			}else {
				dtoObject =  getDTOObject(request.getObjectDTO(),getMapaMetodos().get(request.getAction()).getParameterTypes()[0]);
				
				if(getEncryptIds()) {
					getPrivacityIdServices().transformarDesencriptarOut(dtoObject);
					objetoRetorno =getMapaMetodos().get(request.getAction()).invoke(getMapaController().get(request.getComponent()),  dtoObject);
				}else {
					objetoRetorno = getMapaMetodos().get(request.getAction()).invoke(getMapaController().get(request.getComponent()), dtoObject);
				}	
				
			}



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
	
	if (objetoRetorno instanceof MessageDTO ) {
		p.setMessageDTO((MessageDTO)objetoRetorno);
	}else {
		String retornoJson = new Gson().toJson(objetoRetorno);
		System.out.println("OBJETO RETORNO >>" + retornoJson);

		p.setObjectDTO(retornoJson);	
	}
	
	p.setPeticionId(request.getPeticionId());
	System.out.println(">>" + p.toString());
	return p;

}	

public abstract boolean getEncryptIds();

public abstract PrivacityIdServices getPrivacityIdServices();

public static void main(String...strings ) {

	//String a = new Gson().toJson("wCddwFHXqIgLzTB/6ntQLlfIKO3t92onltnujNtNads=");
	//System.out.println(a);
	Gson gson = new GsonBuilder()
	        .setLenient()
	        .create();
	String b =gson.fromJson("wCddwFHXqIgLzTB/6ntQLlfIKO3t92onltnujNtNads=", String.class);
	System.out.println(b);
}
}

