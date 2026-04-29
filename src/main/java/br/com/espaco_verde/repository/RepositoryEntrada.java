package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Entrada;
import org.springframework.data.repository.CrudRepository;

public interface RepositoryEntrada extends CrudRepository<Entrada, Integer> {
}
