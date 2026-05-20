package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "reports")
@Table(name = "reports")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime createdAt;

    private BigDecimal totalOrdersValue;

    private Integer ordersQuantity;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportTopProduct> topProducts = new ArrayList<>();

    @Embedded
    private ReportFilter filters;

    private String pdfPath;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    public void addTopProduct(ReportTopProduct topProduct){
        topProducts.add(topProduct);
        topProduct.setReport(this);
    }

}
