package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.PricingCategory;
import br.com.espaco_verde.entity.Product;
import br.com.espaco_verde.entity.TiposProdutos;

import java.math.BigDecimal;

public record ProductDTO(
        int id,
        String nome,
        TiposProdutos tipo,
        int stockQuantity,
        String dataDeEntrada,
        BigDecimal precoCusto,
        BigDecimal suggestedPrice,
        BigDecimal preco,
        String imagem,
        String descricao,
        boolean useSuggestedPrice,
        PricingCategory pricingCategory
) {

    public ProductDTO(Product p){

        this(
            p.getId(),
            p.getNome(),
            p.getTipo(),
            p.getStockQuantity(),
            p.getDataDeEntrada(),
            p.getPrecoCusto(),
            p.getSuggestedPrice(),
            p.getPreco(),
            p.getImagem(),
            p.getDescricao(),
            p.isUseSuggestedPrice(),
            p.getPricingCategory()
        );
    }

    public Product toEntity(){

        Product p = new Product();
        p.setId(this.id);
        p.setNome(this.nome);
        p.setTipo(this.tipo);
        p.setStockQuantity(this.stockQuantity);
        p.setDataDeEntrada(this.dataDeEntrada);
        p.setPrecoCusto(this.precoCusto);
        p.setSuggestedPrice(this.suggestedPrice);
        p.setPreco(this.preco);
        p.setImagem(this.imagem);
        p.setDescricao(this.descricao);
        p.setUseSuggestedPrice(this.useSuggestedPrice);
        p.setPricingCategory(this.pricingCategory);
        return p;
    }
}
