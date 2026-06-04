package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.Cart;
import java.math.BigDecimal;
import java.util.List;

public record CartDTO (int id, List<ProductCartDTO> productCartDTOs, BigDecimal price){

    public CartDTO(Cart c){

        this(
                c.getId(),
                c.getProductCarts().stream().map(ProductCartDTO::new).toList(),
                c.getPrice()
        );
    }


}
