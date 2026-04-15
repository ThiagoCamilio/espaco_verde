package br.com.espaco_verde.control;

import br.com.espaco_verde.entity.*;
import br.com.espaco_verde.repository.RepositoryProduto;
import br.com.espaco_verde.service.ServiceConversa;
import br.com.espaco_verde.service.ServiceMensagens;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RequestMapping("whatsapp")
@RestController
public class ControlConversa {

    @Value("${wa.api.verify.token}")
    private String token ="token-verificacao";

    @Autowired
    private ServiceMensagens serviceMensagens;

    @Autowired
    private ServiceConversa serviceConversa;

    @Autowired
    private RepositoryProduto repositoryProduto;

    @GetMapping("/home")
    public String home(){

        return "Ola";

    }

    /*@GetMapping("/testeCarousel")
    public void testeCarousel(){

        MensagemCliente m = new MensagemCliente("Oi", "42991057267", "Mensagem");

        List<Produto> produtos = repositoryProduto.findAll();

        Map<String, Object> map = serviceMensagens.sendCarouselMessage(m , produtos);

        for (String keys : map.keySet())
        {
            System.out.println(keys + ":"+ map.get(keys));
        }
    }*/



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
    public ResponseEntity<?> webhook(@RequestBody JsonNode json) throws Exception {

        Mensagem mensagem = serviceMensagens.parseJson(json);


        /*if (mensagem instanceof MensagemSistema msgSistema && msgSistema.getStatus().equals("sent")){

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

        }else*/
        if (mensagem instanceof MensagemCliente msgCliente){

            serviceConversa.getConversa(msgCliente);

            serviceMensagens.readMessage(msgCliente);

            serviceConversa.processarMensagem(msgCliente);

        }

        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
