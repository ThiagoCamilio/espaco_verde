package br.com.espaco_verde.repository;

import java.util.List;

import br.com.espaco_verde.entity.Order;
import br.com.espaco_verde.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositoryOrder extends JpaRepository<Order, Integer> {

    List<Order> findAll();

    List<Order> findByCustumerId(int userId);

    List<Order> findByOrderStatus(OrderStatus orderStatus);

    public int countByOrderStatus(OrderStatus orderStatus);
}