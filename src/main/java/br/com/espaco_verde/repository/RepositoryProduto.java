package br.com.espaco_verde.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.espaco_verde.entity.Product;

@Repository
public interface RepositoryProduto extends JpaRepository<Product, Integer> {
    
    public List<Product> findAll();

    public Product findByNomeLike(String nome);

    @Query("SELECT p FROM produtos p WHERE p.active = true AND (p.stockQuantity - p.reservedQuantity) > 0")
    public List<Product> findActive();
}
