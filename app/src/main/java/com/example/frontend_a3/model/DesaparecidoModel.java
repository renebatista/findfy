package com.example.frontend_a3.model;

import android.graphics.Bitmap;
import java.util.Date;

public class DesaparecidoModel {
    private Integer id;
    private String nome;
    private String dataNascimento;
    private String descricao;
    private String status;
    private Bitmap imagemBitmap;

    // Construtor vazio (necessário para deserialização)
    public DesaparecidoModel() {
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bitmap getImagemBitmap() {
        return imagemBitmap;
    }

    public void setImagemBitmap(Bitmap imagemBitmap) {
        this.imagemBitmap = imagemBitmap;
    }
}
