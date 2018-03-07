package br.com.marcelo.mcare.model;

import android.support.annotation.NonNull;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class TipoResultado implements Comparable<TipoResultado> {

    @Id
    private long id;
    private String descricao;
    public ToOne<TipoExame> tipoExameToOne;

    public TipoResultado() {
    }

    public TipoResultado(String descricao) {
        this.descricao = descricao;
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

    @Override
    public String toString() {
        return getDescricao();
    }

    @Override
    public int compareTo(@NonNull TipoResultado especialidade) {
        return  this.toString().compareToIgnoreCase(especialidade.toString());
    }
}
