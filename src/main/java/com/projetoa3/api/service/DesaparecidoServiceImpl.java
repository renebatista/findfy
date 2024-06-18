package com.projetoa3.api.service;

import com.projetoa3.api.entities.Desaparecido;
import com.projetoa3.api.repository.DesaparecidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DesaparecidoServiceImpl implements DesaparecidoService {

    @Autowired
    private DesaparecidoRepository desaparecidoRepository;

    @Override
    public Desaparecido createDesaparecido(Desaparecido desaparecido) {
        return desaparecidoRepository.save(desaparecido);
    }

    @Override
    public List<Desaparecido> getAllDesaparecidos() {
        return desaparecidoRepository.findAll();
    }

    @Override
    public Desaparecido getDesaparecidoById(int id) {
        Optional<Desaparecido> desaparecido = desaparecidoRepository.findById((long) id);
        return desaparecido.orElse(null);
    }

    @Override
    public Desaparecido updateDesaparecido(int id, Desaparecido desaparecido) {
        Optional<Desaparecido> existingDesaparecido = desaparecidoRepository.findById((long) id);
        if (existingDesaparecido.isPresent()) {
            Desaparecido updatedDesaparecido = existingDesaparecido.get();
            updatedDesaparecido.setNome(desaparecido.getNome());
            updatedDesaparecido.setDataNascimento(desaparecido.getDataNascimento());
            updatedDesaparecido.setDescricao(desaparecido.getDescricao());
            updatedDesaparecido.setStatus(desaparecido.getStatus());
            return desaparecidoRepository.save(updatedDesaparecido);
        }
        return null;
    }

    @Override
    public boolean deleteDesaparecido(int id) {
        if (desaparecidoRepository.existsById((long) id)) {
            desaparecidoRepository.deleteById((long) id);
            return true;
        }
        return false;
    }
}