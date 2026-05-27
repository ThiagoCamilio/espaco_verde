package br.com.espaco_verde.DTO;

import java.math.BigDecimal;

public record PaymentRequestDTO(
        BigDecimal amount,
        String email,
        int orderId
) {
}
