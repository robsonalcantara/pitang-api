package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.domain.dto.UserDTO;
import com.pitang.desafiopitangapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MeControllerTest {

    @InjectMocks
    private MeController meController;

    @Mock
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks

        userDTO = new UserDTO();
        userDTO.setId("123e4567-e89b-12d3-a456-426614174000");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");
        userDTO.setEmail("test@test.com");
        userDTO.setBirthday(new Date());
        userDTO.setLogin("test");
        userDTO.setPassword("password123");
        userDTO.setPhone("123456789");

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn("test"); // ou pode ser um objeto UserDetails se seu sistema precisar

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve retornar os dados do usuário autenticado")
    public void testFindByMe() {
        HttpServletRequest request = new MockHttpServletRequest();

        when(userService.findByMe(request)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = meController.findByMe(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserDTO body = response.getBody();
        assertNotNull(body);
        assertEquals("Test", body.getFirstName());
        assertEquals("User", body.getLastName());
        assertEquals("test@test.com", body.getEmail());
    }
}
