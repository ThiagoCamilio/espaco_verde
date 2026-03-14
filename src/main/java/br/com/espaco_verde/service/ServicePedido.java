package br.com.espaco_verde.service;

import br.com.espaco_verde.entity.Pedido;
import br.com.espaco_verde.repository.RepositoryPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ServicePedido {

    @Autowired
    RepositoryPedido repositoryPedido;

    public ResponseEntity<?> cadastrar(Pedido pedido){

        return new ResponseEntity<>(repositoryPedido.save(pedido), HttpStatus.CREATED);

    }

}
