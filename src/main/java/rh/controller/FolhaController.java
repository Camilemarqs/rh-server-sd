package rh.controller;

import rh.service.FolhaService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Objeto distribuído 3 — FolhaController.
 * Expõe endpoints REST para cálculos de folha de pagamento.
 */
@RestController
@RequestMapping("/folha")
@CrossOrigin(origins = "*")
public class FolhaController {

    private final FolhaService service;

    public FolhaController(FolhaService service) {
        this.service = service;
    }

    /** GET /folha — folha total mensal de toda a empresa */
    @GetMapping
    public Map<String, Object> folhaTotal() {
        return service.folhaTotal();
    }

    /** GET /folha/departamento/{id} — folha de um departamento específico */
    @GetMapping("/departamento/{id}")
    public Map<String, Object> folhaDepartamento(@PathVariable Integer id) {
        return service.folhaPorDepartamento(id);
    }

    /** GET /folha/colaborador/{id} — custo individual de um colaborador */
    @GetMapping("/colaborador/{id}")
    public Map<String, Object> folhaColaborador(@PathVariable Integer id) {
        return service.custoPorColaborador(id);
    }

    /** GET /folha/resumo — resumo de custos agrupado por tipo de colaborador */
    @GetMapping("/resumo")
    public Map<String, Object> resumo() {
        return service.resumoPorTipo();
    }
}
