package br.com.fiap.vagai.dto;

import jakarta.validation.constraints.*;

public class AvaliacaoRequestDto {
    @NotNull @Positive
    private Long idEstacionamento;
    @NotNull @Min(1) @Max(5)
    private Integer nota;
    @Size(max = 500)
    private String comentario;

    public AvaliacaoRequestDto() {}

    public Long getIdEstacionamento() {
        return idEstacionamento;
    }

    public void setIdEstacionamento(Long idEstacionamento) {
        this.idEstacionamento = idEstacionamento;
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
}
