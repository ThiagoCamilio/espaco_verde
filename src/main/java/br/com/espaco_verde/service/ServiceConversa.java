package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.ProductDTO;
import br.com.espaco_verde.entity.*;
import br.com.espaco_verde.repository.MessageRepository;
import br.com.espaco_verde.repository.RepositoryConversa;
import br.com.espaco_verde.repository.RepositoryPagina;
import br.com.espaco_verde.repository.RepositoryUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceConversa {

    @Value("${wa.api.phone.number}")
    private String waPhoneNumber;

    @Autowired
    private RepositoryConversa repositoryConversa;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ServiceProduto serviceProduto;

    @Autowired
    private RepositoryPagina repositoryPagina;

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SystemMessageService systemMessageService;

    @Transactional
    public void processarMensagem(Message message) throws Exception {

        Chat chat = getChat(message);
        message.setChat(chat);
        chat.refreshInteraction();
        System.out.println(message.getContent());
        messageRepository.save(message);
        messageService.readMessage(message);

        String systemResponseText = "";

        switch (chat.getChatState()){
            case NEW_CONTACT:
                systemResponseText = systemMessageService.getMessage("NEW_CONTACT", Map.of("nome", message.getSenderName()));
                messageService.sendTextMessage(message, systemResponseText);
                chat.setChatState(ChatState.GREETINGS);
                break;

            case GREETINGS, STAND_BY:
                handlerMainMenu(chat, message);
                break;

            case AWAITING_MENU_RESPONSE:
                handlerAwaitingMenuResponse(chat, message);
                break;

            /*case "checkout":
                retorno = "Ainda não disponivel";
                messageService.sendTextMessage(mensagem, retorno);
                break;
            */
            default:
                //chat.setEstadoConversa(EstadoConversa.MENU_PRINCIPAL);
                //messageService.sendButtonMessage(mensagem);
                break;
        }

    }

    private Chat getChat(Message message){
        if (repositoryConversa.existsByWhatsappNumber(message.getPhone())){
            return repositoryConversa.findByWhatsappNumber(message.getPhone());
        }else{
            Chat chat = new Chat(message.getPhone(), ChatState.NEW_CONTACT);
            repositoryUser.findByPhone(message.getPhone()).ifPresent(chat::setUser);
            return repositoryConversa.save(chat);
        }
    }

    @Transactional
    private void handlerMainMenu(Chat chat, Message message){
        Map<String, String> var = Map.of("nome", chat.getUser().getName());
        String systemResponseText = systemMessageService.getMessage("GREETINGS", var);
        Map<String, String> opt = new LinkedHashMap<>();
        opt.put("OPT_CATALOGO", "Ver Catálogo");
        opt.put("OPT_ORDERS", "Meus Pedidos");
        opt.put("OPT_CART", "Carrinho");
        opt.put("OPT_STAFF", "Falar com Atendente");

        messageService.sendInteractiveList(chat.getWhatsappNumber(), systemResponseText, "Escolha uma opção", opt);

        Message systemMessage = new Message();
        systemMessage.setChat(chat);
        systemMessage.setSenderType(SenderType.SYSTEM);
        systemMessage.setContent(systemResponseText);
        systemMessage.setPhone(waPhoneNumber);
        systemMessage.setTimestamp(LocalDateTime.now());
        systemMessage.setWamId(message.getWamId());
        messageRepository.save(systemMessage);

        chat.setChatState(ChatState.AWAITING_MENU_RESPONSE);
        repositoryConversa.save(chat);
    }

    private void handlerAwaitingMenuResponse(Chat chat, Message message) {

        String userResponse = message.getContent();

        switch (userResponse){

            case "OPT_CATALOGO":
                chat.setChatState(ChatState.CATALOG);
                break;

            case "OPT_ORDERS":
                chat.setChatState(ChatState.ORDERS);
                break;

            case "OPT_CART":
                chat.setChatState(ChatState.CART);
                break;

            case "OPT_STAFF":
                chat.setChatState(ChatState.STAFF);
                break;

            default:
                break;
        }

    }
}
