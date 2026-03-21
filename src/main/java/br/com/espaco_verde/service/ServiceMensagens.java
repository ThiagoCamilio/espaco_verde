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

import java.util.HashMap;
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
            msg.setTipo(value.at("/messages/0/type").asString());
            msg.setRemetente(value.at("/messages/0/from").asString());
            msg.setTexto(value.at("/messages/0/text/body").asString());
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

    public void sendMessage(String toPhone, String messageText){

        RestTemplate restTemplate = new RestTemplate();

        String url = apiUrl+phoneNumberId+"/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(acessToken);

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", toPhone);
        body.put("type", "text");
        body.put("text", Map.of("body", messageText));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        System.out.println("Resposta da Meta: " + response.getBody());

    }

}
