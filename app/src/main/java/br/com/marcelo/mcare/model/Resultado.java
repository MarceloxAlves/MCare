package br.com.marcelo.mcare.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Resultado {
    @Id
    private long id;
    private String descricao;
    public ToOne<Exame> exameToOne;
    public ToOne<TipoResultado> tipoResultadoToOne;
    private double valor;

    public Resultado() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }


}
