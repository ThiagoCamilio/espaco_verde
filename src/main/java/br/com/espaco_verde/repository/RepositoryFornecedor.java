package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Fornecedor;
import org.springframework.data.repository.CrudRepository;

public interface RepositoryFornecedor extends CrudRepository<Fornecedor, Integer> {
}
