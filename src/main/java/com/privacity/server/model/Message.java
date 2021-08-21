package com.privacity.server.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.privacity.server.security.Usuario;

@Entity
public class Message implements Serializable {
	
	private static final long serialVersionUID = 2473293848377594179L;

	@EmbeddedId
	private MessageId messageId; 
	
	@ManyToOne
	@JoinColumn(name = "idUser")
    private Usuario userCreation;
    private Date dateCreation;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="messageDetailId.message", cascade = CascadeType.PERSIST) //don't fetch immediately in order to use power of caching
    private Set<MessageDetail> messagesDetail;
    
    @OneToOne(fetch=FetchType.LAZY, mappedBy="mediaId.message", cascade = CascadeType.ALL) //don't fetch immediately in order to use power of caching

    private Media media;
    
	private boolean blackMessage;
    private boolean timeMessage;
    
    private boolean anonimo;
    private boolean systemMessage;
    private boolean secretKeyPersonal;
    private String text;
    
	@ManyToOne
    private Message messageParentResend;

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
	
	
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	public Set<MessageDetail> getMessagesDetail() {
		return messagesDetail;
	}

	public void setMessagesDetail(Set<MessageDetail> messagesDetail) {
		this.messagesDetail = messagesDetail;
	}




    
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
    
	public boolean isSystemMessage() {
		return systemMessage;
	}

	public void setSystemMessage(boolean systemMessage) {
		this.systemMessage = systemMessage;
	}

	public boolean isAnonimo() {
		return anonimo;
	}

	public void setAnonimo(boolean anonimo) {
		this.anonimo = anonimo;
	}

	public boolean isTimeMessage() {
		return timeMessage;
	}

	public void setTimeMessage(boolean timeMessage) {
		this.timeMessage = timeMessage;
	}

	public boolean isBlackMessage() {
		return blackMessage;
	}

	public void setBlackMessage(boolean blackMessage) {
		this.blackMessage = blackMessage;
	}

	public boolean isSecretKeyPersonal() {
		return secretKeyPersonal;
	}

	public void setSecretKeyPersonal(boolean secretKeyPersonal) {
		this.secretKeyPersonal = secretKeyPersonal;
	}

	public Message getMessageParentResend() {
		return messageParentResend;
	}

	public void setMessageParentResend(Message messageParentResend) {
		this.messageParentResend = messageParentResend;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

}