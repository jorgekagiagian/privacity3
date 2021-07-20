package com.privacity.common.dto;

import lombok.Data;


@Data
public class MessageDetailDTO{


	private String idMessageDetail;
	

	private String idMessage;
	

	private String idGrupo;

	private String userDestino;
	
	private String text;

    //@PrivacityPGPOut
    private String estado;
//
//	public void setIdMessage(Long idMessage) {
//		this.idMessage = idMessage+"";
//		
//	}
//
//	public void setIdMessage(String idMessage) {
//		this.idMessage = idMessage;
//		
//	}
//	
//	public void setIdMessageDetail(Long idMessageDetail) {
//		this.idMessageDetail = idMessageDetail+"";
//		
//	}
//
//	public void setIdMessageDetail(String idMessageDetail) {
//		this.idMessageDetail = idMessageDetail;
//		
//	}
	    

}