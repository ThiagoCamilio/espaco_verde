package br.com.espaco_verde.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.espaco_verde.entity.Produto;

@Repository
public interface RepositoryProduto extends CrudRepository<Produto, Integer>{
    
    public List<Produto> findAll();

    public Produto findById(int id);

    public Produto findByNomeLike(String nome);
}
