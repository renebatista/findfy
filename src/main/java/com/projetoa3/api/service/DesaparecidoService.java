package com.projetoa3.api.service;

import com.projetoa3.api.entities.Desaparecido;

import java.util.List;

public interface DesaparecidoService {
    Desaparecido createDesaparecido(Desaparecido desaparecido);
    List<Desaparecido> getAllDesaparecidos();
    Desaparecido getDesaparecidoById(int id);
    Desaparecido updateDesaparecido(int id, Desaparecido desaparecido);
    boolean deleteDesaparecido(int id);
}