package br.com.espaco_verde.DTO;

public record PaymentResponseDTO(
        String qrCodeBase64,
        String copyAndPastCode,
        String paymentUrl
) {
}
