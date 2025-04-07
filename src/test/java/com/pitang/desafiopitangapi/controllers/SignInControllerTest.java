package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.config.UserAuthenticationProvider;
import com.pitang.desafiopitangapi.domain.dto.UserDTO;
import com.pitang.desafiopitangapi.domain.mapper.UserMapper;
import com.pitang.desafiopitangapi.domain.model.User;
import com.pitang.desafiopitangapi.domain.dto.LoginRequestDTO;
import com.pitang.desafiopitangapi.exceptions.InvalidTokenException;
import com.pitang.desafiopitangapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignInControllerTest {

    @InjectMocks
    private SignInController signInController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserAuthenticationProvider userAuthenticationProvider;

    private User user;
    private LoginRequestDTO loginRequestDTO;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@test.com");
        user.setBirthday(new Date());
        user.setLogin("test");
        user.setPassword("hashedPassword123");
        user.setPhone("123456789");

        loginRequestDTO = new LoginRequestDTO("test", "password123");

        userDTO = new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthday(),
                user.getLogin(),
                user.getPassword(),
                user.getPhone(),
                null,
                LocalDate.now(),
                null,
                null
        );
    }

    @Test
    @DisplayName("Failed sign-in due to wrong password")
    public void testSignInFailureWrongPassword() {
        when(userRepository.findByLogin("test")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        LoginRequestDTO wrongPasswordLogin = new LoginRequestDTO("test", "wrongpassword");

        Exception exception = assertThrows(InvalidTokenException.class, () -> {
            signInController.signIn(wrongPasswordLogin);
        });

        assertEquals("Invalid login or password", exception.getMessage());

        verify(userRepository).findByLogin("test");
        verify(passwordEncoder).matches("wrongpassword", "hashedPassword123");
    }

    @Test
    @DisplayName("Failed sign-in due to invalid credentials")
    public void testSignInFailureInvalidCredentials() {
        when(userRepository.findByLogin("invalid")).thenReturn(Optional.empty());

        LoginRequestDTO invalidLogin = new LoginRequestDTO("invalid", "wrongpassword");

        Exception exception = assertThrows(InvalidTokenException.class, () -> {
            signInController.signIn(invalidLogin);
        });

        assertEquals("Invalid login or password", exception.getMessage());

        verify(userRepository).findByLogin("invalid");
    }

    @Test
    @DisplayName("Unexpected exception during sign-in")
    public void testSignInUnexpectedException() {
        when(userRepository.findByLogin("test")).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            signInController.signIn(loginRequestDTO);
        });

        assertEquals("Database error", exception.getMessage());

        verify(userRepository).findByLogin("test");
    }

}
