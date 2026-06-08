package com.rh.rh.model;

public class Efetivo extends Funcionario {

    private Double  bonusAnual;
    private Integer anosDeEmpresa;

    public Efetivo() {}

    public Efetivo(Integer id, String nome, Double salario, String cargo,
                   String dataAdmissao, Double bonusAnual, Integer anosDeEmpresa) {
        super(id, nome, salario, cargo, dataAdmissao);
        this.bonusAnual = bonusAnual;
        this.anosDeEmpresa = anosDeEmpresa;
    }

    public Double getBonusAnual() { return bonusAnual; }
    public void setBonusAnual(Double b) { this.bonusAnual = b; }

    public Integer getAnosDeEmpresa() { return anosDeEmpresa; }
    public void setAnosDeEmpresa(Integer a) { this.anosDeEmpresa = a; }

    @Override public String getTipo() { return "Efetivo"; }

    @Override
    public Double calcularCustoTotal() { return salario * 1.68 + (bonusAnual / 12.0); }

    @Override
    public String admitir() {
        return String.format("Colaborador %s efetivado como %s em %s. Bônus: R$%.2f/ano.",
                nome, getCargo(), getDataAdmissao(), bonusAnual);
    }
}
