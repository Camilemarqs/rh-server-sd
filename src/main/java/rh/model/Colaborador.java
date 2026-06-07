package rh.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Superclasse abstrata de todos os colaboradores.
 * A anotação JsonTypeInfo permite que o Jackson serialize/deserialize
 * polimorficamente com base no campo "tipo".
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = rh.model.Funcionario.class,  name = "Funcionario"),
    @JsonSubTypes.Type(value = rh.model.Estagiario.class,   name = "Estagiario"),
    @JsonSubTypes.Type(value = Autonomo.class,     name = "Autonomo"),
    @JsonSubTypes.Type(value = rh.model.Efetivo.class,      name = "Efetivo")
})
public abstract class Colaborador {

    protected Integer id;
    protected String  nome;
    protected Double  salario;

    public Colaborador() {}

    public Colaborador(Integer id, String nome, Double salario) {
        this.id = id;
        this.nome = nome;
        this.salario = salario;
    }

    public Integer getId()     { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome()       { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Double getSalario()        { return salario; }
    public void setSalario(Double s)  { this.salario = s; }

    public abstract String getTipo();
    public abstract Double calcularCustoTotal();
}
