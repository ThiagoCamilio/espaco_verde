package br.com.espacoverde.espacoverde.entity;

public class Cliente {

    private String nome;
    private String contato;
    private String endereco;

    public Cliente() {

        nome = "";
        contato = "";
        endereco = "";

    }

    public Cliente(String nome, String contato, String endereco) {
        this.nome = nome;
        this.contato = contato;
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
