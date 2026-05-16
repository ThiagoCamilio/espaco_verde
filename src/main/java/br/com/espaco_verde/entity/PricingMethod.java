package br.com.espaco_verde.entity;

public enum PricingMethod {

    MARKUP_DIVISOR("Markup Divisor"),
    LUCRO_SOBRE_O_CUSTO_COM_REPASSE("Lucro sobre o custom com repasse");

    private String type;

    PricingMethod(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
