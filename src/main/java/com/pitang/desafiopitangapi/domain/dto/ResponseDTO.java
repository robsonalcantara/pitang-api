package com.pitang.desafiopitangapi.domain.dto;

import com.pitang.desafiopitangapi.domain.dto.UserDTO;

/**
 * DTO (Data Transfer Object) used for sending the response
 * after a successful login. Contains the authenticated user
 * and the generated JWT token.
 */
public record ResponseDTO(UserDTO user, String token) {
}
