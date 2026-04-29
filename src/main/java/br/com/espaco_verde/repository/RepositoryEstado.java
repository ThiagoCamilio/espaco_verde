package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryEstado extends JpaRepository<Estado, Integer> {
}
