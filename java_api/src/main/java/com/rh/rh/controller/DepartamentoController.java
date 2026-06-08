package com.rh.rh.controller;

import com.rh.rh.model.Departamento;
import com.rh.rh.service.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/departamentos")
@CrossOrigin(origins = "*")
public class DepartamentoController {

    private final DepartamentoService service;

    @Autowired
    public DepartamentoController(DepartamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Departamento> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Departamento buscar(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Departamento> criar(@RequestBody Departamento departamento) {
        Departamento salvo = service.criar(departamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PostMapping("/{id}/colaboradores")
    public ResponseEntity<Departamento> adicionarColaborador(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> body) {
        Integer idColab = body.get("idColaborador");
        if (idColab == null) {
            throw new IllegalArgumentException("Campo 'idColaborador' é obrigatório.");
        }
        Departamento atualizado = service.adicionarColaborador(id, idColab);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> remover(@PathVariable Integer id) {
        if (!service.remover(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Departamento id=" + id + " não encontrado."));
        }
        return ResponseEntity.ok(Map.of("mensagem", "Departamento id=" + id + " removido."));
    }
}
