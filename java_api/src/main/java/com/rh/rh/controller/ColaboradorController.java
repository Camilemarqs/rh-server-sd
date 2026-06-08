package com.rh.rh.controller;

import com.rh.rh.model.Colaborador;
import com.rh.rh.service.ColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/colaboradores")
@CrossOrigin(origins = "*")
public class ColaboradorController {

    private final ColaboradorService service;

    @Autowired
    public ColaboradorController(ColaboradorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Colaborador> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Colaborador buscar(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Colaborador> adicionar(@RequestBody Colaborador colaborador) {
        Colaborador salvo = service.adicionar(colaborador);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> remover(@PathVariable Integer id) {
        service.remover(id);
        return ResponseEntity.ok(Map.of("mensagem", "Colaborador id=" + id + " removido com sucesso."));
    }

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
