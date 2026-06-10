package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.*;
import br.com.espaco_verde.entity.*;
import br.com.espaco_verde.repository.RepositoryOrder;
import br.com.espaco_verde.repository.RepositoryProduto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceOrder {

    @Autowired
    private RepositoryOrder repositoryOrder;

    @Autowired
    private RepositoryProduto repositoryProduct;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ServicePayment servicePayment;

    @Autowired
    private CartService cartService;

    @Autowired
    private MessageService messageService;

    @Transactional
    public OrderResponseDTO createOrder(String deliveryAddress, int userId){

        Cart cart = cartService.getCart(userId);

        if(cart.getProductCarts().isEmpty()){
            throw new RuntimeException("Não foi possível finalizar o pedido pois o carrinho esta vazio");
        }
        Order order = new Order();
        order.setCustumer(cart.getUser());
        if(StringUtils.hasText(deliveryAddress)){
            order.setDeliveryMethod(DeliveryMethod.DELIVERY);
            order.setDeliveryAddress(deliveryAddress);
        }else {
            order.setDeliveryMethod(DeliveryMethod.PICKUP);
            order.setDeliveryAddress(null);
        }
        BigDecimal totalPrice = BigDecimal.ZERO;

        for(ProductCart productCart : cart.getProductCarts()){
            Product product = repositoryProduct.findById(productCart.getProduct().getId())
                    .orElseThrow(()-> new RuntimeException("Product Indisponivel"));

            if (product.getAvailableQuantity() < productCart.getQuantity()){
                throw new RuntimeException("Estoque insuficiente para o produto "+product.getNome());
            }

            product.setReservedQuantity(product.getReservedQuantity() + productCart.getQuantity());
            repositoryProduct.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(productCart.getQuantity());
            orderItem.setUnitPrice(product.getPreco());
            order.getItems().add(orderItem);
            BigDecimal subtotal = product.getPreco().multiply(BigDecimal.valueOf(productCart.getQuantity()));
            totalPrice = subtotal.add(totalPrice);
        }
        order.setTotalPrice(totalPrice);

        Order persistOrder = repositoryOrder.save(order);
        int pendingOrders = repositoryOrder.countByOrderStatus(OrderStatus.AWAITING_ANALYSIS);
        simpMessagingTemplate.convertAndSend("/topic/pending-orders", pendingOrders);
        cartService.clearCart(userId);

        return new OrderResponseDTO(persistOrder);

    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(int orderId, UpdateOrderDTO updateOrder){
        Order order = repositoryOrder.findById(orderId).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        String oldStatus = order.getOrderStatus().name();
        String newStatus = OrderStatus.fromString(updateOrder.status()).name();

        if(oldStatus.equals(newStatus)){
            return new OrderResponseDTO(order);
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
                order.getPayment().setStatus("PAID");
            }else if(wasPending && isNowCanceled){
                product.setReservedQuantity(product.getReservedQuantity() - quantity);
            }else if(!wasPending && isNowCanceled){
                product.setReservedQuantity(product.getReservedQuantity() - quantity);
                product.setStockQuantity(product.getStockQuantity() + quantity);
            }
        }

        order.setOrderStatus(OrderStatus.valueOf(newStatus));

        if(order.getOrderStatus().equals(OrderStatus.AWAITING_PAYMENT)){

            PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(
                    order.getTotalPrice(),
                    order.getCustumer().getLogin(),
                    order.getId()
            );

            PaymentResponseDTO paymentResponseDTO = servicePayment.createPayment(paymentRequestDTO);

            Payment payment = new Payment(
                    paymentRequestDTO.amount(),
                    "PENDING",
                    paymentResponseDTO.copyAndPastCode(),
                    paymentResponseDTO.qrCodeBase64(),
                    paymentResponseDTO.paymentUrl(),
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    order
            );

            order.setPayment(payment);

        }

        int pendingOrders = repositoryOrder.countByOrderStatus(OrderStatus.AWAITING_ANALYSIS);
        simpMessagingTemplate.convertAndSend("/topic/pending-orders", pendingOrders);

        int userId = order.getCustumer().getId();
        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/order-updates-message",
                "Seu pedido #"+order.getId()+" agora esta: "+order.getOrderStatus().getType()
        );

        simpMessagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/order-updates",
                new OrderResponseDTO(order)
        );

        messageService.sendUpdateOrderMessage(order);

        return new OrderResponseDTO(order);
    }

    @Transactional
    public List<OrderResponseDTO> getAllOrders(){
        List<Order> orders = repositoryOrder.findAll();
        return orders.stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<OrderResponseDTO> getUserOrders(int userId) {
        List<Order> orders = repositoryOrder.findByCustumerId(userId);

        return orders.stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getActiveUserOrders(Integer userId){

        List<OrderStatus> statuses = List.of(OrderStatus.DELIVERED, OrderStatus.CANCELED);

        List<Order> orders = repositoryOrder.findTop10ByCustumerIdAndOrderStatusNotInOrderByCreatedAtDesc(userId, statuses);

        return orders.stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Integer getPendingOrdersCount(){
        return repositoryOrder.countByOrderStatus(OrderStatus.AWAITING_ANALYSIS);
    }

    public OrderResponseDTO getOrderById(int orderId) {
        Order order = repositoryOrder.findById(orderId).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        return new OrderResponseDTO(order);
    }
}
