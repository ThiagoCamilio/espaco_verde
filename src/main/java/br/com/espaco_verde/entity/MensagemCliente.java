package br.com.espaco_verde.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MensagemCliente extends Mensagem{

    private String texto;
    private String remetente;


}
