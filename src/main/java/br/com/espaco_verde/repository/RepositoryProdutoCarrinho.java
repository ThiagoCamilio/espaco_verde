package br.com.espaco_verde.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.espaco_verde.entity.ProdutoCarrinho;

@Repository
public interface RepositoryProdutoCarrinho extends JpaRepository<ProdutoCarrinho, Integer> {

    List<ProdutoCarrinho> findAll();

    ProdutoCarrinho findById(int id);
    
} 