package br.com.marcelo.mcare.model;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Exame {
    @Id
    private long id;
    private String descricao;
    private String nota;
    private Date data;
    public ToOne<Consulta> consultaToOne;
    public ToOne<TipoExame> tipoExame;
    public ToOne<Usuario> usuarioToOne;
    public ToMany<Resultado> resultados;

    public Exame() {

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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
