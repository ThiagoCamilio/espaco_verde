package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Pagina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryPagina extends JpaRepository<Pagina, Integer> {

    public Pagina findById(String id);
}
