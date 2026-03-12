package br.com.espaco_verde.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.espaco_verde.entity.ProdutoCarrinho;

@Repository
public interface RepositorioProdutoCarrinho extends CrudRepository<ProdutoCarrinho, Integer>{

    List<ProdutoCarrinho> findAll();

    ProdutoCarrinho findById(int id);
    
} 