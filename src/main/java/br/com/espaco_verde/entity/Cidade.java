package br.com.espaco_verde.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "cidade")
public class Cidade implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String nome;
  private String cep;

  @ManyToOne
  private Estado estado;

  public Cidade(Long id, String nome, String cep, Estado estado) {
    this.id = id;
    this.nome = nome;
    this.cep = cep;
    this.estado = estado;
  }

  public Cidade() {
    id = (long) 0;
    nome = "";
    cep = "";
    estado = new Estado();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public Estado getEstado() {
    return estado;
  }

  public void setEstado(Estado estado) {
    this.estado = estado;
  }
}
