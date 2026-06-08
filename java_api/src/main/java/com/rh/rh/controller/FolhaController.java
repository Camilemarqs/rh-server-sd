package com.rh.rh.controller;

import com.rh.rh.service.FolhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/folha")
@CrossOrigin(origins = "*")
public class FolhaController {

    private final FolhaService service;

    @Autowired
    public FolhaController(FolhaService service) {
        this.service = service;
    }

    @GetMapping
    public Map<String, Object> folhaTotal() {
        return service.folhaTotal();
    }

    @GetMapping("/departamento/{id}")
    public Map<String, Object> folhaDepartamento(@PathVariable Integer id) {
        return service.folhaPorDepartamento(id);
    }

    @GetMapping("/colaborador/{id}")
    public Map<String, Object> folhaColaborador(@PathVariable Integer id) {
        return service.custoPorColaborador(id);
    }

    @GetMapping("/resumo")
    public Map<String, Object> resumo() {
        return service.resumoPorTipo();
    }
}
