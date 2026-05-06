package br.com.espaco_verde.DTO;

public record OrderItemResponseDTO(Integer productId, String productName, Integer quantity,Double price) {
}
