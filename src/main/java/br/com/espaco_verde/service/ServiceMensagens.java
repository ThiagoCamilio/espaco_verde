package br.com.espaco_verde.service;

import br.com.espaco_verde.entity.Mensagem;
import br.com.espaco_verde.entity.MensagemCliente;
import br.com.espaco_verde.entity.MensagemSistema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceMensagens {

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.acess.token}")
    private String acessToken;

    @Value("${api.phone.number.id}")
    private String phoneNumberId;

    public Mensagem parseJson(JsonNode json){

        JsonNode value = json.at("/entry/0/changes/0/value");

        if (value.has("messages")){

            MensagemCliente msg = new MensagemCliente();
            msg.setId(value.at("/messages/0/id").asString());
            msg.setTipo(value.at("/messages/0/type").asString());
            msg.setRemetente(value.at("/messages/0/from").asString());
            if(value.at(("/messages/0")).has("text")){
                msg.setTexto(value.at("/messages/0/text/body").asString());
            }else{
                msg.setTexto(value.at("/messages/0/interactive/button_reply/id").asString());
            }

            msg.setTimestamp(value.at("/messages/0/timestamp").asString());
            return msg;

        } else if (value.has("statuses")){
            MensagemSistema msg = new MensagemSistema();
            msg.setTimestamp(value.at("/statuses/0/timestamp").asString());
            msg.setStatus(value.at("/statuses/0/status").asString());
            return msg;
        }

        return null;

    }

    public void readMessage(MensagemCliente mensagem){

        RestTemplate restTemplate = new RestTemplate();

        String url = apiUrl+phoneNumberId+"/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(acessToken);

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("status", "read");
        body.put("message_id", mensagem.getId());
        body.put("typing_indicator", Map.of("type", "text"));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
    }

    public void sendTextMessage(MensagemCliente mensagem, String messageText){

        RestTemplate restTemplate = new RestTemplate();

        String url = apiUrl+phoneNumberId+"/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(acessToken);

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", mensagem.getRemetente());
        body.put("type", "text");
        body.put("text", Map.of("body", messageText));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

    }

    public void sendButtonMessage(MensagemCliente mensagem){

        RestTemplate restTemplate = new RestTemplate();

        String url = apiUrl+phoneNumberId+"/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(acessToken);

        List<Map<String, Object>> botoes = new ArrayList<>();

        botoes.add(Map.of("type", "reply", "reply", Map.of("id", "produtos", "title", "produtos")));
        botoes.add(Map.of("type", "reply", "reply", Map.of("id", "carrinho", "title", "carrinho")));
        botoes.add(Map.of("type", "reply", "reply", Map.of("id", "finalizar compra", "title", "finalizar compra")));

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", mensagem.getRemetente());
        body.put("type", "interactive");
        body.put("interactive",
                Map.of("type", "button", "header",
                    Map.of("type", "text", "text","Menu Principal"),
                "body",
                    Map.of("text", "Esse é o Menu Principal, escolha uma das opções abaixo"),
                "action",
                    Map.of("buttons", botoes)
        ));

        System.out.println(body);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

    }

}
