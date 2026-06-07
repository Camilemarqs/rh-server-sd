package rh.service;

import rh.model.Colaborador;
import rh.model.Departamento;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Terceiro objeto distribuído: FolhaService.
 * Responsável por cálculos de folha de pagamento da empresa,
 * por departamento e por colaborador individual.
 */
@Service
public class FolhaService {

    private final ColaboradorService colaboradorService;
    private final DepartamentoService departamentoService;

    public FolhaService(ColaboradorService colaboradorService,
                        DepartamentoService departamentoService) {
        this.colaboradorService = colaboradorService;
        this.departamentoService = departamentoService;
    }

    /** Custo total mensal de toda a empresa. */
    public Map<String, Object> folhaTotal() {
        double total = colaboradorService.listarTodos().stream()
                .mapToDouble(Colaborador::calcularCustoTotal).sum();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalColaboradores", colaboradorService.listarTodos().size());
        result.put("folhaTotalMensal", total);
        return result;
    }

    /** Custo total mensal de um departamento específico. */
    public Map<String, Object> folhaPorDepartamento(Integer idDept) {
        Departamento d = departamentoService.buscarPorId(idDept);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("departamento", d.getNome());
        result.put("totalMembros", d.getColaboradores().size());
        result.put("folhaMensal", d.calcularFolha());
        return result;
    }

    /** Custo individual de um colaborador. */
    public Map<String, Object> custoPorColaborador(Integer idColab) {
        Colaborador c = colaboradorService.buscarPorId(idColab);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("colaborador", c.getNome());
        result.put("tipo", c.getTipo());
        result.put("salarioBase", c.getSalario());
        result.put("custoTotalMensal", c.calcularCustoTotal());
        return result;
    }

    /** Resumo de custos de todos os colaboradores agrupados por tipo. */
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
