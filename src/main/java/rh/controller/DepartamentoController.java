package rh.controller;

import rh.model.Departamento;
import rh.service.DepartamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Objeto distribuído 2 — DepartamentoController.
 * Expõe endpoints REST para gerenciamento de departamentos.
 */
@RestController
@RequestMapping("/departamentos")
@CrossOrigin(origins = "*")
public class DepartamentoController {

    private final DepartamentoService service;

    public DepartamentoController(DepartamentoService service) {
        this.service = service;
    }

    /** GET /departamentos — lista todos os departamentos */
    @GetMapping
    public List<Departamento> listar() {
        return service.listarTodos();
    }

    /** GET /departamentos/{id} — busca por id */
    @GetMapping("/{id}")
    public Departamento buscar(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    /** POST /departamentos — cria um novo departamento */
    @PostMapping
    public ResponseEntity<Departamento> criar(@RequestBody Departamento departamento) {
        Departamento salvo = service.criar(departamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    /** POST /departamentos/{id}/colaboradores — adiciona colaborador ao departamento */
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

    /** DELETE /departamentos/{id} — remove um departamento */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> remover(@PathVariable Integer id) {
        if (!service.remover(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Departamento id=" + id + " não encontrado."));
        }
        return ResponseEntity.ok(Map.of("mensagem", "Departamento id=" + id + " removido."));
    }
}
