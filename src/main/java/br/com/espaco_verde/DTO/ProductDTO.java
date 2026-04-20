package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.Produto;
import br.com.espaco_verde.entity.TiposProdutos;

public record ProductDTO(
        int id,
        String nome,
        TiposProdutos tipo,
        int quantidade,
        String dataDeEntrada,
        double precoCusto,
        double preco,
        String imagem,
        String descricao
) {

    public ProductDTO(Produto p){

        this(
            p.getId(),
            p.getNome(),
            p.getTipo(),
            p.getQuantidade(),
            p.getDataDeEntrada(),
            p.getPrecoCusto(),
            p.getPreco(),
            p.getImagem(),
            p.getDescricao()
        );

    }

    public Produto toEntity(){

        Produto p = new Produto();
        p.setId(this.id);
        p.setNome(this.nome);
        p.setTipo(this.tipo);
        p.setQuantidade(this.quantidade);
        p.setDataDeEntrada(this.dataDeEntrada);
        p.setPrecoCusto(this.precoCusto);
        p.setPreco(this.preco);
        p.setImagem(this.imagem);
        p.setDescricao(this.descricao);
        return p;

    }

}
