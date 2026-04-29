package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Colaborador;
import org.springframework.data.repository.CrudRepository;

public interface RepositoryColaborador extends CrudRepository<Colaborador, Integer> {
}
