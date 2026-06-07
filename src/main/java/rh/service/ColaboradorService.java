package rh.service;

import rh.exception.ResourceNotFoundException;
import rh.model.Colaborador;
import rh.repository.ColaboradorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColaboradorService {

    private final ColaboradorRepository repository;

    public ColaboradorService(ColaboradorRepository repository) {
        this.repository = repository;
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
        return repository.save(c);
    }

    public void remover(Integer id) {
        if (!repository.deleteById(id)) {
            throw new ResourceNotFoundException("Colaborador id=" + id + " não encontrado.");
        }
    }

    public Double calcularCustoTotal(Integer id) {
        return buscarPorId(id).calcularCustoTotal();
    }
}
