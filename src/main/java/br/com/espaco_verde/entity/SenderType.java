package br.com.espaco_verde.entity;

public enum SenderType {
    SYSTEM("Sistema"),
    CLIENT("Cliente");

    private String type;

    SenderType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
