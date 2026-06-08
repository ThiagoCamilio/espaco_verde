package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrderResponseDTO(
        Integer id,
        String orderStatus,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        List<OrderItemResponseDTO> items,
        String deliveryMethod,
        String deliveryAdress,
        OrderUserDTO customer,
        PaymentResponseDTO payment
)
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

        PaymentResponseDTO payment = null;
        if(o.getPayment() != null){
            payment = new PaymentResponseDTO(
                o.getPayment().getQrCodeBase(),
                o.getPayment().getCopyAndPastCode(),
                o.getPayment().getUrl()
            );
        }

        this(
          o.getId(),
          o.getOrderStatus().getType(),
          o.getTotalPrice(),
          o.getCreatedAt(),
          itemsDTO,
          o.getDeliveryMethod().name(),
          o.getDeliveryAddress(),
          new OrderUserDTO(
            o.getCustumer().getId(),
            o.getCustumer().getName()
          ),
          payment
        );
    }
}
