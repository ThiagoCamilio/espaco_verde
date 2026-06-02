package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity(name = "message")
@Table(name = "messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderType senderType;

    private String senderName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String status;

    @Column(columnDefinition = "TEXT", unique = true, nullable = false)
    private String wamId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public void setTimestampFromEpoch(String timestamp){
        long epochTimestamp = Long.parseLong(timestamp);
        Instant instant = Instant.ofEpochSecond(epochTimestamp);
        this.timestamp = LocalDateTime.ofInstant(instant, ZoneId.of("America/Sao_Paulo"));
    }
}
