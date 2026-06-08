package br.com.espaco_verde.entity;

public enum ChatState {

    NEW_CONTACT("novo contato"),
    STAND_BY("ocioso"),
    GREETINGS("saudacao"),
    AWAITING_MENU_RESPONSE("esperando resposta do menu"),
    CATALOG("catalogo"),
    AWAITING_CATALOG_RESPONSE("esperando resposta do catalogo"),
    AWAITING_ADD_CART_RESPONSE("esperando resposta ao adicionar ao carrinho"),
    CART("carrinho"),
    AWAITING_CART_RESPONSE("esperando resposta do carrinho"),
    ORDERS("pedidos"),
    AWAITING_CHECKOUT_DELIVERY_RESPONSE("esperando resposta para o método de entrega"),
    CHECKOUT_DELIVERY("Checkout Entrega"),
    DELIVERY_ADDRESS_INPUT("perguntando ao cliente qual é o endereço de entrega"),
    AWAITING_DELIVERY_INPUT_RESPONSE("Esperando o cliente digitar o endereço de entrega"),
    AWAITING_CHECKOUT_CONFIRMATION_RESPONSE("Esperando confirmação do checkout"),
    AWAITING_ORDER_RESPONSE("Esperando decisão do cliente após criar pedido"),
    AWAITING_MY_ORDERS_RESPONSE("Esperando escolha nos pedidos do cliente"),
    AWAITING_ORDER_DETAILS_RESPONSE("Esperando resposta nos detalhes do pedido"),
    AWAITING_PAYMENT_RESPONSE("Esperando resposta do pagamento"),
    IN_HUMAN_ATTENDANCE("Em atendimento humano");

    private String type;

    ChatState(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
