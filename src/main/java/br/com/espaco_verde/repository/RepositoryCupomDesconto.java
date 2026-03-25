package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.CupomDesconto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryCupomDesconto extends CrudRepository<CupomDesconto, Integer> {
}
