package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.Produto;
import br.com.espaco_verde.entity.TiposProdutos;

public record RegisterProductDTO(
        String nome,
        TiposProdutos tipo,
        int quantidade,
        String dataDeEntrada,
        double precoCusto,
        double preco
){

    public Produto toEntity(){

        Produto p = new Produto();
        p.setNome(this.nome);
        p.setTipo(this.tipo);
        p.setQuantidade(this.quantidade);
        p.setDataDeEntrada(this.dataDeEntrada);
        p.setPrecoCusto(this.precoCusto);
        p.setPreco(this.preco);
        return p;

    }

}
