package com.privacity.server.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.privacity.common.enumeration.MessageState;
import com.privacity.server.security.Usuario;

import lombok.Data;

@Entity

public class MessageDetail implements Serializable{

	public MessageDetailId getMessageDetailId() {
		return messageDetailId;
	}
	public void setMessageDetailId(MessageDetailId messageDetailId) {
		this.messageDetailId = messageDetailId;
	}
	

	public MessageState getState() {
		return state;
	}
	public void setState(MessageState state) {
		this.state = state;
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 888492831940808873L;

	@EmbeddedId
	private MessageDetailId messageDetailId; 
	

    private MessageState state;
    
    private boolean logicDeleted;

	public boolean isLogicDeleted() {
		return logicDeleted;
	}
	public void setLogicDeleted(boolean logicDeleted) {
		this.logicDeleted = logicDeleted;
	}


}