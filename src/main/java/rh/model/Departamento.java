package rh.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Agregação "tem-um" 1: Departamento tem um Colaborador gerente.
 * Agregação "tem-um" 2: Departamento tem uma lista de Colaboradores.
 */
public class Departamento {

    private Integer id;
    private String nome;
    private Colaborador gerente;               // tem-um Colaborador
    private List<Colaborador> colaboradores;   // tem-uma lista de Colaboradores

    public Departamento() { this.colaboradores = new ArrayList<>(); }

    public Departamento(Integer id, String nome, Colaborador gerente) {
        this.id = id;
        this.nome = nome;
        this.gerente = gerente;
        this.colaboradores = new ArrayList<>();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Colaborador getGerente() { return gerente; }
    public void setGerente(Colaborador gerente) { this.gerente = gerente; }

    public List<Colaborador> getColaboradores() { return colaboradores; }
    public void setColaboradores(List<Colaborador> colaboradores) { this.colaboradores = colaboradores; }

    public void adicionarColaborador(Colaborador c) { colaboradores.add(c); }

    public Double calcularFolha() {
        return colaboradores.stream().mapToDouble(Colaborador::calcularCustoTotal).sum();
    }
}
