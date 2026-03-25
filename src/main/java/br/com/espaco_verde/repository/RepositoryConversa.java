package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Conversa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryConversa extends CrudRepository<Conversa, Integer> {

    Conversa findByTelefone(String telefone);
    boolean existsByTelefone(String telefone);

}
