package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryColaborador extends JpaRepository<Colaborador, Integer> {
}
