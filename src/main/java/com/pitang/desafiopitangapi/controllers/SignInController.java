package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.config.UserAuthenticationProvider;
import com.pitang.desafiopitangapi.domain.dto.UserDTO;
import com.pitang.desafiopitangapi.domain.mapper.UserMapper;
import com.pitang.desafiopitangapi.exceptions.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import com.pitang.desafiopitangapi.domain.dto.LoginRequestDTO;
import com.pitang.desafiopitangapi.domain.dto.ResponseDTO;
import com.pitang.desafiopitangapi.domain.model.User;
import com.pitang.desafiopitangapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling user sign-in requests.
 * Provides an endpoint for user authentication and token generation.
 */

@RestController
@RequestMapping("/signin")
@RequiredArgsConstructor
public class SignInController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UserMapper userMapper;

    /**
     * Authenticates the user based on the provided login credentials.
     * If valid, generates a JWT token and updates the user's last login timestamp.
     *
     * @author Robson Rodrigues
     * @param body The login request containing the user's login and password.
     * @return A {@link ResponseEntity} containing the user details and JWT token.
     * @throws InvalidTokenException if the login or password is incorrect.
     */
    @PostMapping()
    public ResponseEntity signIn(@RequestBody LoginRequestDTO body) {

        User user = userRepository.findByLogin(body.login())
                .orElseThrow(() -> new InvalidTokenException("Invalid login or password", HttpStatus.UNAUTHORIZED));

        // Verifica se a senha é válida
        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new InvalidTokenException("Invalid login or password", HttpStatus.UNAUTHORIZED);
        }

        UserDTO userDTO = userMapper.toUserDTO(user);
        userDTO = User.toDTO(user);
        String token = userAuthenticationProvider.createToken(userDTO);

        return ResponseEntity.ok(new ResponseDTO(userDTO, token));
    }

}
