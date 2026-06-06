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
    STAFF("atendente");

    private String type;

    ChatState(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
