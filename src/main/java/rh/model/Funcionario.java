package rh.model;

public class Funcionario extends Colaborador implements Admissivel {

    private String cargo;
    private String dataAdmissao;

    public Funcionario() {}

    public Funcionario(Integer id, String nome, Double salario, String cargo, String dataAdmissao) {
        super(id, nome, salario);
        this.cargo = cargo;
        this.dataAdmissao = dataAdmissao;
    }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    @Override
    public String getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(String d) { this.dataAdmissao = d; }

    @Override public String getTipo() { return "Funcionario"; }

    @Override
    public Double calcularCustoTotal() { return salario * 1.68; }

    @Override
    public String admitir() {
        return String.format("Funcionário %s admitido como %s em %s.", nome, cargo, dataAdmissao);
    }
}
