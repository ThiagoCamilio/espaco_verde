package br.com.espaco_verde.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "estado")
public class Estado implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String nome;
  private String sigla;

  public Estado() {
    id = (long) 0.0;
    nome = "";
    sigla = "";
  }

  public Estado(Long id, String nome, String sigla) {
    this.id = id;
    this.nome = nome;
    this.sigla = sigla;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getSigla() {
    return sigla;
  }

  public void setSigla(String sigla) {
    this.sigla = sigla;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
