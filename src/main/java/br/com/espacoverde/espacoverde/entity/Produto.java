package br.com.espacoverde.espacoverde.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Produtos")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    @Enumerated(EnumType.STRING)
    private TiposProdutos tipo;
    private int quantidade;
    private String dataDeEntrada;
    private double precoDeCusto;
    private double precoDeVenda;

    public Produto(){
        nome = "";
        tipo = TiposProdutos.OUTRO;
        quantidade = 0;
        dataDeEntrada = "";
        precoDeCusto = 0;
        precoDeVenda = 0;
    }

    public Produto (String nome, TiposProdutos tipo, int quantidade, String dataDeEntrada, double precoDeCusto, double precoDeVenda){
        this.nome = nome;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataDeEntrada = dataDeEntrada;
        this.precoDeCusto = precoDeCusto;
        this.precoDeVenda = precoDeVenda;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TiposProdutos getTipo() {
        return tipo;
    }

    public void setTipo(TiposProdutos tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDataDeEntrada() {
        return dataDeEntrada;
    }

    public void setDataDeEntrada(String dataDeEntrada) {
        this.dataDeEntrada = dataDeEntrada;
    }

    public double getPrecoDeCusto() {
        return precoDeCusto;
    }

    public void setPrecoDeCusto(double precoDeCusto) {
        this.precoDeCusto = precoDeCusto;
    }

    public double getPrecoDeVenda() {
        return precoDeVenda;
    }

    public void setPrecoDeVenda(double precoDeVenda) {
        this.precoDeVenda = precoDeVenda;
    }
}
