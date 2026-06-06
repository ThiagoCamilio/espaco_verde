package br.com.espaco_verde.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.math.BigDecimal;

@Entity(name = "ProductCart")
@Table(name = "ProductCarts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductCart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private BigDecimal sellPrice;

    private int quantity;

    public ProductCart(Product product, BigDecimal sellPrice, int quantity){
        this.product = product;
        this.sellPrice = sellPrice;
        this.quantity = quantity;
    }

    public BigDecimal getTotal(){
        return sellPrice.multiply(new BigDecimal(quantity));
    }

}
