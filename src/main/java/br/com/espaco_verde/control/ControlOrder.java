package br.com.espaco_verde.control;

import br.com.espaco_verde.DTO.OrderRequestDTO;
import br.com.espaco_verde.DTO.OrderResponseDTO;
import br.com.espaco_verde.DTO.UpdateOrderDTO;
import br.com.espaco_verde.entity.User;
import br.com.espaco_verde.service.ServiceOrder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ControlOrder {

    @Autowired
    private ServiceOrder serviceOrder;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderResquest, @AuthenticationPrincipal User user){
        int userId = user.getId();
        OrderResponseDTO responseDTO = serviceOrder.createOrder(orderResquest, userId);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @PatchMapping("/admin/orders/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable("id") int orderId, @RequestBody UpdateOrderDTO updateOrderDTO){
        OrderResponseDTO updatedOrder = serviceOrder.updateOrderStatus(orderId, updateOrderDTO);
        return ResponseEntity.status(200).body(updatedOrder);
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(){
        List<OrderResponseDTO> orders = serviceOrder.getAllOrders();
        return ResponseEntity.status(200).body(orders);
    }

    @GetMapping("/admin/orders/pending-count")
    public ResponseEntity<Integer> getPendingOrdersCont(){
        return ResponseEntity.ok(serviceOrder.getPendingOrdersCount());
    }

    @GetMapping("/user/orders")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(@AuthenticationPrincipal User user){
        int userId = user.getId();
        List<OrderResponseDTO> myOrders = serviceOrder.getUserOrders(userId);
        return ResponseEntity.status(200).body(myOrders);
    }
}
