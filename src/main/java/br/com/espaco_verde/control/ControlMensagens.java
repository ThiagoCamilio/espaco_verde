package br.com.espaco_verde.control;

import br.com.espaco_verde.entity.Mensagem;
import br.com.espaco_verde.entity.MensagemCliente;
import br.com.espaco_verde.entity.MensagemSistema;
import br.com.espaco_verde.service.ServiceMensagens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

@RestController
public class ControlMensagens {

    @Value("${api.verify_token}")
    private String token ="token-verificacao";

    @Autowired
    private ServiceMensagens serviceMensagens;

    @GetMapping("/home")
    public String home(){

        return "Ola";

    }


    @GetMapping("/webhook")
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

    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody JsonNode json){

        Mensagem mensagem = serviceMensagens.parseJson(json);

        if (mensagem instanceof MensagemSistema msgSistema && msgSistema.getStatus().equals("sent")){

            System.out.println("Resposta enviada ao cliente");
            System.out.println(msgSistema.getStatus());
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        }else if (mensagem instanceof MensagemSistema msgSistema && msgSistema.getStatus().equals("delivered")){

            System.out.println("Resposta recebida pelo cliente");
            System.out.println(msgSistema.getStatus());
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));

        }else if (mensagem instanceof MensagemSistema msgSistema && msgSistema.getStatus().equals("read")){

            System.out.println("Resposta lida pelo cliente");
            System.out.println(msgSistema.getStatus());
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));

        }else if (mensagem instanceof MensagemCliente msgCliente){

            String phoneNumber = msgCliente.getRemetente();
            String message = "FUNCIONOU";
            System.out.println(msgCliente.getRemetente() + " enviou " + msgCliente.getTexto());
            serviceMensagens.sendMessage(phoneNumber, message);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));

        } else {

            System.out.println("Algo deu errado");
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));

        }
    }
}
