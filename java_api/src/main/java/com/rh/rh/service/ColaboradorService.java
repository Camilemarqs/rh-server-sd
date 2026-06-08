package com.rh.rh.service;

import com.rh.rh.exception.ResourceNotFoundException;
import com.rh.rh.messaging.EventoService;
import com.rh.rh.model.Colaborador;
import com.rh.rh.repository.ColaboradorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ColaboradorService {

    private final ColaboradorRepository repository;
    private final EventoService eventoService;

    public ColaboradorService(ColaboradorRepository repository, EventoService eventoService) {
        this.repository = repository;
        this.eventoService = eventoService;
    }

    public List<Colaborador> listarTodos() {
        return repository.findAll();
    }

    public Colaborador buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colaborador id=" + id + " não encontrado."));
    }

    public Colaborador adicionar(Colaborador c) {
        if (repository.existsById(c.getId())) {
            throw new IllegalArgumentException("Colaborador id=" + c.getId() + " já existe.");
        }
        Colaborador salvo = repository.save(c);
        eventoService.publicar("COLABORADOR_CRIADO", Map.of(
                "id", salvo.getId(),
                "nome", salvo.getNome(),
                "tipo", salvo.getTipo()
        ));
        return salvo;
    }

    public void remover(Integer id) {
        Colaborador c = buscarPorId(id);
        repository.deleteById(id);
        eventoService.publicar("COLABORADOR_REMOVIDO", Map.of(
                "id", id,
                "nome", c.getNome(),
                "tipo", c.getTipo()
        ));
    }

    public Double calcularCustoTotal(Integer id) {
        return buscarPorId(id).calcularCustoTotal();
    }
}
