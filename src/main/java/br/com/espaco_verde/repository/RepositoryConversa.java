package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Conversa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryConversa extends JpaRepository<Conversa, Integer> {

    Conversa findByTelefone(String telefone);
    boolean existsByTelefone(String telefone);

}
