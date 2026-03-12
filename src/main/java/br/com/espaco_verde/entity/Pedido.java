package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity (name = "Pedidos")
@Table(name = "Pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private int id;

    @OneToOne
    @JoinColumn(name = "carrinho_id")
    @Getter @Setter
    private Carrinho carrinho;

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private Status status;

    @Getter @Setter
    private double preco;

    @Getter @Setter
    private String data;

    public Pedido(Carrinho carrinho, Status status, double preco, String data){

        this.carrinho = carrinho;
        this.status = status;
        this.preco = carrinho.getPreco();
        this.data = data;

    }

}
