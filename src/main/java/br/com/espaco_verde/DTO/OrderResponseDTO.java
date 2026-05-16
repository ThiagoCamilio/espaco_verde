package br.com.espaco_verde.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(Integer id, String orderStatus, java.math.BigDecimal totalPrice, LocalDateTime createdAt, List<OrderItemResponseDTO> items, String deliveryMethod, String deliveryAdress, OrderUserDTO customer) {
}
