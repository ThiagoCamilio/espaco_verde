package br.com.espaco_verde.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Conversa")
@Table(name = "Conversas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Conversa {

    @Id
    private String telefone;

    private String idMensagem;

    @Enumerated(EnumType.STRING)
    private EstadoConversa estadoConversa;


    public Conversa(String telefone, String idMensagem) {

        this.telefone = telefone;
        this.idMensagem = idMensagem;
        this.estadoConversa = null;

    }
}
