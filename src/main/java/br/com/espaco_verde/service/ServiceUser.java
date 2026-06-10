package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.AuthenticationDTO;
import br.com.espaco_verde.DTO.LoginResponseDTO;
import br.com.espaco_verde.DTO.UserDTO;
import br.com.espaco_verde.DTO.UserUpdateDTO;
import br.com.espaco_verde.entity.User;
import br.com.espaco_verde.entity.UserRole;
import br.com.espaco_verde.repository.RepositoryUser;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ServiceUser implements UserDetailsService {

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private ServiceToken serviceToken;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return repositoryUser.findByLogin(username);
    }

    public ResponseEntity<?> login(AuthenticationDTO userData){
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(userData.login(), userData.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        String token = serviceToken.generateToken((User) auth.getPrincipal());

        return ResponseEntity.status(200).body(new LoginResponseDTO(token));
    }

    public ResponseEntity<?> userRegister(UserDTO userData){
        if (repositoryUser.findByLogin(userData.login()) != null){
            return ResponseEntity.status(409).body(Map.of("message", "Email já cadastrados!"));
        }else{
            String encryptedPassword = new BCryptPasswordEncoder().encode(userData.password());
            User newUser = new User(userData.name(), userData.login(), encryptedPassword, userData.role(), userData.phone(), userData.adress());

            repositoryUser.save(newUser);
            return ResponseEntity.status(201).body(Map.of("message", "Usuario " +userData.name() + " cadastrado com sucesso!"));
        }
    }

    @Transactional
    public ResponseEntity<?> updateUser(User user, UserUpdateDTO updateData){

        User updateUser = repositoryUser.getReferenceById(user.getId());
        updateUser.setName(updateData.name());
        updateUser.setAdress(updateData.adress());
        updateUser.setPhone(updateData.phone());

        return ResponseEntity.status(201).build();
    }

    public ResponseEntity<?> completeUser(UserDTO updateData) {

        Optional<User> optionalUser = repositoryUser.findByPhone(updateData.phone());
        if(optionalUser.isEmpty()){
            return ResponseEntity.badRequest().body("Usuário não encontrado");
        }

        User user = optionalUser.get();

        if(user.isProfileComplete()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Esse perfil já está completo, por favor, faça o login");
        }
        user.setLogin(updateData.login());
        String encryptedPassword = new BCryptPasswordEncoder().encode(updateData.password());
        user.setPassword(encryptedPassword);
        user.setName(updateData.name());
        user.setAdress(updateData.adress());
        user.setRole(UserRole.USER);

        repositoryUser.save(user);
        return ResponseEntity.status(201).body(Map.of("message", "Usuario " +updateData.name() + " cadastrado com sucesso!"));

    }
}
