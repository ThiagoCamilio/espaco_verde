package br.com.espaco_verde.repository;

import br.com.espaco_verde.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByChatIdOrderByTimestampAsc(Long chatId);

}
