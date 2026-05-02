package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Entrada;
import br.com.espaco_verde.entity.ItemEntrada;
<<<<<<< Updated upstream
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryItemEntrada extends JpaRepository<ItemEntrada, Integer> {
=======
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RepositoryItemEntrada extends CrudRepository<ItemEntrada, Integer> {

  @Query("SELECT e FROM ItemEntrada e WHERE e.entrada.id = ?1")
  List<ItemEntrada> findByEntrada(long id);

>>>>>>> Stashed changes
}
