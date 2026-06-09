package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.MessageDTO;
import br.com.espaco_verde.DTO.MessageInteractiveListOption;
import br.com.espaco_verde.DTO.ProductDTO;
import br.com.espaco_verde.entity.*;
import br.com.espaco_verde.repository.ChatRepository;
import br.com.espaco_verde.repository.MessageRepository;
import br.com.espaco_verde.repository.RepositoryPagina;
import br.com.espaco_verde.repository.RepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

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
                    message.setContent(value.at("/messages/0/button/payload").asString());
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

    public String sendRequest(Map<String, Object> request){

        RestTemplate restTemplate = new RestTemplate();

        String url = waApiUrl + waPhoneNumberId +"/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(waAcessToken);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, entity, JsonNode.class);


        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            JsonNode body = response.getBody();
            if(body.has("messages") && body.get("messages").isArray() && !body.get("messages").isEmpty()){
                return response.getBody().get("messages").get(0).get("id").asString();
            }else if(body.has("success") && body.get("success").asBoolean()){
                return null;
            }
        }
        return null;
    }

    public String uploadImage(byte[] imagemBytes){

        RestTemplate restTemplate = new RestTemplate();

        String url = waApiUrl + waPhoneNumberId +"/media";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(waAcessToken);

        MultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
        request.add("messaging_product", "whatsapp");
        request.add("file", new ByteArrayResource(imagemBytes){
            @Override
            public String getFilename(){
                return "qrcode_pix.png";
            }
        });

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, entity, JsonNode.class);
        return response.getBody().get("id").asString();

    }

    public void readMessage(Message message){

        Map<String, Object> request = new HashMap<>();
        request.put("messaging_product", "whatsapp");
        request.put("status", "read");
        request.put("message_id", message.getWamId());

        sendRequest(request);
    }

    public void showTypingIndicator(Message message){

        Map<String, Object> request = new HashMap<>();
        request.put("messaging_product", "whatsapp");
        request.put("message_id", message.getWamId());
        request.put("status", "read");
        request.put("typing_indicator", Map.of("type", "text"));

        sendRequest(request);
    }

    public String sendTextMessage(String phone, String systemResponseText){

        Map<String, Object> request = new HashMap<>();
        request.put("messaging_product", "whatsapp");
        request.put("to", phone);
        request.put("type", "text");
        request.put("text", Map.of("body", systemResponseText));

        return sendRequest(request);
    }

    public String sendButtonMessage(String phone, String systemResponseText, Map<String, String> opt){

        List<Map<String, Object>> buttons = new ArrayList<>();

        for(Map.Entry<String, String> entry: opt.entrySet()){
            buttons.add(Map.of("type", "reply", "reply", Map.of("id", entry.getKey(), "title", entry.getValue())));
        }

        Map<String, Object> request = new HashMap<>();
        request.put("messaging_product", "whatsapp");
        request.put("to", phone);
        request.put("type", "interactive");
        request.put("interactive",
                Map.of("type", "button",
                        "body", Map.of("text", systemResponseText),
                        "action", Map.of("buttons", buttons)
        ));

        return sendRequest(request);

    }

    public String sendCarouselMessage(Chat chat, List<ProductDTO> products, boolean hasNext) {

        List<Map<String, Object>> cards = new ArrayList<>();

        int currentIndex = (chat.getCatalogPage() > 0) ? 1 : 0;

        if(chat.getCatalogPage() > 0){

            List<Map<String, Object>> buttonCard = new ArrayList<>();
            buttonCard.add(Map.of("type", "quick_reply", "quick_reply", Map.of("id", "PREVIOUS_PAGE", "title", "Pagina anterior")));

            cards.add(Map.of("card_index", 0,
                    "type", "cta_url",
                    "header", Map.of("type","image", "image",Map.of("link", localApiUrl+"/img/left-arrow.png")),
                    "body", Map.of("text", "Voltar"),
                    "action", Map.of("buttons", buttonCard)
            ));
        }

        for (ProductDTO p : products) {

            List<Map<String, Object>> buttonCard = new ArrayList<>();
            buttonCard.add(Map.of("type", "quick_reply", "quick_reply", Map.of("id", "ADD_"+p.id(), "title", "Add ao carrinho")));

            cards.add(Map.of("card_index", Integer.toString(currentIndex),
                    "type","cta_url",
                    "header", Map.of("type","image", "image",Map.of("link", localApiUrl+"/produtos/imagem/"+p.imagem())),
                    "body", Map.of("text", p.nome()+"\n\n"+"R$"+p.preco()),
                    "action", Map.of("buttons", buttonCard)
            ));
            currentIndex++;
        }

        if(hasNext){
            List<Map<String, Object>> buttonCard = new ArrayList<>();
            buttonCard.add(Map.of("type", "quick_reply", "quick_reply", Map.of("id", "NEXT_PAGE", "title", "Próxima pagina")));

            cards.add(Map.of("card_index", Integer.toString(currentIndex),
                    "type","cta_url",
                    "header", Map.of("type","image", "image",Map.of("link", localApiUrl+"/img/right-arrow.png")),
                    "body", Map.of("text", "Ainda temos mais coisa para te mostrar!"),
                    "action", Map.of("buttons", buttonCard)
            ));

        }

        Map<String, Object> request = new HashMap<>();
        request.put("messaging_product", "whatsapp");
        request.put("to", chat.getWhatsappNumber());
        request.put("type", "interactive");
        request.put("interactive",
                Map.of("type", "carousel",
                        "body", Map.of("text", "Seguem os produtos disponíveis"),
                        "action", Map.of("cards", cards)
                ));

        return sendRequest(request);
    }

    public String sendInteractiveList(String phone, String systemResponseText, String messageHeader, List<MessageInteractiveListOption> options) {

        List<Map<String, Object>> menu = new ArrayList<>();
        for(MessageInteractiveListOption opt: options){
            Map<String, Object> row = new HashMap<>();
            row.put("id", opt.id());
            row.put("title", opt.title());
            if(opt.description() != null && !opt.description().isEmpty()){
                row.put("description", opt.description());
            }
            menu.add(row);
        }

        Map<String, Object> section = new HashMap<>();
        section.put("title", "Opções do Menu");
        section.put("rows", menu);

        Map<String, Object> action = new HashMap<>();
        action.put("button", "Escolha uma opção");
        action.put("sections", List.of(section));

        Map<String, Object> header = new HashMap<>();
        header.put("type", "text");
        header.put("text", messageHeader);

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

        return sendRequest(request);

    }

    public String sendImagemMessage(String phone, String imageId, String systemResponseText){

        Map<String, Object> request = new HashMap<>();
        request.put("messaging_product", "whatsapp");
        request.put("recipient_type", "individual");
        request.put("to", phone);
        request.put("type", "image");

        Map<String, String> image = new HashMap<>();
        image.put("id", imageId);
        image.put("caption", systemResponseText);
        request.put("image", image);

        return sendRequest(request);

    }

    public void sendUpdateOrderMessage(Order order){

        String phone = order.getCustumer().getPhone();
        StringBuilder sb = new StringBuilder();
        Map<String, String> opt = new LinkedHashMap<>();

        sb.append("Ola *" +order.getCustumer().getName()+"*! Temos uma atualização para o seu pedido\n\n\n");
        sb.append("*Pedido #").append(order.getId()).append("*\n");
        System.out.println(order.getOrderStatus());
        switch (order.getOrderStatus()){
            case AWAITING_PAYMENT:
                sb.append("*O seu pedido foi aceito!*\n\n");
                sb.append("Acesse o menu de pedidos para realizar o pagamento\n");
                opt.put("STATELESS_MY_ORDER", "Ver meus pedidos");
                break;

            case PAID:
                sb.append("*Seu pagamento foi aprovado!*\n\n");
                sb.append("Já estamos preparando o seu pedido e ele logo sairá para entrega\n");
                break;

            case IN_DELIVERY:
                sb.append("*Seu pedido está a caminho!*\n\n");
                sb.append("Logo logo entregaremos os seus produtos\n");
                break;

            case DELIVERED:
                sb.append("*Pedido entregue!*\n\n");
                sb.append("Aproveite seus produtos. Obrigado pela preferência e volte sempre!\n");
                break;

            case CANCELED:
                sb.append("*Cancelado*\n\n");
                sb.append("Infelizmente seu pedido não foi aceito. Caso tenha dúvidas, estamos à disposição.\n");
                break;

        }

        opt.put("STATELESS_BACK_TO_MAIN_MENU", "Voltar ao menu");
        String wamId = sendButtonMessage(phone, sb.toString(), opt);

        Chat chat = chatRepository.findByWhatsappNumber(phone).orElseThrow();

        Message systemMessage = new Message();
        systemMessage.setChat(chat);
        systemMessage.setSenderType(SenderType.SYSTEM);
        systemMessage.setContent(sb.toString());
        systemMessage.setPhone(phone);
        systemMessage.setTimestamp(LocalDateTime.now());
        systemMessage.setWamId(wamId);
        messageRepository.save(systemMessage);
        MessageDTO messageDTO = new MessageDTO(systemMessage);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chat.getId(), messageDTO);
    }

}
