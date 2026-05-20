package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.DeliveryMethod;
import br.com.espaco_verde.entity.OrderStatus;
import br.com.espaco_verde.entity.ReportFilter;
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

    public ReportFilter toEntity(){
        ReportFilter r = new ReportFilter();
        r.setInitalDate(initalDate);
        r.setFinalDate(finalDate);
        r.setStatus(status);
        r.setDeliveryMethod(deliveryMethod);
        r.setCustumerName(custumerName);
        r.setProductName(productName);
        r.setProductType(productType);
        return r;
    }
}
