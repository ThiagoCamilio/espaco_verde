package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.DeliveryMethod;

import java.util.List;

public record OrderRequestDTO(DeliveryMethod deliveryMethod, String deliveryAdress, List<OrderItemRequestDTO> items) {
}
