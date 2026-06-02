package br.com.espaco_verde.entity;

public enum ChatState {

    NEW_CONTACT("novo contato"),
    STAND_BY("ocioso"),
    GREETINGS("saudacao"),
    AWAITING_MENU_RESPONSE("esperando resposta do menu"),
    CATALOG("catalogo"),
    ORDERS("pedidos"),
    CART("carrinho"),
    STAFF("atendente");

    private String type;

    ChatState(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
