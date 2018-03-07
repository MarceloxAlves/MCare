package br.com.marcelo.mcare.model;

import android.support.annotation.NonNull;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

/**
 * Created by Marcelo on 28/02/2018.
 */
@Entity
public class Clinica implements Comparable<Clinica> {
    @Id
    public long id;
    private String nome;
    public ToMany<Medico> medicos;

    public Clinica() {
    }

    @Override
    public int compareTo(@NonNull Clinica clinica) {
        return getNome().compareToIgnoreCase(clinica.getNome());
    }

    public Clinica(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}