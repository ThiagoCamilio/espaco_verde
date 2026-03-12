package br.com.espaco_verde.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name = "produtos")
@Table(name = "Produtos")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Produto {

    @Getter @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter @Setter
    private String nome;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private TiposProdutos tipo;

    @Getter @Setter
    private int quantidade;

    @Getter @Setter
    private String dataDeEntrada;

    @Getter @Setter
    private double precoCusto;

    @Getter @Setter
    private double preco;

    public Produto (String nome, TiposProdutos tipo, int quantidade, String dataDeEntrada, double precoCusto, double preco){
        this.nome = nome;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataDeEntrada = dataDeEntrada;
        this.precoCusto = precoCusto;
        this.preco = preco;
    }
}

