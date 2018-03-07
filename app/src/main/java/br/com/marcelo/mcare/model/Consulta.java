package br.com.marcelo.mcare.model;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Consulta {

    @Id
    private long id;
    public ToOne<Usuario> usuario;
    public ToOne<Medico>  medico;
    public ToOne<Clinica> clinica;
    public ToOne<Especialidade> especialidadeToOne;
    private int status;
    private Date data;
    private String horario; // timer n aceito


    public Consulta(ToOne<Usuario> usuario, ToOne<Medico> medico, Date data, String horario, ToOne<Clinica> clinica) {
        this.usuario = usuario;
        this.medico = medico;
        this.status = StatusConsulta.AGENDADA;
        this.data = data;
        this.horario = horario;
        this.clinica = clinica;

    }

    public Consulta() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
