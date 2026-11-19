package br.com.fiap.vagai.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class EstacionamentoRequestDto {
    @NotBlank @Size(max = 100)
    private String nome;
    @Size(max = 255)
    private String descricao;
    @NotNull @Pattern(regexp = "GRATUITO|ROTATIVO|PRIVADO")
    private String tipo;
    @NotNull @Min(0) @Max(1)
    private Integer ativo;

    @NotBlank @Size(max = 150)
    private String rua;
    @NotBlank @Size(max = 100)
    private String bairro;
    @NotBlank @Size(max = 20)
    private String numero;
    @PositiveOrZero @Digits(integer = 8, fraction = 2)
    private BigDecimal primeiraHora;
    @PositiveOrZero @Digits(integer = 8, fraction = 2)
    private BigDecimal demaisHoras;

    public EstacionamentoRequestDto() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getAtivo() {
        return ativo;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public BigDecimal getPrimeiraHora() {
        return primeiraHora;
    }

    public void setPrimeiraHora(BigDecimal primeiraHora) {
        this.primeiraHora = primeiraHora;
    }

    public BigDecimal getDemaisHoras() {
        return demaisHoras;
    }

    public void setDemaisHoras(BigDecimal demaisHoras) {
        this.demaisHoras = demaisHoras;
    }

    @AssertTrue(message = "Privado exige tarifa inicial e demais horas; gratuito aceita somente valores zero.")
    @com.fasterxml.jackson.annotation.JsonIgnore
    public boolean isTarifasValidas() {
        if ("GRATUITO".equals(tipo)) {
            return zeroOuNulo(primeiraHora) && zeroOuNulo(demaisHoras);
        }
        return !"PRIVADO".equals(tipo) ||
                (primeiraHora != null && demaisHoras != null);
    }

    private boolean zeroOuNulo(BigDecimal valor) {
        return valor == null || valor.signum() == 0;
    }
}
