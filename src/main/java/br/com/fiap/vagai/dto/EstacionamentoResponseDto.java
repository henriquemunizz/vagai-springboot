package br.com.fiap.vagai.dto;

import java.math.BigDecimal;

public class EstacionamentoResponseDto {
    private Long id;
    private Long idUsuarioCriador;
    private String nome;
    private String descricao;
    private String tipo;
    private Integer ativo;

    private String rua;
    private String bairro;
    private String numero;
    private BigDecimal primeiraHora;
    private BigDecimal demaisHoras;

    public EstacionamentoResponseDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuarioCriador() {
        return idUsuarioCriador;
    }

    public void setIdUsuarioCriador(Long idUsuarioCriador) {
        this.idUsuarioCriador = idUsuarioCriador;
    }

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
}
