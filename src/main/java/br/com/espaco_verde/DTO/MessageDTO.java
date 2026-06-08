package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.Message;

import java.time.LocalDateTime;

public record MessageDTO(
        Long id,
        String content,
        String senderType,
        LocalDateTime timestamp
) {

    public MessageDTO(Message message) {
        this(
                message.getId(),
                message.getContent(),
                message.getSenderType().name(),
                message.getTimestamp()
        );
    }
}
