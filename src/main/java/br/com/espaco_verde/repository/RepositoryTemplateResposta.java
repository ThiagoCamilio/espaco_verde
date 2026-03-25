package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.TemplateResposta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryTemplateResposta extends CrudRepository<TemplateResposta, Integer> {

    TemplateResposta findById(String id);

}
