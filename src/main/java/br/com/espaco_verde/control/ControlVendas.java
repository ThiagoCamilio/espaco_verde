package br.com.espaco_verde.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ControlVendas {

  @GetMapping("/vendas")
  public String AcessarVendas() throws Exception{
    return "vendas/tela";
  }


}
