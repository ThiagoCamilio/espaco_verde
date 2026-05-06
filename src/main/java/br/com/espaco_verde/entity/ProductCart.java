package br.com.espaco_verde.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Entity(name = "ProodutosVendas")
@Table(name = "ProdutosVendas")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProductCart implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Getter @Setter
    private int id;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    @Getter @Setter
    private Product product;

    @Getter @Setter
    private double precoVenda;

    @Getter @Setter
    private int quantidade;

    public ProductCart(Product product, double precoVenda, int quantidade){
        this.product = product;
        this.precoVenda = precoVenda;
        this.quantidade = quantidade;
    }

}
