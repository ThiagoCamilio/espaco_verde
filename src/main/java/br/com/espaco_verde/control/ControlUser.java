package br.com.espaco_verde.control;

import br.com.espaco_verde.DTO.UserResponseDTO;
import br.com.espaco_verde.DTO.UserUpdateDTO;
import br.com.espaco_verde.entity.User;
import br.com.espaco_verde.service.ServiceUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class ControlUser {

    @Autowired
    private ServiceUser serviceUser;

    @GetMapping("/profile")
    public ResponseEntity<?> getUserData(@AuthenticationPrincipal User logedUser){
        return ResponseEntity.status(200).body(new UserResponseDTO(logedUser));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal User logedUser, @RequestBody @Valid UserUpdateDTO updateData){
        return serviceUser.updateUser(logedUser, updateData);
    }

}
