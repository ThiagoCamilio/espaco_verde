package br.com.espaco_verde.repository;

import java.time.LocalDateTime;
import java.util.List;

import br.com.espaco_verde.DTO.ReportTopProductProjection;
import br.com.espaco_verde.entity.Order;
import br.com.espaco_verde.entity.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositoryOrder extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {

    public List<Order> findAll();

    public List<Order> findByCustumerId(int userId);

    public List<Order> findByOrderStatus(OrderStatus orderStatus);

    public int countByOrderStatus(OrderStatus orderStatus);

    @Query(
            "SELECT " +
            "i.product.id AS productId, " +
            "i.product.nome AS productName, " +
            "SUM(i.quantity) AS soldQuantity, " +
            "SUM(i.quantity * i.unitPrice) as totalValue " +
            "FROM Orders o " +
            "JOIN o.items i " +
            "WHERE o.createdAt BETWEEN :initialDate AND :finalDate " +
            "AND (:status IS NULL OR o.orderStatus =:status) " +
            "GROUP BY i.product.id, i.product.nome " +
            "ORDER BY SUM(i.quantity) DESC"
    )
    List<ReportTopProductProjection> getMostSoldProducts(
            @Param("initialDate")LocalDateTime initialDate,
            @Param("finalDate") LocalDateTime finalDate,
            @Param("status") OrderStatus status,
            Pageable pageable
            );
}