package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name = "produtos")
@Table(name = "Produtos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SQLDelete(sql = "UPDATE produtos SET excluido = true WHERE id= ?")
@SQLRestriction("excluido = false")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private TiposProdutos tipo;

    private Integer stockQuantity;

    private Integer reservedQuantity = 0;

    private BigDecimal precoCusto;

    private BigDecimal preco;

    private BigDecimal suggestedPrice;

    private String imagem;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "pricing_category_id")
    private PricingCategory pricingCategory;

    private boolean excluido = false;

    private boolean active = true;

    private boolean useSuggestedPrice = false;

    public int getAvailableQuantity(){
        return this.stockQuantity - this.reservedQuantity;
    }

    public void verifyStock(){
        int stock = this.stockQuantity != null ? this.stockQuantity: 0;
        int reserved = this.reservedQuantity != null ? this.reservedQuantity:0;

        if(stock == 0 || reserved >= stock){
            this.active = false;
        }
    }
}

