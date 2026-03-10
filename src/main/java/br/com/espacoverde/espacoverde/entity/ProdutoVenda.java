package br.com.espacoverde.espacoverde.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;

@Entity
@Table(name = "ProdutosVendas")
public class ProdutoVenda{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
    private double preçoVenda;
    private int quantidade;

    public ProdutoVenda(){

        id = 0;
        produto = new Produto();
        preçoVenda = 0;
        quantidade = 0;

    }

    public ProdutoVenda(int id, Produto produto, double preçoVenda, int quantidade){

        this.id = id;
        this.produto = produto;
        this.preçoVenda = preçoVenda;
        this.quantidade = quantidade; 

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Produto getProduto() {
        return produto;
    }
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    public double getPreçoVenda() {
        return preçoVenda;
    }
    public void setPreçoVenda(double preçoVenda) {
        this.preçoVenda = preçoVenda;
    }
    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

}