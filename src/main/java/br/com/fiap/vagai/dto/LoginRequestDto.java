package br.com.fiap.vagai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequestDto {
    @NotBlank @Size(max = 40)
    private String nome;
    @NotBlank @Size(max = 255)
    private String senha;

    public LoginRequestDto() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
