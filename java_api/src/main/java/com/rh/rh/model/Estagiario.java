package com.rh.rh.model;

public class Estagiario extends Colaborador {

    private String curso;
    private Integer cargaHorariaSemanal;

    public Estagiario() {}

    public Estagiario(Integer id, String nome, Double salario, String curso, Integer cargaHorariaSemanal) {
        super(id, nome, salario);
        this.curso = curso;
        this.cargaHorariaSemanal = cargaHorariaSemanal;
    }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    public Integer getCargaHorariaSemanal() { return cargaHorariaSemanal; }
    public void setCargaHorariaSemanal(Integer c) { this.cargaHorariaSemanal = c; }

    @Override public String getTipo() { return "Estagiario"; }

    @Override
    public Double calcularCustoTotal() { return salario + 200.0 + salario * 0.05; }
}
