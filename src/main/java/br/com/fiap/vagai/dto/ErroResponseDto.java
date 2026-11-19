package br.com.fiap.vagai.dto;

public class ErroResponseDto {
    private int status;
    private String mensagem;

    public ErroResponseDto() {}

    public ErroResponseDto(int status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
