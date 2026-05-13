package br.com.espaco_verde.entity;

public enum PricingMethod {

    MARKUP_DIVISOR("Markup Divisor"),
    MARKUP_MIXED("Markup Misto");

    private String type;

    PricingMethod(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
