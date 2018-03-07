package br.com.marcelo.mcare.model;

import android.support.annotation.NonNull;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

/**
 * Created by Marcelo on 27/02/2018.
 */
@Entity
public class Medico implements Comparable<Medico> {

    @Id
    private long id;
    private String nome;
    private String crm;
    private int avaliacao;
    private String contato;
    private int foto;
    public ToMany<Especialidade> especialidade;



    public Medico() {
    }

    public Medico(String nome, String crm) {
        this.nome = nome;
        this.crm = crm;
        this.foto = 0;
    }

    @Override
    public int compareTo(@NonNull Medico o) {
        return  getNome().compareToIgnoreCase(o.getNome());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}
