package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private BigDecimal amount;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String copyAndPastCode;

    @Column(columnDefinition = "TEXT")
    private String qrCodeBase;

    @Column(columnDefinition = "TEXT")
    private String url;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "payment")
    private Order order;

    public Payment (
            BigDecimal amount,
            String status,
            String copyAndPastCode,
            String qrCodeBase,
            String url,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Order order
    ){
        this.amount = amount;
        this.status = status;
        this.copyAndPastCode = copyAndPastCode;
        this.qrCodeBase = qrCodeBase;
        this.url = url;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.order = order;
    }
}
