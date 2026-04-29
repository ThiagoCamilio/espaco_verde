package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.User;
import br.com.espaco_verde.entity.UserRole;

public record UserResponseDTO(
        String name,
        String login,
        String password,
        String phone,
        String adress
) {
    public  UserResponseDTO(User u){

        this(
                u.getName(),
                u.getLogin(),
                u.getPassword(),
                u.getPhone(),
                u.getAdress()
        );

    }

    public User toEntity(){

        User u = new User();
        u.setName(this.name);
        u.setLogin(this.login);
        u.setPassword(this.password);
        u.setPhone(this.phone);
        u.setAdress(this.adress);
        return u;

    }
}
