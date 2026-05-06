package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.ItemEntrada;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositoryItemEntrada extends JpaRepository<ItemEntrada, Integer> {

  @Query("SELECT e FROM ItemEntrada e WHERE e.entrada.id = ?1")
  List<ItemEntrada> findByEntrada(long id);

}
