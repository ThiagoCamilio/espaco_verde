package br.com.espaco_verde.control;

import br.com.espaco_verde.entity.*;
import br.com.espaco_verde.repository.RepositoryProduto;
import br.com.espaco_verde.service.ServiceConversa;
import br.com.espaco_verde.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

//@RequestMapping("whatsapp")
@RestController
public class ControlConversa {

    @Value("${wa.api.verify.token}")
    private String token ="token-verificacao";

    @Autowired
    private MessageService messageService;

    @Autowired
    private ServiceConversa serviceConversa;

    @Autowired
    private RepositoryProduto repositoryProduto;

    @GetMapping("/webhook/whatsapp")
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

    @PostMapping("/webhook/whatsapp")
    public ResponseEntity<?> webhook(@RequestBody JsonNode json) throws Exception {
        Message message = messageService.parseJson(json);
        System.out.println(json);

        if (message != null && message.getSenderType().equals(SenderType.CLIENT)){
            serviceConversa.processarMensagem(message);
        }

        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
