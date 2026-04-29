package br.com.espaco_verde.control;

import br.com.espaco_verde.DTO.AuthenticationDTO;
import br.com.espaco_verde.DTO.UserDTO;
import br.com.espaco_verde.repository.RepositoryUser;
import br.com.espaco_verde.service.ServiceUser;
import br.com.espaco_verde.service.ServiceToken;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("auth")

public class ControllerAuthentication {

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private ServiceToken serviceToken;

    @Autowired
    private ServiceUser serviceUser;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO userData){

        return serviceUser.login(userData);

    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserDTO userData){

        return serviceUser.userRegister(userData);
    }
}
