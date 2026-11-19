package br.com.fiap.vagai.dto;

import jakarta.validation.constraints.*;

public class RegraRequestDto {
    @NotNull @Positive
    private Long idEstacionamento;
    @NotBlank @Size(max = 255)
    private String descricao;
    @NotNull @Min(1) @Max(7)
    private Integer diaSemana;
    @NotNull @Min(0) @Max(1439)
    private Integer inicio;
    @NotNull @Min(0) @Max(1439)
    private Integer fim;
    @NotNull @Pattern(regexp = "PERMITIDO|PROIBIDO")
    private String tipo;
    @NotNull @Min(0) @Max(1)
    private Integer ativo;

    public RegraRequestDto() {}

    public Long getIdEstacionamento() {
        return idEstacionamento;
    }

    public void setIdEstacionamento(Long idEstacionamento) {
        this.idEstacionamento = idEstacionamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Integer getInicio() {
        return inicio;
    }

    public void setInicio(Integer inicio) {
        this.inicio = inicio;
    }

    public Integer getFim() {
        return fim;
    }

    public void setFim(Integer fim) {
        this.fim = fim;
    }

    public Integer getAtivo() {
        return ativo;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
