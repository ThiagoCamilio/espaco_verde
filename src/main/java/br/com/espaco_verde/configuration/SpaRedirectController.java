package br.com.espaco_verde.configuration;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaRedirectController implements ErrorController {

    @RequestMapping(value = "{path:[^\\.]*}")
    public String redirect(){
        return "forward:/index.html";
    }
}
