package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.MPOrderResponseDTO;
import br.com.espaco_verde.DTO.PaymentRequestDTO;
import br.com.espaco_verde.DTO.PaymentResponseDTO;
import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class ServicePayment {

    @Value("${mp.access.token}")
    private String accessToken;

    @Value("${mp.orders.url}")
    private String mpUrl;

    @PostConstruct
    public void onInit(){
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO){

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        headers.set("x-idempotency-key", UUID.randomUUID().toString());

        Map<String, String> payer = Map.of(
                "email", paymentRequestDTO.email()
        );

        Map<String, String> paymentMethod = Map.of(
                "id", "pix",
                "type", "bank_transfer"
        );


        Map<String, Object> payment = Map.of(
                "amount", paymentRequestDTO.amount().toString(),
                "payment_method", paymentMethod
        );
        List<Map<String, Object>> payments = new ArrayList<>();
        payments.add(payment);

        Map<String, Object> transaction = Map.of(
                "payments", payments
        );

        Map<String, Object> request = Map.of(
                "type","online",
                "total_amount", paymentRequestDTO.amount().toString(),
                "external_reference",Integer.toString(paymentRequestDTO.orderId()),
                "payer",payer,
                "transactions",transaction
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(mpUrl, entity, String.class);

            String responseBody = response.getBody();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode =  mapper.readTree(responseBody);
            JsonNode transactionNode = rootNode.path("transactions");
            JsonNode paymentArray = transactionNode.path("payments");
            JsonNode paymentMethodData = paymentArray.get(0).path("payment_method");
            String qrCodeBase64 = paymentMethodData.path("qr_code_base64").asString();
            String copyAndPastCode = paymentMethodData.path("qr_code").asString();
            String paymentUrl = paymentMethodData.path("ticket_url").asString();

            return new PaymentResponseDTO(qrCodeBase64, copyAndPastCode, paymentUrl);

        }catch (HttpClientErrorException e){
            throw new IllegalArgumentException("Dados recusados pelo Mercado Pago " + e.getMessage());
        }catch (Exception e){
            throw new RuntimeException("Falha interna ao processar pagamento via PIX." + e.getMessage());
        }
    }



    public MPOrderResponseDTO fetchOrderDetails(String dataId) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://api.mercadopago.com/v1/payments/"+dataId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<MPOrderResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, MPOrderResponseDTO.class );

        return response.getBody();
    }

}
