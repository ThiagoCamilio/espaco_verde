package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.OrderResponseDTO;
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
import org.springframework.util.StringUtils;

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

    @Autowired
    private CartService cartService;

    @Autowired
    private ServiceOrder serviceOrder;

    @Transactional
    public void processarMensagem(Message message) throws Exception {

        Chat chat = getChat(message);
        message.setChat(chat);
        chat.refreshInteraction();
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

            case AWAITING_CART_RESPONSE:
                handlerAwaitingCartResponse(chat, message);
                break;

            case AWAITING_CHECKOUT_DELIVERY_RESPONSE:
                handlerAwaitingCheckoutDeliveryResponse(chat, message);
                break;

            case AWAITING_DELIVERY_INPUT_RESPONSE:
                handlerCheckoutConfirmation(chat, message);
                break;

            case AWAITING_CHECKOUT_CONFIRMATION_RESPONSE:
                handlerAwaitingCheckoutConfirmationResponse(chat, message);
                break;

            case AWAITING_ORDER_RESPONSE:
                handlerAwaitingOrderResponse(chat, message);
                break;

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

    private void handlerWrongAnswer(Chat chat, Message message){
        String systemResponse = systemMessageService.getMessage("WRONG_ANSWER", null);
        messageService.sendTextMessage(message, systemResponse);
        repositoryConversa.save(chat);

    }

    @Transactional
    private void handlerMainMenu(Chat chat, Message message){
        Map<String, String> var = Map.of("nome", chat.getUser().getName());
        String systemResponseText = systemMessageService.getMessage("GREETINGS", var);
        Map<String, String> opt = new LinkedHashMap<>();
        opt.put("OPT_CATALOGO", "Ver Catálogo");
        opt.put("OPT_ORDERS", "Meus Pedidos");
        opt.put("OPT_CART", "Carrinho de compras");
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
                handlerCatalog(chat, message);
                break;
            case "OPT_ORDERS":
                break;
            case "OPT_CART":
                handlerCart(chat, message);
                break;
            case "OPT_STAFF":
                break;
            default:
                handlerWrongAnswer(chat, message);
                handlerMainMenu(chat, message);
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
            cartService.addProduct(chat.getUser().getId(), productId, 1);
            handlerAddCart(chat);
        }else{
            handlerWrongAnswer(chat, message);
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
        if(userResponse.equals("BACK_TO_CATALOG")){
            handlerCatalog(chat, message);

        } else if (userResponse.equals("CART")) {
            handlerCart(chat, message);
        }else{
            handlerWrongAnswer(chat, message);
            handlerAddCart(chat);
        }

    }

    private void handlerCart(Chat chat, Message message) {

        Cart cart = cartService.getCart(chat.getUser().getId());
        if(cart.getProductCarts() == null || cart.getProductCarts().isEmpty()){
            String systemResponseText = systemMessageService.getMessage("EMPTY_CART", null);
            Map<String, String> opt = new LinkedHashMap<>();
            opt.put("BACK_TO_CATALOG", "Ir para o catalogo");
            opt.put("BACK_TO_MAIN_MENU", "Voltar ao menu");
            messageService.sendButtonMessage(chat.getWhatsappNumber(), systemResponseText, opt);

        }else{
            StringBuilder sb = new StringBuilder();
            sb.append("*Seu Carrinho de Compras*\n\n");

            for(ProductCart productCart : cart.getProductCarts()){
                sb.append(productCart.getQuantity()).append("x *").append(productCart.getProduct().getNome()).append("*\n");
                sb.append("Preço unit.: R$").append(productCart.getSellPrice()).append("\n");
                sb.append("_Subtotal: R$").append(productCart.getTotal()).append("_\n\n");
            }

            sb.append("-----------------------------------\n");
            sb.append("*Total da Compra: R$ ").append(cart.getTotal()).append("*\n");
            sb.append("-----------------------------------\n");
            sb.append("Como deseja prosseguir?");

            Map<String, String> opt = new LinkedHashMap<>();
            opt.put("TO_CHECK_OUT", "Finalizar compra");
            opt.put("CLEAR_CART", "Limpar Carrinho");
            opt.put("BACK_TO_CATALOG", "Voltar ao catalogo");

            messageService.sendButtonMessage(chat.getWhatsappNumber(), sb.toString(), opt);
        }

        chat.setChatState(ChatState.AWAITING_CART_RESPONSE);
        repositoryConversa.save(chat);

    }

    private void handlerAwaitingCartResponse(Chat chat, Message message){
        String userResponse = message.getContent();

        switch (userResponse){
            case "CLEAR_CART":
                cartService.clearCart(chat.getUser().getId());
                handlerCart(chat, message);
                break;
            case "TO_CHECK_OUT":
                handlerCheckoutDelivery(chat, message);
                break;
            case "BACK_TO_CATALOG":
                handlerCatalog(chat, message);
                break;
            case "BACK_TO_MAIN_MENU":
                handlerMainMenu(chat, message);
                break;
            default:
                handlerWrongAnswer(chat, message);
                handlerCart(chat, message);
                break;
        }
    }

    private void handlerCheckoutDelivery(Chat chat, Message message){
        String systemResponseText = systemMessageService.getMessage("CHECKOUT_DELIVERY", null);
        Map<String, String> opt = new LinkedHashMap<>();
        opt.put("DELIVERY", "Entrega");
        opt.put("PICKUP", "Retirada");
        messageService.sendButtonMessage(chat.getWhatsappNumber(), systemResponseText, opt);
        chat.setChatState(ChatState.AWAITING_CHECKOUT_DELIVERY_RESPONSE);
        repositoryConversa.save(chat);

    }

    private void handlerAwaitingCheckoutDeliveryResponse(Chat chat, Message message){
        String userResponse = message.getContent();
        switch (userResponse){
            case "DELIVERY":
                handlerDeliveryAddressInput(chat, message);
                break;

            case "PICKUP":
                handlerCheckoutConfirmation(chat, message);
                break;

            default:
                handlerWrongAnswer(chat, message);
                handlerCheckoutDelivery(chat, message);
                break;
        }
    }

    private void handlerDeliveryAddressInput(Chat chat, Message message) {
        String systemResponseText = systemMessageService.getMessage("DELIVERY_ADDRESS_INPUT", null);
        messageService.sendTextMessage(message, systemResponseText);
        chat.setChatState(ChatState.AWAITING_DELIVERY_INPUT_RESPONSE);
        repositoryConversa.save(chat);
    }

    private void handlerCheckoutConfirmation(Chat chat, Message message){

        Cart cart = cartService.getCart(chat.getUser().getId());
        String deliveryAddress = message.getContent().trim();

        if(deliveryAddress.equals("PICKUP")){
            deliveryAddress = "";
        }

        chat.setDeliveryAddress(deliveryAddress);

        StringBuilder sb = new StringBuilder();

        sb.append("*Revisão do pedido*\n\n");
        sb.append("Antes de prosseguirmos, vamos revisar as informações do seu pedido:\n\n");

        sb.append("Seus items:\n");
        for(ProductCart productCart : cart.getProductCarts()){
            sb.append(productCart.getQuantity()).append("x *").append(productCart.getProduct().getNome()).append("*\n");
            sb.append("Preço unit.: R$").append(productCart.getSellPrice()).append("\n");
            sb.append("_Subtotal: R$").append(productCart.getTotal()).append("_\n");
        }
        sb.append("-----------------------------------\n");
        if(StringUtils.hasText(deliveryAddress)){
            sb.append("Endereço de entrega:\n").append(deliveryAddress).append("\n");
        }else{
            sb.append("Retirada\n");
        }
        sb.append("-----------------------------------\n");
        sb.append("*Total da Compra: R$ ").append(cart.getTotal()).append("*\n");
        sb.append("-----------------------------------\n\n");

        sb.append("Podemos confirmar seu pedido?");

        Map<String, String> opt = new LinkedHashMap<>();
        opt.put("CONFIRM_ORDER", "Confirmar pedido");
        opt.put("EDIT_ADDRESS", "Editar endereço");
        opt.put("BACK_TO_CART", "Voltar ao carrinho");

        messageService.sendButtonMessage(chat.getWhatsappNumber(), sb.toString(), opt);

        chat.setChatState(ChatState.AWAITING_CHECKOUT_CONFIRMATION_RESPONSE);
        repositoryConversa.save(chat);

    }

    private void handlerAwaitingCheckoutConfirmationResponse(Chat chat, Message message){
        String userResponse = message.getContent();
        switch (userResponse){
            case "CONFIRM_ORDER":
                handlerOrder(chat, message);
                break;

            case "EDIT_ADDRESS":
                handlerCheckoutDelivery(chat, message);
                break;

            case "BACK_TO_CART":
                handlerCart(chat, message);
                break;

            case "BACK_TO_MAIN_MENU":
                handlerMainMenu(chat, message);
                break;

            default:
                handlerWrongAnswer(chat, message);
                handlerCheckoutConfirmation(chat, message);
                break;
        }
    }

    private void handlerOrder(Chat chat, Message message){
        System.out.println(chat.getDeliveryAddress());

        OrderResponseDTO orderResponse =  serviceOrder.createOrder(chat.getDeliveryAddress(), chat.getUser().getId());

        String systemResponse =
                "Pedido Recebido!\n" +
                "Seu pedido é o numero: #"+ orderResponse.id() + "\n\n" +
                "*O que acontece agora?*\n" +
                "\n" +
                "Sua lista de produtos foi encaminhada para a nossa equipe técnica.\n Faremos uma rápida *análise de " +
                "viabilidade logística* para garantir a melhor forma de entregar o seu pedido.\n" +
                "\n" +
                "Assim que seu pedido for aprovado, você receberá um aviso com as instruções para realizar o pagamento.";

        Map<String, String> opt = new LinkedHashMap<>();
        opt.put("MY_ORDERS", "Ver meus pedido");
        opt.put("BACK_TO_MAIN_MENU", "Voltar ao menu");

        messageService.sendButtonMessage(chat.getWhatsappNumber(), systemResponse, opt);
        chat.setDeliveryAddress(null);
        chat.setChatState(ChatState.AWAITING_ORDER_RESPONSE);
        repositoryConversa.save(chat);

    }

    private void handlerAwaitingOrderResponse(Chat chat, Message message) {
        String userResponse = message.getContent();
        switch (userResponse){
            case "MY_ORDERS":
                handlerMyOrders(chat, message);
                break;

            case "BACK_TO_MAIN_MENU":
                handlerMainMenu(chat, message);
                break;

            default:
                handlerWrongAnswer(chat, message);
                handlerCheckoutConfirmation(chat, message);
                break;
        }
    }

    private void handlerMyOrders(Chat chat, Message message) {



    }

}
