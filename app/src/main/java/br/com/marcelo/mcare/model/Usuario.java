package br.com.marcelo.mcare.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Marcelo on 21/02/2018.
 */
@Entity
public class Usuario {
    @Id
    private long id;
    private String email;
    private String senha;
    private String nome;

    public Usuario() {

    }

    public Usuario(String email, String senha, String nome) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
