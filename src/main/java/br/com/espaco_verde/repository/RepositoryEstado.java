package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Estado;
import org.springframework.data.repository.CrudRepository;

public interface RepositoryEstado extends CrudRepository<Estado, Integer> {
}
