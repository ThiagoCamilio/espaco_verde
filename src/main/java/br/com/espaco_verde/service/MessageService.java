package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.ProductDTO;
import br.com.espaco_verde.entity.*;
import br.com.espaco_verde.repository.RepositoryPagina;
import br.com.espaco_verde.repository.RepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    @Value("${wa.api.url}")
    private String waApiUrl;

    @Value("${wa.api.acess.token}")
    private String waAcessToken;

    @Value("${wa.api.phone.number.id}")
    private String waPhoneNumberId;

    @Value("${local.api.url}")
    private String localApiUrl;

    @Autowired
    private RepositoryPagina repositoryPagina;

    @Autowired
    private RepositoryUser repositoryUser;

    public Message parseJson(JsonNode json){

        JsonNode value = json.at("/entry/0/changes/0/value");

        Message message = new Message();

        if (value.has("messages")){

            message.setSenderType(SenderType.CLIENT);
            message.setWamId(value.at("/messages/0/id").asString());
            message.setPhone(value.at("/messages/0/from").asString());
            message.setSenderName(value.at("/contacts/0/profile/name").asString());

            String messageType  = value.at("/messages/0/type").asString();

            switch (messageType){
                case "text":
                    message.setContent(value.at("/messages/0/text/body").asString());
                    break;

                case "interactive":
                    String interactiveType = value.at("/messages/0/interactive/type").asString();
                    if(interactiveType.equals("button_reply")){
                        message.setContent(value.at("/messages/0/interactive/button_reply/id").asString());
                    } else if (interactiveType.equals("list_reply")) {
                        message.setContent(value.at("/messages/0/interactive/list_reply/id").asString());
                    }
                    break;

                case "button":
                    message.setContent(value.at("/messages/0/button/text").asString());
                    break;

                default:
                    message.setContent("UNSUPPORTED_MESSAGE_TYPE");
                    break;
            }

            message.setTimestampFromEpoch(value.at("/messages/0/timestamp").asString());
            return message;

        }

        return null;

    }

    public void readMessage(Message message){

        RestTemplate restTemplate = new RestTemplate();

        String url = waApiUrl + waPhoneNumberId +"/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(waAcessToken);

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("status", "read");
        body.put("message_id", message.getWamId());
        body.put("typing_indicator", Map.of("type", "text"));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
    }

    public void sendTextMessage(Message message, String messageText){

        RestTemplate restTemplate = new RestTemplate();

        String url = waApiUrl + waPhoneNumberId +"/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(waAcessToken);

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", message.getPhone());
        body.put("type", "text");
        body.put("text", Map.of("body", messageText));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        System.out.println(response);

    }

    public void sendButtonMessage(MensagemCliente mensagem, Pagina pagina){

        RestTemplate restTemplate = new RestTemplate();

        String url = waApiUrl + waPhoneNumberId +"/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(waAcessToken);

        List<Map<String, Object>> botoes = new ArrayList<>();

        for(Pagina p : pagina.getProximasPaginas()){
            botoes.add(Map.of("type", "reply", "reply", Map.of("id", p.getId(), "title", p.getId())));
        }

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", mensagem.getRemetente());
        body.put("type", "interactive");
        body.put("interactive",
                Map.of("type", "button",
                        "header", Map.of("type", "text", "text","Menu Principal"),
                        "body", Map.of("text", "Esse é o Menu Principal, escolha uma das opções abaixo"),
                        "action", Map.of("buttons", botoes)
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

    }

    public void sendCarouselMessage(MensagemCliente mensagem , List<ProductDTO> produtos) {

        RestTemplate restTemplate = new RestTemplate();

        String url = waApiUrl + waPhoneNumberId + "/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(waAcessToken);

        List<Map<String, Object>> cards = new ArrayList<>();
        for (ProductDTO p : produtos) {

            List<Map<String, Object>> buttonCard = new ArrayList<>();
            buttonCard.add(Map.of("type", "quick_reply", "quick_reply", Map.of("id", "1", "title", "Add Carrinho")));

            cards.add(Map.of("card_index", Integer.toString(produtos.indexOf(p)),
                    "type","cta_url",
                    "header", Map.of("type","image", "image",Map.of("link", localApiUrl+"/produtos/imagem/"+p.imagem())),
                    "body", Map.of("text", p.nome()),
                    "action", Map.of("buttons", buttonCard)
            ));

        }

        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", mensagem.getRemetente());
        body.put("type", "interactive");
        body.put("interactive",
                Map.of("type", "carousel",
                        "body", Map.of("text", "Seguem os produtos disponiveis"),
                        "action", Map.of("cards", cards)
                ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

    }

    public void sendInteractiveList(String phone, String systemResponseText, String buttonText, Map<String, String> opt) {

        List<Map<String, Object>> menu = new ArrayList<>();
        for(Map.Entry<String, String> entry: opt.entrySet()){
            Map<String, Object> row = new HashMap<>();
            row.put("id", entry.getKey());
            row.put("title", entry.getValue());
            menu.add(row);
        }

        Map<String, Object> section = new HashMap<>();
        section.put("title", "Opções do Menu");
        section.put("rows", menu);

        Map<String, Object> action = new HashMap<>();
        action.put("button", buttonText);
        action.put("sections", List.of(section));

        Map<String, Object> header = new HashMap<>();
        header.put("type", "text");
        header.put("text", "Bem vindo à Espaço Verde");

        Map<String, Object> interactive = new HashMap<>();
        interactive.put("type", "list");
        interactive.put("body", Map.of("text", systemResponseText));
        interactive.put("header", header);
        interactive.put("footer", Map.of("text", "Pressione o botão para escolher uma das opções"));
        interactive.put("action", action);

        Map<String, Object> request = new HashMap<>();
        request.put("messaging_product", "whatsapp");
        request.put("to", phone);
        request.put("type", "interactive");
        request.put("interactive", interactive);

        sendRequest(request);

    }

    public void sendRequest(Map<String, Object> request){

        RestTemplate restTemplate = new RestTemplate();

        String url = waApiUrl + waPhoneNumberId +"/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(waAcessToken);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
    }

}
