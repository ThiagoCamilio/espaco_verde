package br.com.espaco_verde.control;

import br.com.espaco_verde.entity.Carrinho;
import br.com.espaco_verde.entity.ProdutoCarrinho;
import br.com.espaco_verde.service.ServiceCarrinho;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ControlCarrinho {

    @Autowired
    private ServiceCarrinho serviceCarrinho;


    @GetMapping("/addProduto/{id}")
    public void addProduto(@PathVariable int id, HttpSession session){
        //comportamento provisorio apenas para teste
        session.setAttribute("carrinho", serviceCarrinho.addProduto(id, session));
        Carrinho carrinho =(Carrinho) session.getAttribute("carrinho");
        for (ProdutoCarrinho produto : carrinho.getProdutosCarrinho()){
            System.out.println(produto.getProduto().getNome());
        }

    }

}
