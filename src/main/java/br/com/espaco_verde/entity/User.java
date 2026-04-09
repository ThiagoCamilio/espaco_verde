package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "usuarios")
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String nome;

  private String login;

  private String senha;

  private String funcao;

}
