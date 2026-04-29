package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Cidade;
import br.com.espaco_verde.entity.Estado;
import org.springframework.data.repository.CrudRepository;

public interface RepositoryCidade extends CrudRepository<Cidade, Integer> {
}
