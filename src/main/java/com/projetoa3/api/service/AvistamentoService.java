package com.projetoa3.api.service;

import java.util.List;

import com.projetoa3.api.entities.Avistamento;

public interface AvistamentoService {
    void createAvistamento(Long desaparecidoId, String comentario);
    List<String> getAvistamentosByDesaparecidoId(Long desaparecidoId);
}
