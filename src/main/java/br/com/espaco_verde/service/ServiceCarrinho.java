package br.com.espaco_verde.service;

import br.com.espaco_verde.entity.Carrinho;
import br.com.espaco_verde.entity.Produto;
import br.com.espaco_verde.entity.ProdutoCarrinho;
import br.com.espaco_verde.repository.RepositoryProduto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceCarrinho {

    private List<ProdutoCarrinho> produtos = new ArrayList<ProdutoCarrinho>();

    @Autowired
    private RepositoryProduto produtoRepository;

    public Carrinho addProduto(int id, HttpSession session){

        //é preciso adicionar logica para aumento de quantidade no caso de add um produto que ja existe

        Produto produto = produtoRepository.findById(id);

        ProdutoCarrinho produtoCarrinho = new ProdutoCarrinho(produto, produto.getPreco(), 1);

        produtos.add(produtoCarrinho);

        Carrinho carrinho = new Carrinho(produtos);

        return carrinho;

    }

}