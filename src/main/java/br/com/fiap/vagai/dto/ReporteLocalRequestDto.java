package br.com.fiap.vagai.dto;

import jakarta.validation.constraints.*;

public class ReporteLocalRequestDto {
    @NotNull @Pattern(regexp = "VAGA_ENCONTRADA|SEM_VAGA")
    private String tipo;
    @Size(max = 255)
    private String observacao;

    public ReporteLocalRequestDto() {}

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
