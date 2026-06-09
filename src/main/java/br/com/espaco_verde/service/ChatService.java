package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.*;
import br.com.espaco_verde.entity.*;
import br.com.espaco_verde.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Value("${wa.api.phone.number}")
    private String waPhoneNumber;

    @Value("${local.url}")
    private String localUrl;


    @Autowired
    private ChatRepository chatRepository;

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

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public void processMessage(Message message) throws Exception {

        Chat chat = getChat(message);
        message.setChat(chat);
        chat.refreshInteraction();
        messageRepository.save(message);
        messageService.readMessage(message);
        String userResponse = message.getContent();
        MessageDTO messageDTO = new MessageDTO(message);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chat.getId(), messageDTO);

        if(chat.getChatState() == ChatState.IN_HUMAN_ATTENDANCE){
            return;
        }
        messageService.showTypingIndicator(message);

        if(userResponse != null && userResponse.startsWith("STATELESS")){
            handlerStatelessMessage(chat, message);
            return;
        }

        switch (chat.getChatState()){
            case NEW_CONTACT:
                handlerNewContact(chat, message);
                break;

            case AWAITING_NEW_CONTACT_INPUT:
                handlerAwaitingNewContactInput(chat, message);
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
                handlerAwaitingDeliveryAddressInput(chat, message);
                break;

            case AWAITING_CHECKOUT_CONFIRMATION_RESPONSE:
                handlerAwaitingCheckoutConfirmationResponse(chat, message);
                break;

            case AWAITING_ORDER_RESPONSE:
                handlerAwaitingOrderResponse(chat, message);
                break;

            case AWAITING_MY_ORDERS_RESPONSE:
                handlerAwaitingMyOrdersResponse(chat, message);
                break;

            case AWAITING_ORDER_DETAILS_RESPONSE:
                handlerAwaitingOrderDetailsResponse(chat, message);
                break;

            case AWAITING_PAYMENT_RESPONSE:
                handlerAwaitingPaymentResponse(chat, message);
                break;

            case AWAITING_REGISTRATION_COMPLETION:
                handlerAwaitingRegistrationCompletion(chat, message);
                break;

            default:
                handlerMainMenu(chat, message);
                break;
        }
    }

    public List<ChatDTO> listAttendanceQueue(){
        List<Chat> chats = chatRepository.findAll();
        return chats.stream()
                .map(ChatDTO::new)
                .collect(Collectors.toList());
    }

    public List<MessageDTO> fetchMessageHistory(Long chatId){
        List<Message> messages = messageRepository.findByChatIdOrderByTimestampAsc(chatId);
        return messages.stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendAttendantMessage(Long chatId, String messageText){
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        String wamId = messageService.sendTextMessage(chat.getWhatsappNumber(), messageText);
        saveMessage(chat, wamId, messageText);
    }

    @Transactional
    public void closeAttendance(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();

        chat.setChatState(ChatState.STAND_BY);
        chatRepository.save(chat);

        String messageText = "Atendimento humano encerrado. Obrigado por entrar em contato com o Espaço Verde!\n\n" +
                "Para novas solicitações, basta digitar 'Oi'.";
        String wamId = messageService.sendTextMessage(chat.getWhatsappNumber(), messageText);
        saveMessage(chat, wamId, messageText );
    }

    private Chat getChat(Message message){

        Optional<Chat> optionalChat = chatRepository.findByWhatsappNumber(message.getPhone());
        if (optionalChat.isPresent()){
            return optionalChat.get();
        }else{
            Chat chat = new Chat(message.getPhone(), ChatState.NEW_CONTACT);
            Optional<User> optionalUser = repositoryUser.findByPhone(message.getPhone());
            if(optionalUser.isPresent()){
                chat.setUser(optionalUser.get());
            }else {
                User newUser = new User();
                newUser.setPhone(message.getPhone());
                repositoryUser.save(newUser);
                chat.setUser(newUser);
            }
            return chatRepository.save(chat);
        }
    }

    @Transactional
    private void saveMessage(Chat chat, String wamId, String systemResponseText){
        Message systemMessage = new Message();
        systemMessage.setChat(chat);
        systemMessage.setSenderType(SenderType.SYSTEM);
        systemMessage.setContent(systemResponseText);
        systemMessage.setPhone(waPhoneNumber);
        systemMessage.setTimestamp(LocalDateTime.now());
        systemMessage.setWamId(wamId);
        messageRepository.save(systemMessage);
        MessageDTO messageDTO = new MessageDTO(systemMessage);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chat.getId(), messageDTO);
    }

    private void handlerStatelessMessage(Chat chat, Message message){
        String userResponse = message.getContent();

        if(userResponse.equals("STATELESS_BACK_TO_MAIN_MENU")){
            handlerMainMenu(chat, message);
            return;
        } else if (userResponse.equals("STATELESS_MY_ORDER")) {
            handlerMyOrders(chat, message);
            return;
        }
    }

    private void handlerWrongAnswer(Chat chat, Message message){
        String systemResponseText = systemMessageService.getMessage("WRONG_ANSWER", null);
        String wamId = messageService.sendTextMessage(chat.getWhatsappNumber(), systemResponseText);
        saveMessage(chat, wamId, systemResponseText);
        chatRepository.save(chat);

    }

    private void handlerNewContact(Chat chat, Message message) {
        String systemResponseText = systemMessageService.getMessage("NEW_CONTACT", null);
        String wamId = messageService.sendTextMessage(chat.getWhatsappNumber(), systemResponseText);
        saveMessage(chat, wamId, systemResponseText);
        chat.setChatState(ChatState.AWAITING_NEW_CONTACT_INPUT);
        chatRepository.save(chat);
    }

    private void handlerAwaitingNewContactInput(Chat chat, Message message) {
        String userResponse = message.getContent();
        User user = chat.getUser();
        user.setName(userResponse);
        String systemResponseText = systemMessageService.getMessage("NEW_USER", null);
        String wamId = messageService.sendTextMessage(chat.getWhatsappNumber(), systemResponseText);
        saveMessage(chat, wamId, systemResponseText);
        handlerMainMenu(chat, message);
        chatRepository.save(chat);
    }

    @Transactional
    private void handlerMainMenu(Chat chat, Message message){
        Map<String, String> var = Map.of("nome", chat.getUser().getName());
        String systemResponseText = systemMessageService.getMessage("GREETINGS", var);
        List<MessageInteractiveListOption> options = new ArrayList<>();
        MessageInteractiveListOption catalog = new MessageInteractiveListOption("OPT_CATALOGO", "Ver Catálogo", null);
        MessageInteractiveListOption orders = new MessageInteractiveListOption("OPT_ORDERS", "Meus Pedidos", null);
        MessageInteractiveListOption cart = new MessageInteractiveListOption("OPT_CART", "Carrinho de compras", null);
        MessageInteractiveListOption staff = new MessageInteractiveListOption("OPT_STAFF", "Falar com Atendente", null);

        options.add(catalog);
        options.add(orders);
        options.add(cart);
        options.add(staff);

        String messageHeader = "Bem vindo(a) à Espaço Verde";
        String wamId = messageService.sendInteractiveList(chat.getWhatsappNumber(), systemResponseText, messageHeader,options);
        saveMessage(chat, wamId, systemResponseText);
        chat.setChatState(ChatState.AWAITING_MENU_RESPONSE);
        chatRepository.save(chat);
    }

    private void handlerAwaitingMenuResponse(Chat chat, Message message) {

        String userResponse = message.getContent();

        switch (userResponse){
            case "OPT_CATALOGO":
                handlerCatalog(chat, message);
                break;
            case "OPT_ORDERS":
                handlerMyOrders(chat, message);
                break;
            case "OPT_CART":
                handlerCart(chat, message);
                break;
            case "OPT_STAFF":
                handlerHumanAttendance(chat, message);
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

        String wamId = messageService.sendCarouselMessage(chat, productsDTOS, hasNextPage);
        saveMessage(chat, wamId, "Catalogo");
        chat.setChatState(ChatState.AWAITING_CATALOG_RESPONSE);
        chatRepository.save(chat);

    }

    private void handlerAwaitingCatalogResponse(Chat chat, Message message) {

        String userResponse = message.getContent();

        if(userResponse.equals("NEXT_PAGE")){
            chat.setCatalogPage(chat.getCatalogPage() + 1);
            chatRepository.save(chat);
            handlerCatalog(chat, message);
        } else if (userResponse.equals("PREVIOUS_PAGE")) {
            chat.setCatalogPage(chat.getCatalogPage() - 1);
            chatRepository.save(chat);
            handlerCatalog(chat, message);
        } else if(userResponse.startsWith("ADD_")){
            int productId = Integer.parseInt(userResponse.replace("ADD_", ""));
            cartService.addProduct(chat.getUser().getId(), productId, 1);
            handlerAddCart(chat, message);
        }else{
            handlerWrongAnswer(chat, message);
            handlerCatalog(chat, message);
        }

    }

    private void handlerAddCart(Chat chat, Message message) {

        String systemResponseText = systemMessageService.getMessage("ADD_PRODUCT_CART", null);
        Map<String, String> opt = new LinkedHashMap<>();
        opt.put("BACK_TO_CATALOG", "Voltar ao catalogo");
        opt.put("CART", "Ver carrinho");

        String wamId = messageService.sendButtonMessage(chat.getWhatsappNumber(), systemResponseText, opt);
        saveMessage(chat, wamId, systemResponseText);
        chat.setChatState(ChatState.AWAITING_ADD_CART_RESPONSE);
        chatRepository.save(chat);

    }

    private void handlerAwaitingAddCartResponse(Chat chat, Message message){

        String userResponse = message.getContent();
        if(userResponse.equals("BACK_TO_CATALOG")){
            handlerCatalog(chat, message);
            return;
        } else if (userResponse.equals("CART")) {
            handlerCart(chat, message);
            return;
        }else{
            handlerWrongAnswer(chat, message);
            handlerAddCart(chat, message);
            return;
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

            String wamId = messageService.sendButtonMessage(chat.getWhatsappNumber(), sb.toString(), opt);
            saveMessage(chat, wamId, sb.toString());
        }

        chat.setChatState(ChatState.AWAITING_CART_RESPONSE);
        chatRepository.save(chat);

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

        User user = chat.getUser();
        if(!user.isProfileComplete()){
            String encodedUserName = URLEncoder.encode(user.getName(), StandardCharsets.UTF_8);
            String registerLink = localUrl+"/register?phone="+user.getPhone()+"&name="+encodedUserName;
            String systemResponseText = "Quase lá, " + user.getName() + "! \n\n"
                    + "Para finalizarmos o seu pedido, precisamos de alguns dados que ainda não temos.\n\n"
                    + "É bem rápido! Conclua o seu cadastro no nosso ambiente seguro clicando no link abaixo:\n"
                    + registerLink + "\n\n"
                    + "Assim que terminar, digite *PRONTO* aqui e nós finalizamos o seu pedido!";
            String wamId = messageService.sendTextMessage(chat.getWhatsappNumber(), systemResponseText);
            saveMessage(chat, wamId, systemResponseText);
            chat.setChatState(ChatState.AWAITING_REGISTRATION_COMPLETION);
            chatRepository.save(chat);
            return;
        }

        String systemResponseText = systemMessageService.getMessage("CHECKOUT_DELIVERY", null);
        Map<String, String> opt = new LinkedHashMap<>();
        opt.put("DELIVERY", "Entrega");
        opt.put("PICKUP", "Retirada");
        String wamId = messageService.sendButtonMessage(chat.getWhatsappNumber(), systemResponseText, opt);
        saveMessage(chat, wamId, systemResponseText);
        chat.setChatState(ChatState.AWAITING_CHECKOUT_DELIVERY_RESPONSE);
        chatRepository.save(chat);

    }

    private void handlerAwaitingRegistrationCompletion(Chat chat, Message message) {

        User user = repositoryUser.findById(chat.getUser().getId()).orElseThrow();
        if(user.isProfileComplete()){
            String systemResponseText = systemMessageService.getMessage("USER_REGISTER", null);
            String wamId = messageService.sendTextMessage(chat.getWhatsappNumber(), systemResponseText);
            saveMessage(chat, wamId, systemResponseText);
            handlerCheckoutDelivery(chat, message);
            return;
        }else{
            String encodedUserName = URLEncoder.encode(user.getName(), StandardCharsets.UTF_8);
            String registerLink = localUrl+"/register?phone="+user.getPhone()+"&name="+encodedUserName;
            String systemResponseText = systemMessageService.getMessage("USER_DIDNT_REGISTER", null);
            String wamId = messageService.sendTextMessage(chat.getWhatsappNumber(), systemResponseText + registerLink);
            saveMessage(chat, wamId, systemResponseText);
            return;
        }
    }

    private void handlerAwaitingCheckoutDeliveryResponse(Chat chat, Message message){
        String userResponse = message.getContent();
        switch (userResponse){
            case "DELIVERY":
                handlerDeliveryAddressInput(chat, message);
                break;

            case "PICKUP":
                chat.setDeliveryAddress("");
                chatRepository.save(chat);
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
        String wamId =  messageService.sendTextMessage(chat.getWhatsappNumber(), systemResponseText);
        saveMessage(chat, wamId, systemResponseText);
        chat.setChatState(ChatState.AWAITING_DELIVERY_INPUT_RESPONSE);
        chatRepository.save(chat);
    }

    private void handlerAwaitingDeliveryAddressInput(Chat chat, Message message){
        String deliveryAddress = message.getContent().trim();
        chat.setDeliveryAddress(deliveryAddress);
        chatRepository.save(chat);
        handlerCheckoutConfirmation(chat, message);
    }

    private void handlerCheckoutConfirmation(Chat chat, Message message){

        Cart cart = cartService.getCart(chat.getUser().getId());
        String deliveryAddress = chat.getDeliveryAddress();

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

        String wamId = messageService.sendButtonMessage(chat.getWhatsappNumber(), sb.toString(), opt);
        saveMessage(chat, wamId, sb.toString());

        chat.setChatState(ChatState.AWAITING_CHECKOUT_CONFIRMATION_RESPONSE);
        chatRepository.save(chat);

    }

    private void handlerAwaitingCheckoutConfirmationResponse(Chat chat, Message message){
        String userResponse = message.getContent();
        switch (userResponse){
            case "CONFIRM_ORDER":
                OrderResponseDTO orderResponse = serviceOrder.createOrder(chat.getDeliveryAddress(), chat.getUser().getId());
                chat.setCurrentOrderId(orderResponse.id());
                handlerCreateOrder(chat, message);
                chatRepository.save(chat);
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

    private void handlerCreateOrder(Chat chat, Message message){

        System.out.println(chat.getCurrentOrderId());

        String systemResponseText =
                "Pedido Recebido!\n" +
                "Seu pedido é o numero: #"+ chat.getCurrentOrderId() + "\n\n" +
                "*O que acontece agora?*\n" +
                "\n" +
                "Sua lista de produtos foi encaminhada para a nossa equipe técnica.\nFaremos uma rápida *análise de " +
                "viabilidade logística* para garantir a melhor forma de entregar o seu pedido.\n" +
                "\n" +
                "Assim que seu pedido for aprovado, você receberá um aviso com as instruções para realizar o pagamento.";

        Map<String, String> opt = new LinkedHashMap<>();
        opt.put("MY_ORDERS", "Ver meus pedido");
        opt.put("BACK_TO_MAIN_MENU", "Voltar ao menu");

        String wamId = messageService.sendButtonMessage(chat.getWhatsappNumber(), systemResponseText, opt);
        saveMessage(chat, wamId, systemResponseText);
        chat.setDeliveryAddress(null);
        chat.setChatState(ChatState.AWAITING_ORDER_RESPONSE);
        chatRepository.save(chat);

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
                handlerCreateOrder(chat, message);
                break;
        }
    }

    private void handlerMyOrders(Chat chat, Message message) {

        List<OrderResponseDTO> activeOrders = serviceOrder.getActiveUserOrders(chat.getUser().getId());
        List<MessageInteractiveListOption> opt = new ArrayList<>();
        for(OrderResponseDTO orderResponseDTO : activeOrders){
            String optId = "ORDER_" + orderResponseDTO.id();
            String title = "Pedido #" + orderResponseDTO.id();
            String desc = "Total: R$ " + orderResponseDTO.totalPrice() + " - " +
                    orderResponseDTO.orderStatus() + " - " +
                    orderResponseDTO.createdAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            opt.add(new MessageInteractiveListOption(optId, title, desc));
        }

        String messageHeader = "Pedidos";

        String systemResponseText = "Aqui estão os seus pedidos ativos.";
        String wamId = messageService.sendInteractiveList(
                chat.getWhatsappNumber(), systemResponseText, messageHeader, opt
        );
        saveMessage(chat, wamId, systemResponseText);
        chat.setChatState(ChatState.AWAITING_MY_ORDERS_RESPONSE);
        chatRepository.save(chat);

    }

    private void handlerAwaitingMyOrdersResponse(Chat chat, Message message){
        String userResponse = message.getContent();
        if(userResponse.startsWith("ORDER")) {
            int orderId = Integer.parseInt(userResponse.replace("ORDER_", ""));
            chat.setCurrentOrderId(orderId);
            chatRepository.save(chat);
            handlerOrderDetails(chat, message);
        }else {
            handlerWrongAnswer(chat, message);
            handlerMyOrders(chat, message);
        }
    }

    private void handlerOrderDetails(Chat chat, Message message){

        int orderId = chat.getCurrentOrderId();
        OrderResponseDTO order = serviceOrder.getOrderById(orderId);
        StringBuilder sb = new StringBuilder();
        sb.append("*Detalhes do pedido #").append(order.id()).append(":*\n\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
        sb.append("*Data:* ").append(order.createdAt().format(formatter)).append("\n");
        sb.append("*Status:* ").append(order.orderStatus()).append("\n");
        sb.append("*Produtos:*\n");
        for (OrderItemResponseDTO item : order.items()) {
            sb.append(item.quantity()).append("x ").append(item.productName()).append("\n");
        }
        sb.append("\n");

        if(order.deliveryMethod().equals("PICKUP")){
            sb.append("Retirada\n");
        }else{
            sb.append("Endereço de entrega:\n").append(order.deliveryAdress()).append("\n");
        }
        sb.append("-----------------------------------\n");
        sb.append("*Total da Compra: R$ ").append(order.totalPrice()).append("*\n");
        sb.append("-----------------------------------\n\n");

        Map<String, String> opt = new LinkedHashMap<>();
        if(order.orderStatus().equals("Aguardando Pagamento")){
            opt.put("PAYMENT_"+orderId, "Ir para o pagamento");
        }

        opt.put("BACK_TO_MY_ORDERS", "Voltar aos pedidos");

        String wamId = messageService.sendButtonMessage(chat.getWhatsappNumber(), sb.toString(), opt);
        saveMessage(chat, wamId, sb.toString());
        chat.setChatState(ChatState.AWAITING_ORDER_DETAILS_RESPONSE);
        chatRepository.save(chat);

    }

    private void handlerAwaitingOrderDetailsResponse(Chat chat, Message message){
        String userResponse = message.getContent();

        if(userResponse.startsWith("PAYMENT")){
            int orderId = Integer.parseInt(userResponse.replace("PAYMENT_", ""));
            chat.setCurrentOrderId(orderId);
            handlerPayment(chat, message);
            return;
        }else if(userResponse.equals("BACK_TO_MY_ORDERS")){
            handlerMyOrders(chat, message);
            return;
        }else {
            handlerWrongAnswer(chat, message);
            handlerOrderDetails(chat, message);
            return;
        }
    }

    private void handlerPayment(Chat chat, Message message){

        OrderResponseDTO order = serviceOrder.getOrderById(chat.getCurrentOrderId());
        PaymentResponseDTO payment = order.payment();

        String copyAndPastPix = payment.copyAndPastCode();
        String qrCode = payment.qrCodeBase64();
        byte[] imageBytes = Base64.getDecoder().decode(qrCode);
        String imageId = messageService.uploadImage(imageBytes);

        Map<String, String> var = Map.of("valor", order.totalPrice().toString());
        String systemResponseText = systemMessageService.getMessage("PAYMENT", var);

        messageService.sendImagemMessage(chat.getWhatsappNumber(), imageId, systemResponseText);

        String copyPastePixSystemResponseText = systemMessageService.getMessage("COPY_PASTE_PIX", null);
        messageService.sendTextMessage(chat.getWhatsappNumber(), copyPastePixSystemResponseText);

        Map<String, String> opt = new LinkedHashMap<>();
        opt.put("MY_ORDERS", "Ver meus pedidos");
        opt.put("BACK_TO_MAIN_MENU", "Voltar ao menu");
        String wamId =  messageService.sendButtonMessage(chat.getWhatsappNumber(), copyAndPastPix, opt);
        saveMessage(chat, wamId, systemResponseText);
        chat.setChatState(ChatState.AWAITING_PAYMENT_RESPONSE);
        chatRepository.save(chat);

    }

    private void handlerAwaitingPaymentResponse(Chat chat, Message message){
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
                handlerPayment(chat, message);
                break;
        }
    }

    private void handlerHumanAttendance(Chat chat, Message message) {

        String systemResponseText = systemMessageService.getMessage("HUMAN_ATTENDANCE", null);
        String wamId = messageService.sendTextMessage(chat.getWhatsappNumber(), systemResponseText);
        saveMessage(chat, wamId, systemResponseText);
        chat.setChatState(ChatState.IN_HUMAN_ATTENDANCE);
        chatRepository.save(chat);
        simpMessagingTemplate.convertAndSend("/topic/attendance-queue", chat.getId());

    }
}
