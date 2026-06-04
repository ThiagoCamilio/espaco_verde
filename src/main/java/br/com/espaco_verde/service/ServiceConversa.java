package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.ProductDTO;
import br.com.espaco_verde.entity.*;
import br.com.espaco_verde.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private RepositoryProduto repositoryProduct;

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

            case AWAITING_CATALOG_RESPONSE:
                handlerAwaitingCatalogResponse(chat, message);
                break;

            case AWAITING_ADD_CART_RESPONSE:
                handlerAwaitingAddCartResponse(chat, message);
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
        Map<String, String> var = Map.of("nome", message.getSenderName());
        String systemResponseText = systemMessageService.getMessage("GREETINGS", var);
        Map<String, String> opt = new LinkedHashMap<>();
        opt.put("OPT_CATALOGO", "Ver Catálogo");
        opt.put("OPT_ORDERS", "Meus Pedidos");
        opt.put("OPT_CART", "Cart");
        opt.put("OPT_STAFF", "Falar com Atendente");

        messageService.sendInteractiveList(chat.getWhatsappNumber(), systemResponseText, opt);

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
                handlerCatalog(chat, message);
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

    private void handlerCatalog(Chat chat, Message message){

        int currentPage = chat.getCatalogPage();
        int pageSize = 3;

        Pageable pageRequest = PageRequest.of(currentPage, pageSize);
        Page<Product> pageProducts = repositoryProduct.findActive(pageRequest);

        if(pageProducts.isEmpty() && currentPage > 0){
            chat.setCatalogPage(0);
            handlerCatalog(chat, message);
            return;
        }

        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productsDTOS = new ArrayList<>();
        for(Product p : products){
            ProductDTO dto = new ProductDTO(p);
            productsDTOS.add(dto);
        }

        boolean hasNextPage = pageProducts.hasNext();

        messageService.sendCarouselMessage(chat, productsDTOS, hasNextPage);

        chat.setChatState(ChatState.AWAITING_CATALOG_RESPONSE);
        repositoryConversa.save(chat);

    }

    private void handlerAwaitingCatalogResponse(Chat chat, Message message) {

        String userResponse = message.getContent();

        if(userResponse.equals("NEXT_PAGE")){
            chat.setCatalogPage(chat.getCatalogPage() + 1);
            repositoryConversa.save(chat);
            handlerCatalog(chat, message);
        } else if (userResponse.equals("PREVIOUS_PAGE")) {
            chat.setCatalogPage(chat.getCatalogPage() - 1);
            repositoryConversa.save(chat);
            handlerCatalog(chat, message);
        } else if(userResponse.startsWith("ADD_")){
            int productId = Integer.parseInt(userResponse.replace("ADD_", ""));
            //cart service para salvar a planta no carrinho
            handlerAddCart(chat);
        }else{
            messageService.sendTextMessage(message, "Por favor, utilize os botões do menu nos cards");
            handlerCatalog(chat, message);
        }

    }

    private void handlerAddCart(Chat chat) {

        String systemResponseText = systemMessageService.getMessage("ADD_PRODUCT_CART", null);
        Map<String, String> opt = new LinkedHashMap<>();
        opt.put("BACK_TO_CATALOG", "Voltar ao catalogo");
        opt.put("CART", "Ver carrinho");

        messageService.sendButtonMessage(chat.getWhatsappNumber(), systemResponseText, opt);
        chat.setChatState(ChatState.AWAITING_ADD_CART_RESPONSE);
        repositoryConversa.save(chat);

    }

    private void handlerAwaitingAddCartResponse(Chat chat, Message message){

        String userResponse = message.getContent();
        System.out.println("______________");
        System.out.println(userResponse);

        if(userResponse.equals("BACK_TO_CATALOG")){
            handlerCatalog(chat, message);

        } else if (userResponse.equals("CART")) {
            handlerCart(chat, message);
        }else{
            messageService.sendTextMessage(message, "Por favor, utilize os botões do menu");
        }

    }

    private void handlerCart(Chat chat, Message message) {
    }

}
