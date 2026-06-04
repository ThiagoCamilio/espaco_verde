package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.ProductCart;

import java.math.BigDecimal;

public record ProductCartDTO(int id, ProductDTO productDTO ,int quantity, BigDecimal sellPrice) {

    public ProductCartDTO(ProductCart p){
        this(
                p.getId(),
                new ProductDTO(p.getProduct()),
                p.getQuantity(),
                p.getSellPrice()
        );
    }

}
