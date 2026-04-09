package br.com.espaco_verde.entity;

public enum TiposProdutos {
    FLOR_CORTE("flor corte"),
    FLOR_MUDA("flor muda"),
    ARVORE_FRUTIFERA("arvore frutifera"),
    ARVORE_ORNAMENTAL("arvore ornamental"),
    ARBUSTO("arbusto"),
    VASO("vaso"),
    BUQUE("buque"),
    CESTA("cesta"),
    OUTRO("outro");

    private String type;

    TiposProdutos(String type){
      this.type = type;
    }

    public String getType() {
      return type;
    }
}
