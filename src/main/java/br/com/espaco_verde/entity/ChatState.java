package br.com.espaco_verde.entity;

public enum ChatState {

    NEW_CONTACT("novo contato"),
    STAND_BY("ocioso"),
    GREETINGS("saudacao"),
    AWAITING_MENU_RESPONSE("esperando resposta do menu"),
    CATALOG("catalogo"),
    AWAITING_CATALOG_RESPONSE("esperando resposta do catalogo"),
    ORDERS("pedidos"),
    CART("carrinho"),
    STAFF("atendente"),
    AWAITING_ADD_CART_RESPONSE("esperando resposta ao adicionar ao carrinho");

    private String type;

    ChatState(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
