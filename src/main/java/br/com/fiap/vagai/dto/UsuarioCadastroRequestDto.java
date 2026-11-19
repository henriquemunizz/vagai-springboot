package br.com.fiap.vagai.dto;

import jakarta.validation.constraints.*;

public class UsuarioCadastroRequestDto {
    @NotBlank @Size(max = 40)
    private String nome;
    @NotBlank @Size(min = 8, max = 255)
    private String senha;

    public UsuarioCadastroRequestDto() {}

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
