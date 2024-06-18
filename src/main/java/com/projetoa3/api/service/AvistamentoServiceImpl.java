package com.projetoa3.api.service;

import com.projetoa3.api.entities.Avistamento;
import com.projetoa3.api.repository.AvistamentoRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AvistamentoServiceImpl implements AvistamentoService {

    @Autowired
    private AvistamentoRepository avistamentoRepository;

    @Override
    @Transactional
    public void createAvistamento(Long desaparecidoId, String avistamento) {
        avistamentoRepository.insertAvistamento(desaparecidoId, avistamento);
    }

    @Override
    public List<String> getAvistamentosByDesaparecidoId(Long desaparecidoId) {
        return avistamentoRepository.findByDesaparecidoId(desaparecidoId);
    }
}
