package br.com.espaco_verde.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "PricingConfig")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PricingConfig {

    @Id
    private int id = 1;

    private BigDecimal expectedRevenue;

    private BigDecimal fixedExpenses;

    private BigDecimal variableExpenses;

}
