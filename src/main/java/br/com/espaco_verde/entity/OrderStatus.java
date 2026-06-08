package br.com.espaco_verde.entity;

public enum OrderStatus {
    AWAITING_ANALYSIS("Aguardando Análise"),
    AWAITING_PAYMENT("Aguardando Pagamento"),
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

    public static OrderStatus fromString(String string){
        for(OrderStatus status : OrderStatus.values()){
            if(status.type.equalsIgnoreCase(string)){
                return status;
            }
        }

        throw new IllegalArgumentException("Nenhum enum encontrado para a string" + string);
    }
}
