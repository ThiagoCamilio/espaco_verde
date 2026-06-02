package br.com.espaco_verde.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Chat")
@Table(name = "Chats")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String whatsappNumber;

    /*@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pagina_atual_id")
    private Pagina paginaAtual;*/

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatState chatState = ChatState.NEW_CONTACT;

    @Column(nullable = false)
    private LocalDateTime lastUserInteraction;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    public Chat(String whatsappNumber, ChatState chatState){
        this.whatsappNumber = whatsappNumber;
        this.chatState = chatState;
        refreshInteraction();
    }

    public boolean isWithinFreeWindow(){
        if(lastUserInteraction == null){
            return false;
        }
        return LocalDateTime.now().isBefore(lastUserInteraction.plusHours(24));
    }

    public void refreshInteraction(){
        this.lastUserInteraction = LocalDateTime.now();
    }
}
