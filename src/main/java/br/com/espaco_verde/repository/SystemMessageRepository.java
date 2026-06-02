package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.SystemMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemMessageRepository extends JpaRepository<SystemMessage, Integer> {
    Optional<SystemMessage> findByMessageKey(String messageKey);
}
