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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pagina_atual_id")
    private Pagina paginaAtual;


    public Conversa(String telefone, String idMensagem) {

        this.telefone = telefone;
        this.idMensagem = idMensagem;
        this.paginaAtual = null;

    }
}
