package br.com.espaco_verde.repositorio;

import java.util.List;

import br.com.espaco_verde.entity.Pedido;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositorioPedido extends CrudRepository<Pedido, Integer>{

    List<Pedido> findAll();

    Pedido findById(int id);

}