package br.com.espaco_verde.control;

import br.com.espaco_verde.service.ServiceMercadoPagoWebhook;
import br.com.espaco_verde.service.ServicePayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/webhook")
public class ControlWebhook {

    @Autowired
    private ServiceMercadoPagoWebhook serviceMPWebhook;

    @PostMapping("/mercadopago")
    public ResponseEntity<Void> paymentNotification(
            @RequestBody String json,
            @RequestParam(name = "data.id") String dataId,
            @RequestParam(name = "type") String type,
            @RequestHeader(name = "x-signature") String xSignature,
            @RequestHeader(name = "x-request-id") String xRequestId
    ){
        System.out.println(json);
        try{
            serviceMPWebhook.validateWebhook(dataId, type, xSignature, xRequestId);
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
