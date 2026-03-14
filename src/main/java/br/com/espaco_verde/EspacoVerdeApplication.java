package br.com.espaco_verde;

import br.com.espaco_verde.entity.Produto;
import br.com.espaco_verde.entity.ProdutoCarrinho;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
public class EspacoVerdeApplication {

	public static void main(String[] args) {SpringApplication.run(EspacoVerdeApplication.class, args);
    }



}
