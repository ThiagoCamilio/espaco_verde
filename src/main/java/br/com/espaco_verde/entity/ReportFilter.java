package br.com.espaco_verde.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportFilter {

    private LocalDateTime initalDate;
    private LocalDateTime finalDate;
    private OrderStatus status;
    private DeliveryMethod deliveryMethod;
    private String custumerName;
    private String productName;
    private TiposProdutos productType;

}
