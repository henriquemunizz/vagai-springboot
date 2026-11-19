package br.com.fiap.vagai.dto;

import jakarta.validation.constraints.*;

public class FavoritoRequestDto {
    @NotNull @Positive
    private Long idEstacionamento;

    public FavoritoRequestDto() {}

    public Long getIdEstacionamento() {
        return idEstacionamento;
    }

    public void setIdEstacionamento(Long idEstacionamento) {
        this.idEstacionamento = idEstacionamento;
    }
}
