package br.com.espaco_verde.DTO;

import br.com.espaco_verde.entity.UserRole;

public record RegisterDTO(String name, String login, String password, UserRole role) {
}
