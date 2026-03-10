package br.com.espacoverde.espacoverde.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.espacoverde.espacoverde.entity.Venda;

@Repository
public interface RepositorioVenda extends CrudRepository<Venda, Integer>{

    List<Venda> findAll();
    
    Venda findById(int id);

}