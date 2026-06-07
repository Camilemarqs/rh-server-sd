package rh.controller;

import rh.model.Colaborador;
import rh.service.ColaboradorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Objeto distribuído 1 — ColaboradorController.
 * Expõe endpoints REST para gerenciamento de colaboradores.
 */
@RestController
@RequestMapping("/colaboradores")
@CrossOrigin(origins = "*")
public class ColaboradorController {

    private final ColaboradorService service;

    public ColaboradorController(ColaboradorService service) {
        this.service = service;
    }

    /** GET /colaboradores — lista todos os colaboradores */
    @GetMapping
    public List<Colaborador> listar() {
        return service.listarTodos();
    }

    /** GET /colaboradores/{id} — busca por id */
    @GetMapping("/{id}")
    public Colaborador buscar(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    /** POST /colaboradores — adiciona um colaborador (passagem por valor via JSON) */
    @PostMapping
    public ResponseEntity<Colaborador> adicionar(@RequestBody Colaborador colaborador) {
        Colaborador salvo = service.adicionar(colaborador);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    /** DELETE /colaboradores/{id} — remove um colaborador */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> remover(@PathVariable Integer id) {
        service.remover(id);
        return ResponseEntity.ok(Map.of("mensagem", "Colaborador id=" + id + " removido com sucesso."));
    }

    /** GET /colaboradores/{id}/custo — custo total mensal do colaborador */
    @GetMapping("/{id}/custo")
    public Map<String, Object> custo(@PathVariable Integer id) {
        Colaborador c = service.buscarPorId(id);
        return Map.of(
                "id", c.getId(),
                "nome", c.getNome(),
                "tipo", c.getTipo(),
                "custoTotalMensal", c.calcularCustoTotal()
        );
    }
}
