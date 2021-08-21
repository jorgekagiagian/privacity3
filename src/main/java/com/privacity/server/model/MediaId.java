package com.privacity.server.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import com.privacity.server.security.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MediaId implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6642514734541868832L;



	@ManyToOne
    @JoinColumns({
        @JoinColumn(
            name = "idGrupo",
            referencedColumnName = "id_grupo"),
        @JoinColumn(
            name = "idMessage",
            referencedColumnName = "idMessage")
    })
    private Message message;



	@Override
	public String toString() {
		return "MediaId [message=" + message.getMessageId().getIdMessage() + "]"+
				"[grupo=" + message.getMessageId().getGrupo().getIdGrupo() + "]";
	}

}

