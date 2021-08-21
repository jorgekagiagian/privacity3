package com.privacity.common.dto;

import lombok.Data;

@Data
public class ProtocoloDTO {

	public ProtocoloDTO() {
		super();
	}

	public ProtocoloDTO(String component, String action) {
		super();
		this.component = component;
		this.action = action;
	}

    private String component;
    private String action;
    private GrupoDTO grupoDTO;
    private MessageDTO messageDTO;
    private String objectDTO; 
    private String peticionId;
    private String mensajeRespuesta;
    private String codigoRespuesta;
}
