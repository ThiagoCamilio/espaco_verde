package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Venda;
import org.springframework.data.repository.CrudRepository;

public interface RepositoryVenda extends CrudRepository<Venda, Integer> {
}
