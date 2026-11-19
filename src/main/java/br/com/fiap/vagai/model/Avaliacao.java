package br.com.fiap.vagai.model;

import java.time.LocalDateTime;

public class Avaliacao {
    private Long id;
    private Usuario usuario;
    private Estacionamento estacionamento;
    private Integer nota;
    private String comentario;
    private LocalDateTime data;

    public Avaliacao() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getIdUsuario() {
        return usuario == null ? null : usuario.getId();
    }

    public void setIdUsuario(Long id) {
        usuario = id == null ? null : new Usuario(id);
    }

    public Estacionamento getEstacionamento() {
        return estacionamento;
    }

    public void setEstacionamento(Estacionamento estacionamento) {
        this.estacionamento = estacionamento;
    }

    public Long getIdEstacionamento() {
        return estacionamento == null ? null : estacionamento.getId();
    }

    public void setIdEstacionamento(Long id) {
        estacionamento = id == null ? null : new Estacionamento(id);
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
