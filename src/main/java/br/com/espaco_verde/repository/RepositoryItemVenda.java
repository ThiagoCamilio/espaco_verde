package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.ItemVenda;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RepositoryItemVenda extends CrudRepository<ItemVenda, Integer> {

  @Query("SELECT e FROM ItemVenda e WHERE e.venda.id = ?1")
  List<ItemVenda> findBVenda (long id);

}
