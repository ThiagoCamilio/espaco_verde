package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity(name = "Carrinho")
@Table(name = "Carrinhos")
@NoArgsConstructor
public class Carrinho implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private int id;

    @OneToMany
    @JoinColumn(name = "carrinho_id")
    @Getter @Setter
    private List<ProductCart> produtosCarrinho;

    @Getter @Setter
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "cupom_desconto_id")
    @Getter @Setter
    private CupomDesconto cupomDesconto;

    public Carrinho(List<ProductCart> produtosCarrinho, CupomDesconto cupomDesconto){

        this.produtosCarrinho = produtosCarrinho;
        for (ProductCart produto : produtosCarrinho){
            this.preco = produto.getPrecoVenda().multiply(BigDecimal.valueOf(produto.getQuantidade()));
        }
        this.cupomDesconto = cupomDesconto;

    }

    public Carrinho(List<ProductCart> produtosCarrinho){

        this.produtosCarrinho = produtosCarrinho;
        for (ProductCart produto : produtosCarrinho){
            this.preco = produto.getPrecoVenda().multiply(BigDecimal.valueOf(produto.getQuantidade()));
        }

    }

}
