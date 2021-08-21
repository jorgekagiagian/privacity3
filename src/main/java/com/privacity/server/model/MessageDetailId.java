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
public class MessageDetailId  implements Serializable {

	private static final long serialVersionUID = -1657143766873819117L;

	@ManyToOne
	@JoinColumn(name = "idUser")
    private Usuario userDestino;
	
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








    
 }
