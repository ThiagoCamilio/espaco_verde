package br.com.espaco_verde.DTO;

import jakarta.validation.constraints.Min;

public record OrderItemRequestDTO (Integer productId, @Min(value = 1) Integer quantity){
}
