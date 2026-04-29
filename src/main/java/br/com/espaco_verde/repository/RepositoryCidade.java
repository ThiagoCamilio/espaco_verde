package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryCidade extends JpaRepository<Cidade, Integer> {
}
