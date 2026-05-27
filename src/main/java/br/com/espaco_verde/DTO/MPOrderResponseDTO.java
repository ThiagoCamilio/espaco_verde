package br.com.espaco_verde.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record MPOrderResponseDTO(
        String id,
        String status,

        @JsonProperty("status_detail")
        String statusDetail,

        @JsonProperty("external_reference")
        int externalReference,

        @JsonProperty("total_paid_amount")
        BigDecimal totalPaidAmount
) {
}
