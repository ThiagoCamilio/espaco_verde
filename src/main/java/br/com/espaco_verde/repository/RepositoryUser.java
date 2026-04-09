package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryUser extends CrudRepository<User, Integer> {

    UserDetails findByLogin (String login);

}
