package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.Product;
import br.com.espaco_verde.entity.TiposProdutos;

import java.math.BigDecimal;

public record RegisterProductDTO(
        String nome,
        TiposProdutos tipo,
        int quantidade,
        String dataDeEntrada,
        BigDecimal precoCusto,
        double preco,
        String descricao
){

    public Product toEntity(){

        Product p = new Product();
        p.setNome(this.nome);
        p.setTipo(this.tipo);
        p.setStockQuantity(this.quantidade);
        p.setDataDeEntrada(this.dataDeEntrada);
        p.setPrecoCusto(this.precoCusto);
        p.setPreco(this.preco);
        p.setDescricao(this.descricao);
        return p;

    }

}
