package br.com.espaco_verde.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serial;
import java.io.Serializable;

@Entity(name = "produtos")
@Table(name = "Produtos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@SQLDelete(sql = "UPDATE produtos SET excluido = true WHERE id = ?")
@SQLRestriction("excluido = false")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private TiposProdutos tipo;

    private int stockQuantity;

    private int reservedQuantity;

    private String dataDeEntrada;

    private double precoCusto;

    private double preco;

    private String imagem;

    private String descricao;

    private boolean excluido = false;

    private boolean active = true;

    public Product(String nome, TiposProdutos tipo, int stockQuantity, String dataDeEntrada, double precoCusto, double preco, String descricao){
        this.nome = nome;
        this.tipo = tipo;
        this.stockQuantity = stockQuantity;
        this.dataDeEntrada = dataDeEntrada;
        this.precoCusto = precoCusto;
        this.preco = preco;
        this.descricao = descricao;
    }

    public int getAvaliableQuantity(){
        return this.stockQuantity - this.reservedQuantity;
    }
}

