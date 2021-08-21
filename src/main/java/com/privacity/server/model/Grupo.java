package com.privacity.server.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Data;

@Entity
@Data
public class Grupo {
    @Id
    private Long idGrupo;
    private String name;
    /*

    @OneToOne(mappedBy = "grupo", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private AESGrupo aesGrupo;*/
}