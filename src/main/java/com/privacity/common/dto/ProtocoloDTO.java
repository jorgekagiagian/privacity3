package com.privacity.common.dto;

import lombok.Data;

@Data
public class ProtocoloDTO{

	public ProtocoloDTO() {
		super();
	}

	public ProtocoloDTO(String component, String action) {
		super();
		this.component = component;
		this.action = action;
	}

	//@PrivacityPGPIn
	//@PrivacityPGPOut
	private String component;
	
	//@PrivacityPGPIn
	//@PrivacityPGPOut
	private String action;
	

	private String objectDTO;
	
	//@PrivacityPGPIn
	//@PrivacityPGPOut
	private String peticionId;
	
	//@PrivacityPGPOut
	private String mensajeRespuesta;
	
	//@PrivacityPGPOut
	private String codigoRespuesta;




}
