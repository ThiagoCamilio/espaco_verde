package br.com.espaco_verde.control;

import br.com.espaco_verde.entity.Message;
import br.com.espaco_verde.entity.SenderType;
import br.com.espaco_verde.service.MessageService;
import br.com.espaco_verde.service.ChatService;
import br.com.espaco_verde.service.ServiceMercadoPagoWebhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/webhook")
public class ControlWebhook {

    @Value("${wa.api.verify.token}")
    private String token ="token-verificacao";

    @Autowired
    private ServiceMercadoPagoWebhook serviceMPWebhook;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatService chatService;

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

    @GetMapping("/whatsapp")
    public ResponseEntity<?> webhook(@RequestParam String hub_mode , @RequestParam String hub_challenge, @RequestParam String hub_verify_token ){
        System.out.println(hub_mode);
        System.out.println(hub_challenge);
        System.out.println(hub_verify_token);

        if (!hub_mode.isEmpty() || hub_verify_token.equals(token)){
            return new ResponseEntity<>(hub_challenge, HttpStatusCode.valueOf(200));
        }else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }

    }

    @PostMapping("/whatsapp")
    public ResponseEntity<?> webhook(@RequestBody JsonNode json) throws Exception {
        Message message = messageService.parseJson(json);
        if (message != null && message.getSenderType().equals(SenderType.CLIENT)){
            chatService.processMessage(message);
        }

        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
