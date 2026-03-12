package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "CupomDesconto")
@Table(name = "CupomsDesconto")
@NoArgsConstructor
@AllArgsConstructor
public class CupomDesconto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private int id;

    @Getter @Setter
    private String nome;

    @Getter @Setter
    private double desconto;

    public CupomDesconto(String nome, double desconto){

        this.nome = nome;
        this.desconto = desconto;

    }

}
