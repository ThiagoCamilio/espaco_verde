package br.com.espaco_verde.entity;

import lombok.Data;

@Data
public class MensagemCliente extends Mensagem{

    private String texto;
    private String remetente;
    private String tipo;

}
