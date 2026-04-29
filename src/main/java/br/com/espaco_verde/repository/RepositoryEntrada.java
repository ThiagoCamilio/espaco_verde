package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryEntrada extends JpaRepository<Entrada, Integer> {
}
