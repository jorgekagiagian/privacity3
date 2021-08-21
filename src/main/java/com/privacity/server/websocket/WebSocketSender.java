package com.privacity.server.websocket;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.ProtocoloDTO;
import com.privacity.common.enumeration.ExceptionReturnCode;

import com.privacity.server.component.common.repository.FacadeService;
import com.privacity.server.encrypt.CryptSessionRegistry;
import com.privacity.server.exceptions.PrivacityException;
import com.privacity.server.exceptions.ProcessException;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.main.SecretKeyPersonal;
import com.privacity.server.model.Grupo;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageDetail;
import com.privacity.server.model.UserForGrupo;
import com.privacity.server.security.Usuario;

@Service
public class WebSocketSender {

	@Value("${privacity.security.encrypt.ids}")
	private boolean encryptIds;

	private static final String WEBSOCKET_CHANNEL = "/topic/reply";
	
	private FacadeService facadeService;    

	public WebSocketSender(FacadeService facadeService) {
		super();
		this.facadeService = facadeService;
	}

	public void sender(Usuario usuario, ProtocoloDTO protocoloDTP) throws PrivacityException {
		sender(usuario.getUsername(),protocoloDTP);
	}

	public void sender(String username, ProtocoloDTO protocoloDTO) throws PrivacityException {

		if (encryptIds) {
			//username = privacityIdServices.getAES(username);
		}

		
		sentToUser(username, WEBSOCKET_CHANNEL , new Gson().toJson(protocoloDTO));
	}

	public MessageDTO buildSystemMessage(Grupo grupo, String text) {

		MessageDTO mensaje = new MessageDTO();
		mensaje.setIdGrupo(grupo.getIdGrupo()+"");
		mensaje.setBlackMessage(false);
		mensaje.setTimeMessage(false);
		mensaje.setAnonimo(false);
		mensaje.setSystemMessage(true);
		mensaje.setText(text);

		return mensaje;

	}

	
	public void sender(MessageDTO messageDTO, ProtocoloDTO p) throws PrivacityException {
		sender(messageDTO,p, messageDTO.getUsuarioCreacion().getIdUsuario());
	}
	
	public void sender(MessageDTO messageDTO, ProtocoloDTO p, Long idUsuario) throws PrivacityException {
		sender(messageDTO,p, facadeService.getPrivacityIdServices().getAES(idUsuario+""));
	}
	

	public void sender(MessageDTO messageDTO, ProtocoloDTO p, String idUsuario) throws PrivacityException {
	

			for ( int k = 0 ; k < messageDTO.getMessagesDetailDTO().length ; k++ ) {


				if (!messageDTO.getMessagesDetailDTO()[k].getUsuarioDestino().getIdUsuario().equals(idUsuario)){
					Usuario destino; 
					if (encryptIds) {
						destino = facadeService.getUserRepository().findById(  Long.parseLong(facadeService.getPrivacityIdServices().getAESDecrypt(messageDTO.getMessagesDetailDTO()[k].getUsuarioDestino().getIdUsuario()))).get();	
					}else {
						destino = facadeService.getUserRepository().findById(Long.parseLong(messageDTO.getMessagesDetailDTO()[k].getUsuarioDestino().getIdUsuario())).get();						
					}
					
					sentToUser(destino.getUsername(), WEBSOCKET_CHANNEL , new Gson().toJson(p));	
				}

				

			}
		
	}
	public void sender(Message m, String p) throws PrivacityException {
		

		for ( MessageDetail md : m.getMessagesDetail() ) {
			
			if (!md.getMessageDetailId().getUserDestino().getIdUser().equals(m.getUserCreation().getIdUser())){
 
				
				sentToUser(md.getMessageDetailId().getUserDestino().getUsername(), WEBSOCKET_CHANNEL , p);	
			}

			

		}
	
}

	private void sentToUser(String user, String urlDestino, String mensaje) throws PrivacityException {
		
		if (facadeService.getSocketSessionRegistry().getSessionIds(user).size() >0) {
			
				new Runnable() {

					@Override
					public void run() {
						SecretKeyPersonal c;
						try {
							c = CryptSessionRegistry.getInstance().getSessionIds(user).getSecretKeyPersonal();

						
						String retornoFuncionEncriptado = c.getAES(mensaje);

						
						facadeService.getSimpMessagingTemplate().convertAndSendToUser(user, urlDestino , retornoFuncionEncriptado);	
						} catch (ValidationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}}.run();
				
		}
		
	}
	 
	public ProtocoloDTO buildProtocoloDTO(String component, String action, Object dto) throws ProcessException {
		return buildProtocoloDTO(component, action, dto, null);
	}
	
	public ProtocoloDTO buildProtocoloDTO(String component, String action, MessageDTO messageDTO) throws ProcessException {
		return buildProtocoloDTO(component, action, null, messageDTO);
	}
	
	public ProtocoloDTO buildProtocoloDTO(String component, String action, Object dto, MessageDTO messageDTO) throws ProcessException {
		ProtocoloDTO p = new ProtocoloDTO();
		p.setComponent(component);
		p.setAction(action);
		
		if (encryptIds) {
			try {
				facadeService.getPrivacityIdServices().transformarEncriptarOut(dto);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ProcessException(ExceptionReturnCode.ENCRYPT_PROCESS);	
			} 
		}
		p.setObjectDTO(new Gson().toJson(dto));

		return p;
	}

	public ProtocoloDTO buildProtocoloDTO(String component, String action) {
		ProtocoloDTO p = new ProtocoloDTO();
		p.setComponent(component);
		p.setAction(action);

		return p;
	}
	/*
	 * @param U ignora ese usuario
	 */


	public void senderToGrupo(Usuario u, Grupo grupo, ProtocoloDTO p) throws PrivacityException {
		
		List<UserForGrupo> lista = facadeService.getUserForGrupoRepository().findByForGrupo(grupo.getIdGrupo());
		
		for ( int k = 0 ; k < lista.size() ; k++ ) {


			if (!lista.get(k).getUserForGrupoId().getUser().getIdUser().equals(u.getIdUser())){
				Usuario destino = lista.get(k).getUserForGrupoId().getUser(); 
				
				sentToUser(destino.getUsername(), WEBSOCKET_CHANNEL , new Gson().toJson(p));	
			}

			

		}
		
	}
}
