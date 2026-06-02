package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryConversa extends JpaRepository<Chat, Integer> {

    Chat findByWhatsappNumber(String WhatsappNumber);
    boolean existsByWhatsappNumber(String whatsappNumber);

}
