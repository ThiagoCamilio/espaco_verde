package br.com.espaco_verde.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Entity(name = "produtos")
@Table(name = "Produtos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Produto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private TiposProdutos tipo;

    private int quantidade;

    private String dataDeEntrada;

    private double precoCusto;

    private double preco;

    private String imagem;

    public Produto (String nome, TiposProdutos tipo, int quantidade, String dataDeEntrada, double precoCusto, double preco){
        this.nome = nome;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataDeEntrada = dataDeEntrada;
        this.precoCusto = precoCusto;
        this.preco = preco;
    }
}

