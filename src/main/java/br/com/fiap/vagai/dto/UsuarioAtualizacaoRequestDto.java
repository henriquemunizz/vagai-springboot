package br.com.fiap.vagai.dto;

import jakarta.validation.constraints.*;

public class UsuarioAtualizacaoRequestDto {
    @NotBlank @Size(max = 40)
    private String nome;
    @NotBlank @Size(min = 8, max = 255)
    private String senha;

    public UsuarioAtualizacaoRequestDto() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
