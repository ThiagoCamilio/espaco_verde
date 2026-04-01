package br.com.espaco_verde.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensagemCliente extends Mensagem{

    private String texto;
    private String remetente;
    private String tipo;

}
