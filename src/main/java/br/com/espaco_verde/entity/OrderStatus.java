package br.com.espaco_verde.entity;

public enum OrderStatus {
    AWAITING_ANALYSIS("Aguardando Análise"),
    AWAITING_PAYMENT("Aguardando Pagamenro"),
    PAID("Pago"),
    IN_DELIVERY("Em entrega"),
    DELIVERED("Entregue"),
    CANCELED("Cancelado");

    private String type;

    OrderStatus(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
