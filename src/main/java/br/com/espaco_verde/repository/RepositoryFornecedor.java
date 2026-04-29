package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryFornecedor extends JpaRepository<Fornecedor, Integer> {
}
