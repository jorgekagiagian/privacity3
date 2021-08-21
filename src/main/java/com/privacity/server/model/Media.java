package com.privacity.server.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.privacity.common.enumeration.MediaTypeEnum;

import lombok.Data;

@Entity
@Data
public class Media implements Serializable{


	private static final long serialVersionUID = -7043920171959892214L;

	@EmbeddedId
    private MediaId mediaId;

	   @Lob
	    @Column(columnDefinition="BYTEA")
    private byte[] data;
    
	@Enumerated(EnumType.ORDINAL)
	private MediaTypeEnum mediaType;
	
	@Override
	public String toString() {
		return "Media [mediaId=" ; // + mediaId + ", mediaType=" + mediaType + "]";
	}
	
//    @ManyToOne
//    @JoinColumn(name = "id_comment", insertable = false, updatable = false)
//    public Comment comment;


    
}