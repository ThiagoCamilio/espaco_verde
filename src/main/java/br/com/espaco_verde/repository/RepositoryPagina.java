package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.EstadoConversa;
import br.com.espaco_verde.entity.Pagina;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryPagina extends CrudRepository<Pagina, Integer> {

    public Pagina findById(String id);
}
