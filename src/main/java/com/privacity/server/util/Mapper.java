package com.privacity.server.util;

import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.privacity.common.dto.AESDTO;
import com.privacity.common.dto.EncryptKeysDTO;
import com.privacity.common.dto.GrupoDTO;
import com.privacity.common.dto.MediaDTO;
import com.privacity.common.dto.MessageDTO;
import com.privacity.common.dto.MessageDetailDTO;
import com.privacity.common.dto.UserForGrupoDTO;
import com.privacity.common.dto.UsuarioDTO;
import com.privacity.common.enumeration.MediaTypeEnum;
import com.privacity.server.component.common.ZipUtilService;
import com.privacity.server.component.common.repository.FacadeService;
import com.privacity.server.component.grupo.GrupoUtilService;
import com.privacity.server.component.media.MediaUtilService;
import com.privacity.server.component.message.MessageUtilService;
import com.privacity.server.component.messagedetail.MessageDetailUtil;
import com.privacity.server.exceptions.ProcessException;
import com.privacity.server.exceptions.ValidationException;
import com.privacity.server.model.AES;
import com.privacity.server.model.EncryptKeys;
import com.privacity.server.model.Grupo;
import com.privacity.server.model.Media;
import com.privacity.server.model.MediaId;
import com.privacity.server.model.Message;
import com.privacity.server.model.MessageDetail;
import com.privacity.server.model.MessageId;
import com.privacity.server.model.UserForGrupo;
import com.privacity.server.security.Usuario;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class Mapper {
	

	private FacadeService facadeService;
	

	public GrupoDTO doit(Grupo grupo) {
		
		GrupoDTO g = new GrupoDTO();
		g.setIdGrupo(grupo.getIdGrupo().toString());
		g.setName(grupo.getName());

		return g;
	}
	public MediaDTO doit(Media m) throws ProcessException {
		return doit(m, false);
	}
	
	public MediaDTO doit(Media m, boolean fillMediaData) throws ProcessException {
		if (m == null) return null;
		
		MediaDTO mediaDTO = new MediaDTO();

		if (fillMediaData && m.getData() != null) {
			mediaDTO.setData(facadeService.getZipUtilService().decompress(m.getData()));
		}
		
		mediaDTO.setIdGrupo(m.getMediaId().getMessage().getMessageId().getGrupo().getIdGrupo()+"");
		mediaDTO.setIdMessage(m.getMediaId().getMessage().getMessageId().getIdMessage()+"");
		mediaDTO.setMediaType(m.getMediaType().name());

		return mediaDTO;
	}

	public UsuarioDTO doitForGrupo(Grupo grupo, Usuario u) {
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setIdUsuario(u.getIdUser()+"");
		usuarioDTO.setNickname(facadeService.getUserForGrupoUtil().getNicknameForGrupo(grupo, u));
		
		return usuarioDTO;
	}
	
	public UsuarioDTO doit(Usuario u) {
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setIdUsuario(u.getIdUser()+"");
		usuarioDTO.setNickname(u.getNickname());
		
		return usuarioDTO;
	}
	
	public UserForGrupoDTO doit(UserForGrupo userForGrupo, Usuario logU) {
		UserForGrupoDTO ufgDTO = new UserForGrupoDTO();
		ufgDTO.setIdGrupo(userForGrupo.getUserForGrupoId().getGrupo().getIdGrupo()+"");
		ufgDTO.setUsuario(doitForGrupo(userForGrupo.getUserForGrupoId().getGrupo(),userForGrupo.getUserForGrupoId().getUser()));
		ufgDTO.setRole(userForGrupo.getRole().name());
		
		if (userForGrupo.getUserForGrupoId().getUser().getIdUser() == logU.getIdUser()) {
			ufgDTO.setAesDTO( doit(userForGrupo.getAes()));
		}
		
		return ufgDTO;
	}

	public Message doit(MessageDTO dto, Usuario usuarioCreacion) throws ValidationException, ProcessException {
		return doit(dto, usuarioCreacion, false);
	}
	public Message doit(MessageDTO dto, Usuario usuarioCreacion, boolean newId) throws ValidationException, ProcessException {
		Message m = new Message();
		m.setDateCreation(new Date());
		m.setUserCreation(usuarioCreacion);
		m.setText(dto.getText());
		m.setBlackMessage(dto.isBlackMessage());
		m.setAnonimo(dto.isAnonimo());
		m.setTimeMessage(dto.isTimeMessage());
		m.setSystemMessage(dto.isSystemMessage());
		m.setSecretKeyPersonal(dto.isSecretKeyPersonal());
		
		Grupo g = facadeService.getGrupoUtilService().getGrupoById(dto.getIdGrupo());
		
		MessageId idm = new MessageId();
		idm.setGrupo(g);
		
		if (dto.getIdMessage() == null || newId) {
			idm.setIdMessage(facadeService.getMessageUtilService().generateIdMessage());	
		}
		
		m.setMessageId(idm);
		
		m.setMedia(doit(dto.getMediaDTO(), m,true));
		m.setMessagesDetail( facadeService.getMessageDetailUtilService().generateMessagesDetail(m));
		return m;
	}

	public Media doit(MediaDTO dto, Message message) throws ValidationException, ProcessException {
		return doit(dto,message, false);
	}
	public Media doit(MediaDTO dto, Message message, boolean fillMediaData) throws ValidationException, ProcessException {
		if (dto == null) return null;
		Media media = new Media();
		

		if (fillMediaData && dto.getData() != null) {
			byte[] compress = facadeService.getZipUtilService().compress(dto.getData());
			//byte[] compress = zipUtilService.compress("123");
			media.setData(compress);
		}
		media.setMediaType(MediaTypeEnum.valueOf(dto.getMediaType()));

		media.setMediaId(new MediaId());
		media.getMediaId().setMessage(message);

		
		return media;
	}
	
	public MessageDTO doitWithOutMediaData(Message m, String mediaReplace) throws ProcessException {
		MessageDTO r = new MessageDTO();
		r.setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
		r.setText(m.getText());
		r.setUsuarioCreacion(doit(m.getUserCreation()));
		r.setIdMessage(m.getMessageId().getIdMessage()+"");
		r.setMessagesDetailDTO(new MessageDetailDTO[1]);
		r.setSecretKeyPersonal(m.isSecretKeyPersonal());
		Set<MessageDetail> details = m.getMessagesDetail();
		r.setBlackMessage(m.isBlackMessage());
		r.setAnonimo(m.isAnonimo());
		r.setTimeMessage(m.isTimeMessage());
		r.setSystemMessage(m.isSystemMessage());
		
		r.setMessagesDetailDTO(new MessageDetailDTO[details.size()]);
		
		Media media = m.getMedia();
		MediaDTO mediaDTO = doit(media);
		r.setMediaDTO(mediaDTO);
		
		

		int i=0;
		for ( MessageDetail d : details) {
			
			r.getMessagesDetailDTO()[i] = new MessageDetailDTO();
			
			r.getMessagesDetailDTO()[i].setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
			r.getMessagesDetailDTO()[i].setIdMessage(m.getMessageId().getIdMessage()+"");
			//			r.getMessagesDetailDTO()[i].setIdMessageDetail(d.getMessageDetailId().getIdMessageDetail()+"");
			
			
			r.getMessagesDetailDTO()[i].setUsuarioDestino( doit(d.getMessageDetailId().getUserDestino()));
			
			//response.getMessagesDetailDTO()[i].setUserDestino(d.getMessageDetailId().getUserDestino().getUsername());
			
			r.getMessagesDetailDTO()[i].setEstado(d.getState().toString());

			i++;
		}
		return r;
	}
	
	public MessageDTO doitWithOutTextAndMedia(Message m) throws ProcessException {
		MessageDTO r = new MessageDTO();
		r.setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
		
		r.setUsuarioCreacion(doit(m.getUserCreation()));
		r.setIdMessage(m.getMessageId().getIdMessage()+"");
		r.setMessagesDetailDTO(new MessageDetailDTO[1]);
		r.setSecretKeyPersonal(m.isSecretKeyPersonal());
		Set<MessageDetail> details = m.getMessagesDetail();
		r.setBlackMessage(m.isBlackMessage());
		r.setAnonimo(m.isAnonimo());
		r.setTimeMessage(m.isTimeMessage());
		r.setSystemMessage(m.isSystemMessage());
		
		r.setMessagesDetailDTO(new MessageDetailDTO[details.size()]);
		
		Media media = m.getMedia();
		MediaDTO mediaDTO = doit(media,false);
		r.setMediaDTO(mediaDTO);
		

		int i=0;
		for ( MessageDetail d : details) {
			
			r.getMessagesDetailDTO()[i] = new MessageDetailDTO();
			
			r.getMessagesDetailDTO()[i].setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
			r.getMessagesDetailDTO()[i].setIdMessage(m.getMessageId().getIdMessage()+"");
			//			r.getMessagesDetailDTO()[i].setIdMessageDetail(d.getMessageDetailId().getIdMessageDetail()+"");
			
			
			r.getMessagesDetailDTO()[i].setUsuarioDestino( doit(d.getMessageDetailId().getUserDestino()));
			
			//response.getMessagesDetailDTO()[i].setUserDestino(d.getMessageDetailId().getUserDestino().getUsername());
			
			r.getMessagesDetailDTO()[i].setEstado(d.getState().toString());

			i++;
		}
		return r;
	}
	
	public MessageDTO doit(Message m) throws ProcessException {
		MessageDTO r = new MessageDTO();
		r.setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
		r.setText(m.getText());
		r.setUsuarioCreacion(doitForGrupo(m.getMessageId().getGrupo(), m.getUserCreation()));
		r.setIdMessage(m.getMessageId().getIdMessage()+"");
		r.setMessagesDetailDTO(new MessageDetailDTO[1]);
		r.setSecretKeyPersonal(m.isSecretKeyPersonal());
		Set<MessageDetail> details = m.getMessagesDetail();
		r.setBlackMessage(m.isBlackMessage());
		r.setAnonimo(m.isAnonimo());
		r.setTimeMessage(m.isTimeMessage());
		r.setSystemMessage(m.isSystemMessage());
		
		r.setMessagesDetailDTO(new MessageDetailDTO[details.size()]);
		
		Media media = m.getMedia();
		MediaDTO mediaDTO = doit(media,true);
		r.setMediaDTO(mediaDTO);
		

		int i=0;
		for ( MessageDetail d : details) {
			
			r.getMessagesDetailDTO()[i] = new MessageDetailDTO();
			
			r.getMessagesDetailDTO()[i].setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
			r.getMessagesDetailDTO()[i].setIdMessage(m.getMessageId().getIdMessage()+"");
			//			r.getMessagesDetailDTO()[i].setIdMessageDetail(d.getMessageDetailId().getIdMessageDetail()+"");
			
			
			r.getMessagesDetailDTO()[i].setUsuarioDestino( doitForGrupo(m.getMessageId().getGrupo(),d.getMessageDetailId().getUserDestino()));
			
			//response.getMessagesDetailDTO()[i].setUserDestino(d.getMessageDetailId().getUserDestino().getUsername());
			
			r.getMessagesDetailDTO()[i].setEstado(d.getState().toString());

			i++;
		}
		return r;
	}

	

	public MessageDTO doitAnonimoToSend(Message m, Long idUser) throws ProcessException {
		MessageDTO r = new MessageDTO();
		r.setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
		r.setText(m.getText());
		//r.setUserDTOCreation();
		r.setIdMessage(m.getMessageId().getIdMessage()+"");
		r.setMessagesDetailDTO(new MessageDetailDTO[1]);
		Set<MessageDetail> details = m.getMessagesDetail();
		r.setBlackMessage(m.isBlackMessage());
		r.setAnonimo(m.isAnonimo());
		r.setTimeMessage(m.isTimeMessage());
		r.setSystemMessage(m.isSystemMessage());
		r.setSecretKeyPersonal(m.isSecretKeyPersonal());
		
		r.setMessagesDetailDTO(new MessageDetailDTO[1]);
		
		Media media = m.getMedia();
		MediaDTO mediaDTO = doit(media,true);
		r.setMediaDTO(mediaDTO);
		

		int i=0;
		for ( MessageDetail d : details) {
			
			if (d.getMessageDetailId().getUserDestino().getIdUser().equals(idUser)){
				r.getMessagesDetailDTO()[i] = new MessageDetailDTO();
				
				r.getMessagesDetailDTO()[i].setIdGrupo(m.getMessageId().getGrupo().getIdGrupo()+"");
				r.getMessagesDetailDTO()[i].setIdMessage(m.getMessageId().getIdMessage()+"");
				//r.getMessagesDetailDTO()[i].setIdMessageDetail(d.getMessageDetailId().getIdMessageDetail()+"");
				

				r.getMessagesDetailDTO()[i].setUsuarioDestino( doit(d.getMessageDetailId().getUserDestino()));
				
				//response.getMessagesDetailDTO()[i].setUserDestino(d.getMessageDetailId().getUserDestino().getUsername());
				
				r.getMessagesDetailDTO()[i].setEstado(d.getState().toString());
			}
			
		}
		return r;
	}
	public EncryptKeysDTO doit(EncryptKeys e) {
		EncryptKeysDTO r = new EncryptKeysDTO();
		r.setPrivateKey(e.getPrivateKey());
		r.setPublicKey(e.getPublicKey());
		return r;
	}
	
	public EncryptKeysDTO doitPublicKeyNoEncrypt(EncryptKeys e) {
		EncryptKeysDTO r = new EncryptKeysDTO();
		
		r.setPublicKeyNoEncrypt(e.getPublicKeyNoEncrypt());
		return r;
	}
	
	public EncryptKeys doit(EncryptKeysDTO e) {
		EncryptKeys r = new EncryptKeys();
		r.setPrivateKey(e.getPrivateKey());
		r.setPublicKey(e.getPublicKey());
		r.setPublicKeyNoEncrypt(e.getPublicKeyNoEncrypt());
		return r;
	}
	public AES doit(AESDTO e) {
		AES r = new AES();
		r.setSecretKeyAES(e.getSecretKeyAES());
		r.setSaltAES(e.getSaltAES());

		return r;
	}

	public AESDTO doit(AES e) {
		AESDTO r = new AESDTO();
		r.setSecretKeyAES(e.getSecretKeyAES());
		r.setSaltAES(e.getSaltAES());
		
		return r;
	}
	public MessageDetailDTO doit(MessageDetail md) {
		MessageDetailDTO r = new MessageDetailDTO();
		r.setEstado(md.getState().name());
		r.setIdGrupo(md.getMessageDetailId().getMessage().getMessageId().getGrupo().getIdGrupo()+"");
		r.setIdMessage(md.getMessageDetailId().getMessage().getMessageId().getIdMessage()+"");
		r.setUsuarioDestino( doit(md.getMessageDetailId().getUserDestino()));
		return r;
	}
}
