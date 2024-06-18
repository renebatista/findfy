package com.projetoa3.api.entities;

import java.util.Date;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "desaparecidos")
public class Desaparecido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "data_nascimento")
    private Date dataNascimento;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "status")
    private String status;

    @Column(name = "foto_blob")
    private byte[] imagem;

}
