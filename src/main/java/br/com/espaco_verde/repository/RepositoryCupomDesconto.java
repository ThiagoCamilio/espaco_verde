package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.CupomDesconto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryCupomDesconto extends JpaRepository<CupomDesconto, Integer> {
}
