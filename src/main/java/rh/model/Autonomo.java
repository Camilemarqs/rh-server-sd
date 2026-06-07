package rh.model;

public class Autonomo extends rh.model.Colaborador {

    private String especialidade;
    private String cnpj;

    public Autonomo() {}

    public Autonomo(Integer id, String nome, Double salario, String especialidade, String cnpj) {
        super(id, nome, salario);
        this.especialidade = especialidade;
        this.cnpj = cnpj;
    }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String e) { this.especialidade = e; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    @Override public String getTipo() { return "Autonomo"; }

    @Override
    public Double calcularCustoTotal() { return salario * 1.05; }
}
