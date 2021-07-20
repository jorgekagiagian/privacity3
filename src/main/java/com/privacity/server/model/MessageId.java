package com.privacity.server.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;


@Embeddable
public class MessageId implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "id_grupo")
    private Grupo grupo;
    
    public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public Long getIdMessage() {
		return idMessage;
	}

	public void setIdMessage(Long idMessage) {
		this.idMessage = idMessage;
	}

	private Long idMessage;

    public MessageId() {

    }

	public MessageId(Grupo grupo, Long idMessage) {
		super();
		this.grupo = grupo;
		this.idMessage = idMessage;
	}

	

}
