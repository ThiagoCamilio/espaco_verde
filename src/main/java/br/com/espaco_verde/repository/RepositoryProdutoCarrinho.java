package br.com.espaco_verde.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.espaco_verde.entity.ProductCart;

@Repository
public interface RepositoryProdutoCarrinho extends JpaRepository<ProductCart, Integer> {

    List<ProductCart> findAll();

    ProductCart findById(int id);
    
} 