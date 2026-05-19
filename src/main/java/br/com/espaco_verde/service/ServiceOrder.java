package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.*;
import br.com.espaco_verde.entity.*;
import br.com.espaco_verde.repository.RepositoryOrder;
import br.com.espaco_verde.repository.RepositoryProduto;
import br.com.espaco_verde.repository.RepositoryUser;
import br.com.espaco_verde.repository.specification.OrderSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceOrder {

    @Autowired
    private RepositoryOrder repositoryOrder;

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private RepositoryProduto repositoryProduct;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest, int userId){

        User customer = repositoryUser.findById(userId).orElseThrow(()->new RuntimeException("Usuario não encontrado."));
        Order order = new Order();
        order.setCustumer(customer);
        order.setDeliveryMethod(orderRequest.deliveryMethod());
        order.setDeliveryAdress(orderRequest.deliveryAdress());
        BigDecimal totalPrice = BigDecimal.ZERO;

        for(OrderItemRequestDTO itemRequestDTO : orderRequest.items()){
            Product product = repositoryProduct.findById(itemRequestDTO.productId())
                    .orElseThrow(()-> new RuntimeException("Product Indisponivel"));

            if (product.getAvaliableQuantity() < itemRequestDTO.quantity()){
                throw new RuntimeException("Estoque insuficiente para o produto "+product.getNome());
            }

            product.setReservedQuantity(product.getReservedQuantity() + itemRequestDTO.quantity());
            repositoryProduct.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequestDTO.quantity());
            orderItem.setUnitPrice(product.getPreco());

            order.getItems().add(orderItem);
            BigDecimal subtotal = product.getPreco().multiply(BigDecimal.valueOf(itemRequestDTO.quantity()));
            totalPrice = subtotal.multiply(totalPrice);
        }
        order.setTotalPrice(totalPrice);

        Order persistOrder = repositoryOrder.save(order);
        int pendingOrders = repositoryOrder.countByOrderStatus(OrderStatus.AWAITING_ANALYSIS);
        simpMessagingTemplate.convertAndSend("/topic/pending-orders", pendingOrders);

        return toOrderResponseDTO(persistOrder);

    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(int orderId, UpdateOrderDTO updateOrder){
        Order order = repositoryOrder.findById(orderId).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        String oldStatus = order.getOrderStatus().name();
        String newStatus = updateOrder.status();

        if(oldStatus.equals(newStatus)){
            return toOrderResponseDTO(order);
        }

        boolean wasPending = oldStatus.equals("AWAITING_ANALYSIS") || oldStatus.equals("AWAITING_PAYMENT");
        boolean isNowApproved = newStatus.equals("PAID") || newStatus.equals("IN_DELIVERY") || newStatus.equals("DELIVERED");
        boolean isNowCanceled = newStatus.equals("CANCELED");

        for(OrderItem item : order.getItems()){
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            if(wasPending && isNowApproved){
                product.setStockQuantity(product.getStockQuantity() - quantity);
                product.setReservedQuantity(product.getReservedQuantity() - quantity);
            }else if(wasPending && isNowCanceled){
                product.setReservedQuantity(product.getReservedQuantity() - quantity);
            }else if(!wasPending && isNowCanceled){
                product.setReservedQuantity(product.getReservedQuantity() - quantity);
                product.setStockQuantity(product.getStockQuantity() + quantity);
            }
        }

        order.setOrderStatus(OrderStatus.valueOf(newStatus));
        int pendingOrders = repositoryOrder.countByOrderStatus(OrderStatus.AWAITING_ANALYSIS);
        simpMessagingTemplate.convertAndSend("/topic/pending-orders", pendingOrders);

        int userId = order.getCustumer().getId();
        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/order-updates",
                "Seu pedido #"+order.getId()+" agora esta: "+order.getOrderStatus().getType()
        );

        return toOrderResponseDTO(order);
    }


    @Transactional
    public List<OrderResponseDTO> getAllOrders(){
        List<Order> orders = repositoryOrder.findAll();
        return orders.stream()
                .map(this::toOrderResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrderResponseDTO> getUserOrders(int userId) {
        List<Order> orders = repositoryOrder.findByCustumerId(userId);

        return orders.stream()
                .map(this::toOrderResponseDTO)
                .collect(Collectors.toList());
    }

    public Integer getPendingOrdersCount(){
        return repositoryOrder.countByOrderStatus(OrderStatus.AWAITING_ANALYSIS);
    }

    @Transactional
    public List<OrderResponseDTO> getOrdersReport (ReportFilterDTO reportFilterDTO){

        List<Order> reportOrders = repositoryOrder.findAll(OrderSpecification.withFilter(reportFilterDTO));

        return reportOrders.stream()
                .map(this::toOrderResponseDTO)
                .collect(Collectors.toList());
    }

    private OrderResponseDTO toOrderResponseDTO(Order order) {
        List<OrderItemResponseDTO> itemsDTO = order.getItems().stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getProduct().getId(),
                        item.getProduct().getNome(),
                        item.getQuantity(),
                        item.getUnitPrice()
                ))
                .collect(Collectors.toList());

        return new OrderResponseDTO(
                order.getId(),
                order.getOrderStatus().name(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                itemsDTO,
                order.getDeliveryMethod().name(),
                order.getDeliveryAdress(),
                new OrderUserDTO(
                        order.getCustumer().getId(),
                        order.getCustumer().getName()
                )
        );
    }

}
