package com.privacity.common.dto;

import com.privacity.common.annotations.PrivacityId;

import lombok.Data;

@Data
public class MediaDTO {

//	@Id
//	@OneToOne
//    @JoinColumn(name = "id_grupo")
//	public Grupo grupo;
//	
	@PrivacityId
	public String idGrupo;
	@PrivacityId
	public String idMessage;

    public String data;
	public String mediaType;
	@Override
	public String toString() {
		return "MediaDTO [idGrupo=" + idGrupo + ", idMessage=" + idMessage + ", mediaType=" + mediaType + "]";
	}



    
}