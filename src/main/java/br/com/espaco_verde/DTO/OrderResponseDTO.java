package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrderResponseDTO(Integer id, String orderStatus, java.math.BigDecimal totalPrice, LocalDateTime createdAt, List<OrderItemResponseDTO> items, String deliveryMethod, String deliveryAdress, OrderUserDTO customer)
{
    public OrderResponseDTO(Order o){

        List<OrderItemResponseDTO> itemsDTO = o.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getProduct().getId(),
                        item.getProduct().getNome(),
                        item.getQuantity(),
                        item.getUnitPrice()
                ))
                .collect(Collectors.toList());

        this(
          o.getId(),
          o.getOrderStatus().name(),
          o.getTotalPrice(),
          o.getCreatedAt(),
          itemsDTO,
          o.getDeliveryMethod().name(),
          o.getDeliveryAdress(),
          new OrderUserDTO(
                  o.getCustumer().getId(),
                  o.getCustumer().getName()
          )
        );
    }

}
