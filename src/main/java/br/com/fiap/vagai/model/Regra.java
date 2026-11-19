package br.com.fiap.vagai.model;

public class Regra {
    public boolean aplicaEm(int dia, int minuto) {
        if (ativo != 1) return false;
        if (inicio < fim) return dia == diaSemana && minuto >= inicio && minuto < fim;
        int diaSeguinte = diaSemana == 7 ? 1 : diaSemana + 1;
        return (dia == diaSemana && minuto >= inicio) || (dia == diaSeguinte && minuto < fim);
    }

    private Long id;
    private Estacionamento estacionamento;
    private String descricao;
    private Integer diaSemana;
    private Integer inicio;
    private Integer fim;
    private String tipo;
    private Integer ativo;

    public Regra() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
