package br.com.espaco_verde.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity(name = "Pagina")
@Table(name = "Paginas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pagina {

    @Id
    private String id;

    private String texto;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "paginas_proximasPaginas",
            joinColumns = @JoinColumn(name = "pagina_id"),
            inverseJoinColumns = @JoinColumn(name = "proxima_pagina_id")
    )
    private List<Pagina> proximasPaginas;
}
