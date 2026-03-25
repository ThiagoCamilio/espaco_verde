package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "TemplateResposta")
@Table(name = "TemplateRespostas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemplateResposta {

    @Id
    private String id;
    private String texto;
    @Enumerated(EnumType.STRING)
    private EstadoConversa estadoConversa;

}
