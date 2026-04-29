package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.ItemEntrada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryItemEntrada extends JpaRepository<ItemEntrada, Integer> {
}
