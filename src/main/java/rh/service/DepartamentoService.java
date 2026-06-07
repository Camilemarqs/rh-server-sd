package rh.service;

import rh.exception.ResourceNotFoundException;
import rh.model.Colaborador;
import rh.model.Departamento;
import rh.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoService {

    private final DepartamentoRepository repository;
    private final ColaboradorService colaboradorService;

    public DepartamentoService(DepartamentoRepository repository,
                               ColaboradorService colaboradorService) {
        this.repository = repository;
        this.colaboradorService = colaboradorService;
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
        return repository.save(d);
    }

    public Departamento adicionarColaborador(Integer idDept, Integer idColab) {
        Departamento d = buscarPorId(idDept);
        Colaborador c = colaboradorService.buscarPorId(idColab);
        d.adicionarColaborador(c);
        return repository.save(d);
    }

    public boolean remover(Integer id) {
        return repository.deleteById(id);
    }
}
