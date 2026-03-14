package br.com.espaco_verde.control;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espaco_verde.entity.Produto;
import br.com.espaco_verde.entity.TiposProdutos;
import br.com.espaco_verde.repository.RepositoryProduto;
import br.com.espaco_verde.service.ServiceProduto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class Control {

    @Autowired
    private RepositoryProduto acaoProduto;

    @Autowired
    private ServiceProduto serviceProduto;

    @GetMapping("")
    public String mensagem(HttpSession session){

        return "PAGINA INICIAL";

    }

    @GetMapping("/mock")
    public void mock() {

        Produto produto1 = new Produto( "Margarida", TiposProdutos.OUTRO, 2, "Hojekk", 2.1, 3.1);
        acaoProduto.save(produto1);

    }

    @PostMapping("/cadastroProduto")
    public ResponseEntity<?> cadastroProduto(@RequestBody Produto p) {

        return serviceProduto.cadastrar(p);

    }

}
