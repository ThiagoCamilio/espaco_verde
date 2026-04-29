package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryCliente extends JpaRepository<Cliente, Integer> {
}
