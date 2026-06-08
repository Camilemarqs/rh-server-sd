package com.rh.rh.service;

import com.rh.rh.exception.ResourceNotFoundException;
import com.rh.rh.messaging.EventoService;
import com.rh.rh.model.Colaborador;
import com.rh.rh.model.Departamento;
import com.rh.rh.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DepartamentoService {

    private final DepartamentoRepository repository;
    private final ColaboradorService colaboradorService;
    private final EventoService eventoService;

    public DepartamentoService(DepartamentoRepository repository,
                               ColaboradorService colaboradorService,
                               EventoService eventoService) {
        this.repository = repository;
        this.colaboradorService = colaboradorService;
        this.eventoService = eventoService;
    }

    public List<Departamento> listarTodos() {
        return repository.findAll();
    }

    public Departamento buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento id=" + id + " não encontrado."));
    }

    public Departamento criar(Departamento d) {
        if (repository.existsById(d.getId())) {
            throw new IllegalArgumentException("Departamento id=" + d.getId() + " já existe.");
        }
        Departamento salvo = repository.save(d);
        eventoService.publicar("DEPARTAMENTO_CRIADO", Map.of(
                "id", salvo.getId(),
                "nome", salvo.getNome()
        ));
        return salvo;
    }

    public Departamento adicionarColaborador(Integer idDept, Integer idColab) {
        Departamento d = buscarPorId(idDept);
        Colaborador c = colaboradorService.buscarPorId(idColab);
        d.adicionarColaborador(c);
        Departamento salvo = repository.save(d);
        eventoService.publicar("COLABORADOR_ADICIONADO_DEPARTAMENTO", Map.of(
                "idDepartamento", idDept,
                "nomeDepartamento", d.getNome(),
                "idColaborador", idColab,
                "nomeColaborador", c.getNome()
        ));
        return salvo;
    }

    public boolean remover(Integer id) {
        if (!repository.deleteById(id)) {
            return false;
        }
        eventoService.publicar("DEPARTAMENTO_REMOVIDO", Map.of("id", id));
        return true;
    }
}
