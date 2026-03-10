package br.com.espacoverde.espacoverde.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.espacoverde.espacoverde.entity.ProdutoVenda;

@Repository
public interface RepositorioProdutoVenda extends CrudRepository<ProdutoVenda, Integer>{

    List<ProdutoVenda> findAll();

    ProdutoVenda findById(int id);
    
} 