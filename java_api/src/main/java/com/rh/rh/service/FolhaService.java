package com.rh.rh.service;

import com.rh.rh.model.Colaborador;
import com.rh.rh.model.Departamento;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class FolhaService {

    private final ColaboradorService colaboradorService;
    private final DepartamentoService departamentoService;

    public FolhaService(ColaboradorService colaboradorService,
                        DepartamentoService departamentoService) {
        this.colaboradorService = colaboradorService;
        this.departamentoService = departamentoService;
    }

    public Map<String, Object> folhaTotal() {
        double total = colaboradorService.listarTodos().stream()
                .mapToDouble(Colaborador::calcularCustoTotal).sum();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalColaboradores", colaboradorService.listarTodos().size());
        result.put("folhaTotalMensal", total);
        return result;
    }

    public Map<String, Object> folhaPorDepartamento(Integer idDept) {
        Departamento d = departamentoService.buscarPorId(idDept);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("departamento", d.getNome());
        result.put("totalMembros", d.getColaboradores().size());
        result.put("folhaMensal", d.calcularFolha());
        return result;
    }

    public Map<String, Object> custoPorColaborador(Integer idColab) {
        Colaborador c = colaboradorService.buscarPorId(idColab);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("colaborador", c.getNome());
        result.put("tipo", c.getTipo());
        result.put("salarioBase", c.getSalario());
        result.put("custoTotalMensal", c.calcularCustoTotal());
        return result;
    }

    public Map<String, Object> resumoPorTipo() {
        Map<String, Double> porTipo = new LinkedHashMap<>();
        for (Colaborador c : colaboradorService.listarTodos()) {
            porTipo.merge(c.getTipo(), c.calcularCustoTotal(), Double::sum);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("custoPorTipo", porTipo);
        return result;
    }
}
