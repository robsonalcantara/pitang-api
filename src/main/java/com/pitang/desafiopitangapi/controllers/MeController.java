package com.pitang.desafiopitangapi.controllers;

import com.pitang.desafiopitangapi.config.UserAuthenticationProvider;
import com.pitang.desafiopitangapi.domain.dto.UserDTO;
import com.pitang.desafiopitangapi.domain.mapper.UserMapper;
import com.pitang.desafiopitangapi.domain.model.User;
import com.pitang.desafiopitangapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling requests related to the currently logged-in user.
 * Provides an endpoint to retrieve the details of the logged-in user.
 */
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UserMapper userMapper;
    /**
     * Retrieves the details of the currently logged-in user.
     *
     * @author Robson Rodrigues
     * @param request The HTTP request containing authentication information.
     * @return A {@link ResponseEntity} containing the logged-in user's details in a {@link UserDTO} object.
     */
    @GetMapping
    ResponseEntity<UserDTO> findByMe(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(userService.findByMe(request));
    }

}
