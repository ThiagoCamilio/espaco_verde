package br.com.espaco_verde.control;

import br.com.espaco_verde.DTO.PaymentRequestDTO;
import br.com.espaco_verde.DTO.PaymentResponseDTO;
import br.com.espaco_verde.service.ServicePayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/payment")
public class ControlPayment {

    @Autowired
    private ServicePayment servicePayment;

    @PostMapping
    public ResponseEntity<?> createPayment (@RequestBody PaymentRequestDTO paymentRequestDTO){
        try {
            PaymentResponseDTO response = servicePayment.createPayment(paymentRequestDTO);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body("Erro ao criar a cobrança " + e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
