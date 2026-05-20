package br.com.espaco_verde.DTO;

import java.math.BigDecimal;

public interface ReportTopProductProjection {

    Integer getProductid();

    String getProductName();

    Integer getSoldQuantity();

    BigDecimal getTotalValue();

}
