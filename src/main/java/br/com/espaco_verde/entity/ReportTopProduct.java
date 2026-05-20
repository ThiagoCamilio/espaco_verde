package br.com.espaco_verde.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "ReportTopProducts")
@Table(name = "ReportTopProducts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportTopProduct {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repor_id", nullable = false)
    private Report report;

    private int productId;

    private String productName;

    private int soldQuantity;

    private BigDecimal totalValue;

    public ReportTopProduct(Integer productid, String productName, Integer soldQuantity, BigDecimal totalValue) {
    }
}
