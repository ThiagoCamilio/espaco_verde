package br.com.espaco_verde.control;

import br.com.espaco_verde.DTO.AuthenticationDTO;
import br.com.espaco_verde.DTO.LoginResponseDTO;
import br.com.espaco_verde.DTO.RegisterDTO;
import br.com.espaco_verde.entity.User;
import br.com.espaco_verde.repository.RepositoryUser;
import br.com.espaco_verde.service.ServiceToken;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("auth")

//mudar a lógica para um service
public class ControllerAuthentication {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private ServiceToken serviceToken;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = serviceToken.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));

    }

    @GetMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){

        if (repositoryUser.findByLogin(data.login()) != null){
            return ResponseEntity.badRequest().build();
        }else{
            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            User newUser = new User(data.name(), data.login(), encryptedPassword, data.role());

            repositoryUser.save(newUser);
            return ResponseEntity.ok().build();
        }
    }
}
