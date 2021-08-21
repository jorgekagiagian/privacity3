package com.privacity.common.dto;

import com.privacity.common.annotations.PrivacityId;

import lombok.Data;


@Data
public class MessageDetailDTO{

	@PrivacityId
	public String idMessage;
	@PrivacityId
	public String idGrupo;
	public UsuarioDTO usuarioDestino;
    public String estado;

    public String getIdMessageDetailToMap() {
    	String usuarioid=null;
    	if (usuarioDestino != null) {
    		usuarioid= usuarioDestino.getIdUsuario();
    	}
    	return idGrupo + "{-}" + idMessage + "{-}" + usuarioid;
    }
	    
    public String getIdMessageToMap() {
    	return idGrupo + "{-}" + idMessage;
    }
}