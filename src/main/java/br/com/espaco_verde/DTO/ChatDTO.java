package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.Chat;

import java.time.LocalDateTime;

public record ChatDTO(
        Long id,
        String customerName,
        String whatsappNumber,
        String chatState,
        Integer unreadCount,
        LocalDateTime lastInteractionAt
) {

    public ChatDTO(Chat chat){
        this(
            chat.getId(),
            chat.getUser().getName(),
            chat.getWhatsappNumber(),
            chat.getChatState().name(),
            chat.getUnreadCount(),
            chat.getLastUserInteraction()
        );
    }

}
