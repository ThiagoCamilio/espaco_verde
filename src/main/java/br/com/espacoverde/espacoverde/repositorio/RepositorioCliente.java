package br.com.espacoverde.espacoverde.repositorio;

import br.com.espacoverde.espacoverde.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RepositorioCliente extends CrudRepository<Cliente, Integer> {

    public List<Cliente> findAll();

    public Cliente findByNomeLike(String nome);

    public Cliente findById(int id);
}
