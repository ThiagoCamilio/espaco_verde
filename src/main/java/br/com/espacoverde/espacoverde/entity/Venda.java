package br.com.espacoverde.espacoverde.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Vendas")

public class Venda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double valor;
    private String data;
    @OneToOne
    @JoinColumn(name = "produto_venda_id", referencedColumnName = "id")
    private ProdutoVenda produtoVenda;
    private StatusVenda status;

    public Venda(){

        id = 0;
        valor = 0;
        data = "";
        produtoVenda = new ProdutoVenda();
        status = StatusVenda.EM_PREPARO;

    }

    public Venda (int id, double valor, String data, ProdutoVenda produtoVenda, StatusVenda status){

        this.id = id;
        this.valor = valor;
        this.data = data;
        this.produtoVenda = produtoVenda;
        this.status = status;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ProdutoVenda getProdutoVenda() {
        return produtoVenda;
    }

    public void setProdutoVenda(ProdutoVenda produtoVenda) {
        this.produtoVenda = produtoVenda;
    }

    public StatusVenda getStatus() {
        return status;
    }

    public void setStatus(StatusVenda status){
        this.status = status;
    }

}
