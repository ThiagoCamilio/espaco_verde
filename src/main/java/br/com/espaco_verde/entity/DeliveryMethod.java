package br.com.espaco_verde.entity;

public enum DeliveryMethod {
    DELIVERY("Entrega"),
    PICKUP("Retirada");

    private String type;

    DeliveryMethod(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
