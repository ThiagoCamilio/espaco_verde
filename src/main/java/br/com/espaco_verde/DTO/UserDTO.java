package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.User;
import br.com.espaco_verde.entity.UserRole;

public record UserDTO(
        String name,
        String login,
        String password,
        UserRole role,
        String phone,
        String adress
) {
    public UserDTO(User u){

        this(
                u.getName(),
                u.getLogin(),
                u.getPassword(),
                u.getRole(),
                u.getPhone(),
                u.getAdress()
        );

    }

    public User toEntity(){

        User u = new User();
        u.setName(this.name);
        u.setLogin(this.login);
        u.setPassword(this.password);
        u.setRole(this.role);
        u.setPhone(this.phone);
        u.setAdress(this.adress);
        return u;

    }
}
