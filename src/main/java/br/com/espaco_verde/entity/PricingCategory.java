package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity()
@Table(name = "PricingCategories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PricingCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private BigDecimal margin;

}
