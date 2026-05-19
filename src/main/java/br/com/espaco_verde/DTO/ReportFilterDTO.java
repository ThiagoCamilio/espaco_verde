package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.DeliveryMethod;
import br.com.espaco_verde.entity.OrderStatus;
import br.com.espaco_verde.entity.TiposProdutos;

import java.time.LocalDateTime;

public record ReportFilterDTO(
        LocalDateTime initalDate,
        LocalDateTime finalDate,
        OrderStatus status,
        DeliveryMethod deliveryMethod,
        String custumerName,
        String productName,
        TiposProdutos productType
) {
}
