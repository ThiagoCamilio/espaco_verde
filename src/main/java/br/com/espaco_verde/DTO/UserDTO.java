package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.UserRole;

public record RegisterUserDTO(String name, String login, String password, UserRole role, String phone, String adress) {
}
