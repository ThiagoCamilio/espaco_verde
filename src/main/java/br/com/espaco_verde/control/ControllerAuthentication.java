package br.com.espaco_verde.control;

import br.com.espaco_verde.DTO.AuthenticationDTO;
import br.com.espaco_verde.DTO.LoginResponseDTO;
import br.com.espaco_verde.DTO.RegisterUserDTO;
import br.com.espaco_verde.entity.User;
import br.com.espaco_verde.repository.RepositoryUser;
import br.com.espaco_verde.service.ServiceAuthentication;
import br.com.espaco_verde.service.ServiceToken;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private ServiceAuthentication serviceAuthentication;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO userData){

        return serviceAuthentication.login(userData);

    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterUserDTO userData){

        return serviceAuthentication.userRegister(userData);
    }
}
