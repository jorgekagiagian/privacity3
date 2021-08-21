package com.privacity.common.dto;

import com.privacity.common.annotations.PrivacityId;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO implements Cloneable{
	@PrivacityId
	public String idMessage;
	@PrivacityId
	public String idGrupo;
	
	public UsuarioDTO usuarioCreacion;
	
	public MessageDetailDTO[] MessagesDetailDTO;
	public String text;
	public MediaDTO MediaDTO;
	
    public boolean blackMessage;
    public boolean timeMessage;
    public boolean anonimo;
    public boolean systemMessage;
    public boolean secretKeyPersonal;
    
    @PrivacityId
    public String idMessageParentResend;
    
	@Override
	public MessageDTO clone() throws CloneNotSupportedException {
		return (MessageDTO)super.clone();
	}

    public String getIdMessageToMap() {
    	return idGrupo + "{-}" + idMessage;
    }

}