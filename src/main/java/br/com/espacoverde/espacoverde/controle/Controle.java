package br.com.espacoverde.espacoverde.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoverde.espacoverde.entity.Produto;
import br.com.espacoverde.espacoverde.entity.TiposProdutos;
import br.com.espacoverde.espacoverde.repositorio.RepositorioProduto;
import br.com.espacoverde.espacoverde.service.ServiceProduto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class Controle {

    @Autowired
    private RepositorioProduto acaoProduto;

    @Autowired
    private ServiceProduto serviceProduto;

    @GetMapping("")
    public String mensagem(){

        return "Bem Vindo. Essa sera futuramente a api para a floricultura Espaço Verde. Ainda estamos em construção";

    }

    @GetMapping("/mock")
    public void mock() {

        Produto produto1 = new Produto( "Margarida", TiposProdutos.FLOR, 2, "Hojekk", 2.1, 3.1);
        acaoProduto.save(produto1);

    }

    @PostMapping("/cadastroProduto")
    public ResponseEntity<?> cadastroProduto(@RequestBody Produto p) {

        return serviceProduto.cadastrar(p);

    }

}
