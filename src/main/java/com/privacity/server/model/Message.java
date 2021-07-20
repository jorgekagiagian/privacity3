package com.privacity.server.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.privacity.server.security.Usuario;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
public class Message implements Serializable {

	public MessageId getMessageId() {
		return messageId;
	}

	public void setMessageId(MessageId messageId) {
		this.messageId = messageId;
	}

	public Usuario getUserCreation() {
		return userCreation;
	}

	public void setUserCreation(Usuario userCreation) {
		this.userCreation = userCreation;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Set<MessageDetail> getMessagesDetail() {
		return messagesDetail;
	}

	public void setMessagesDetail(Set<MessageDetail> messagesDetail) {
		this.messagesDetail = messagesDetail;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2473293848377594179L;

	@EmbeddedId
	private MessageId messageId; 
	
	@ManyToOne
	@JoinColumn(name = "idUser")
    private Usuario userCreation;
    private Date dateCreation;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="messageDetailId.message") //don't fetch immediately in order to use power of caching

    private Set<MessageDetail> messagesDetail;

}