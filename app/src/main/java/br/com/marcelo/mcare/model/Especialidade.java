package br.com.marcelo.mcare.model;

import android.support.annotation.NonNull;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Especialidade implements Comparable<Especialidade> {

    @Id
    private long id;
    private String descricao;

    public Especialidade() {
    }

    public Especialidade(String descricao) {
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
    public int compareTo(@NonNull Especialidade especialidade) {
        return  this.toString().compareToIgnoreCase(especialidade.toString());
    }
}
