package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface RepositoryCliente extends CrudRepository<Cliente, Integer> {
}
