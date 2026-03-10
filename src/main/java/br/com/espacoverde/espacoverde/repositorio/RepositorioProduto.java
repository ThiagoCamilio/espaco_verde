package br.com.espacoverde.espacoverde.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.espacoverde.espacoverde.entity.Produto;

@Repository
public interface RepositorioProduto extends CrudRepository<Produto, Integer>{
    
    public List<Produto> findAll();

    public Produto findById(int id);

    public Produto findByNomeLike(String nome);
}
