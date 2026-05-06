package br.com.espaco_verde.service;

import br.com.espaco_verde.entity.Carrinho;
import br.com.espaco_verde.entity.Product;
import br.com.espaco_verde.entity.ProductCart;
import br.com.espaco_verde.repository.RepositoryProduto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceCarrinho {

    private List<ProductCart> produtos = new ArrayList<ProductCart>();

    @Autowired
    private RepositoryProduto produtoRepository;

    public Carrinho addProduto(int id, HttpSession session){

        //é preciso adicionar logica para aumento de stockQuantity no caso de add um product que ja existe

        Product product = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Product não encontrado"));

        ProductCart productCart = new ProductCart(product, product.getPreco(), 1);

        produtos.add(productCart);

        Carrinho carrinho = new Carrinho(produtos);

        return carrinho;

    }

}