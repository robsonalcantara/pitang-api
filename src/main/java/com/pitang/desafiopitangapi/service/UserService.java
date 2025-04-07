package com.pitang.desafiopitangapi.service;

import com.pitang.desafiopitangapi.config.TokenService;
import com.pitang.desafiopitangapi.domain.dto.UserDTO;
import com.pitang.desafiopitangapi.exceptions.BusinessException;
import com.pitang.desafiopitangapi.domain.model.Car;
import com.pitang.desafiopitangapi.domain.model.User;
import com.pitang.desafiopitangapi.exceptions.InvalidTokenException;
import com.pitang.desafiopitangapi.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling user-related operations such as registration, update, retrieval, and deletion.
 * It interacts with the {@link UserRepository}, {@link CarService}.
 */
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CarService carService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    //private final UserAuthenticationProvider userAuthenticationProvider;




    /**
     * Registers a new user. Validates the user details, checks for duplicate login and email,
     * encrypts the password, and saves the user to the repository. Also registers any cars associated with the user.
     *
     * @param userDTO The user data transfer object containing the user's details.
     * @return The registered user data transfer object.
     * @throws BusinessException if the login or email already exists or if the validation fails.
     */
    @Transactional
    public UserDTO register(UserDTO userDTO) throws BusinessException {
        if (userRepository.existsByLogin(userDTO.getLogin())) {
            throw new BusinessException("Login already exists", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new BusinessException("Email already exists", HttpStatus.BAD_REQUEST);
        }
        User newUser = UserDTO.toEntity(userDTO);
        newUser.validate();
        //userMapper.toUserDTO(userRepository.save(userMapper.toUserEntity(userDTO)));
        if (newUser.getCars() != null)
            carService.validateCarList(newUser.getCars());

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        //this.userRepository.save(newUser);
        try {
            User saveUser = userRepository.save(newUser);
            if (newUser.getCars() != null) {
                for (Car car : newUser.getCars()) {
                    car.setUser(saveUser);
                    carService.register(car, null);
                }
            }
        }catch (Exception e){
            throw new BusinessException("error in registration", HttpStatus.BAD_REQUEST);
        }
        return userDTO;
    }

    /**
     * Updates the last login timestamp for a user.
     *
     * @param user The user whose last login timestamp will be updated.
     */
    public void updateLastLogin(User user) {
        user.setLastLogin(LocalDate.now());
        userRepository.save(user);
    }

    /**
     * Retrieves all users and returns them as a list of {@link UserDTO}.
     *
     * @return A list of all users as data transfer objects.
     */
    public List<UserDTO> findAll() {

        return userRepository.findAll().stream().map(obj -> User.toDTO(obj)).collect(Collectors.toList());
    }

    /**
     * Finds a user by their ID and returns their data as a {@link UserDTO}.
     *
     * @param id The ID of the user to be retrieved.
     * @return The user data transfer object.
     * @throws BadCredentialsException if the user with the specified ID is not found.
     */
    public UserDTO findById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new InvalidTokenException("Invalid Id"));
        return User.toDTO(user);
    }

    /**
     * Retrieves the currently logged-in user based on the authentication token from the request.
     *
     * @param request The HTTP request containing the authentication token.
     * @return The user data transfer object of the logged-in user.
     */
    public UserDTO findByMe(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByLogin(((UserDTO) auth.getPrincipal()).getLogin())
                .orElseThrow(() -> new InvalidTokenException("Invalid login"));
        UserDTO userDTO = User.toDTO(user);
        userDTO.setCars(user.getCars());
        return userDTO;
    }

    /**
     * Finds a user by their login.
     *
     * @param login The login of the user to be retrieved.
     * @return The user entity.
     * @throws InvalidTokenException if the user with the specified login is not found.
     */
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new InvalidTokenException("Invalid login"));
    }

    /**
     * Updates a user's details based on the provided user ID and user data transfer object.
     *
     * @param id The ID of the user to be updated.
     * @param userDTO The data transfer object containing the updated user details.
     * @return The updated user data transfer object.
     * @throws InvalidTokenException if the user with the specified ID is not found.
     */
    public UserDTO update(String id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new InvalidTokenException("Invalid Id"));
        User newUser = UserDTO.toEntity(userDTO);
        if (newUser.getId() == null)
            newUser.setId(id);
        if (newUser.getPassword() == null)
            newUser.setPassword(user.getPassword());
        if (user.getCars() != null)
            newUser.setCars(user.getCars());
        newUser.setCreatedAt(user.getCreatedAt());
        newUser.setLastLogin(user.getLastLogin());

        newUser.validate();
        userRepository.save(newUser);
        return User.toDTO(user);
    }

    /**
     * Deletes a user based on their ID. Also deletes any associated cars.
     *
     * @param id The ID of the user to be deleted.
     * @throws InvalidTokenException if the user with the specified ID is not found.
     */
    public void delete(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new InvalidTokenException("Invalid Id"));
        if (user.getCars() != null) {
            for (Car car : user.getCars()) {
                carService.deleteByCar(car);
            }
        }
        userRepository.delete(user);
    }
}
