package br.com.espaco_verde.repository;

import java.util.List;

import br.com.espaco_verde.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositoryPedido extends JpaRepository<Pedido, Integer> {

    List<Pedido> findAll();

    Pedido findById(int id);

}