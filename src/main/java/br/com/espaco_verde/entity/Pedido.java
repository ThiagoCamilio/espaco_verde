package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity (name = "Pedidos")
@Table(name = "Pedidos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "carrinho_id")
    private Carrinho carrinho;

    @Enumerated(EnumType.STRING)
    private Status status;

    private double preco;

    private String data;

    public Pedido(Carrinho carrinho, Status status, String data){

        this.carrinho = carrinho;
        this.status = status;
        this.preco = carrinho.getPreco();
        this.data = data;

    }

}
