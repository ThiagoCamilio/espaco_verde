package br.com.espaco_verde.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class ItemEntrada implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private double valor;
  private double valorCusto;
  private double quantidade;
  @ManyToOne
  private Entrada entrada;
  @ManyToOne
  private Product product;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public double getValor() {
    return valor;
  }

  public void setValor(double valor) {
    this.valor = valor;
  }

  public double getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(double quantidade) {
    this.quantidade = quantidade;
  }

  public Entrada getEntrada() {
    return entrada;
  }

  public void setEntrada(Entrada entrada) {
    this.entrada = entrada;
  }

  public Product getProduto() {
    return product;
  }

  public void setProduto(Product product) {
    this.product = product;
  }

  public BigDecimal getValorCusto() {
    return valorCusto;
  }

  public void setValorCusto(double valorCusto) {
    this.valorCusto = valorCusto;
  }
}
