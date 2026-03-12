package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "Carrinho")
@Table(name = "Carrinhos")
@NoArgsConstructor
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private int id;

    @OneToMany
    @JoinColumn(name = "carrinho_id")
    @Getter @Setter
    private List<ProdutoCarrinho> produtosCarrinho;

    @Getter @Setter
    private double preco;

    @ManyToOne
    @JoinColumn(name = "cupom_desconto_id")
    @Getter @Setter
    private CupomDesconto cupomDesconto;

    public Carrinho(List<ProdutoCarrinho> produtosCarrinho, CupomDesconto cupomDesconto){

        this.produtosCarrinho = produtosCarrinho;
        for (ProdutoCarrinho produto : produtosCarrinho){
            this.preco = produto.getPrecoVenda() * produto.getQuantidade();
        }
        this.cupomDesconto = cupomDesconto;

    }

}
