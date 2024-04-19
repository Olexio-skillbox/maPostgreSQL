package com.example.postgres.user.controller;

import com.example.postgres.user.dto.request.RegistrationRequest;
import com.example.postgres.user.dto.request.EditUserRequest;
import com.example.postgres.user.dto.response.UserResponse;
import com.example.postgres.user.entity.UserEntity;
import com.example.postgres.user.exception.BadRequestException;
import com.example.postgres.user.exception.UserAlreadyExistException;
import com.example.postgres.user.exception.UserNotFoundException;
import com.example.postgres.user.repository.UserRepository;
import com.example.postgres.user.routes.UserRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
// Block 09.2 Spring Security
// @AllArgsConstructor
@RequiredArgsConstructor
public class UserApiController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Block 09.2 Spring Security
    @Value("${init.email}")
    private String initUser;
    @Value("${init.password}")
    private String initPassword;

    @GetMapping("/")
    public UserEntity root() {
        UserEntity user = UserEntity.builder()
                .firstName("Smoke")
                .lastName("Test")
                .build();
        user = userRepository.save(user);
        return user;
    }

    // db-14: POST user API
    @PostMapping(UserRoutes.REGISTRATION)
    public UserResponse registration(@RequestBody RegistrationRequest request) throws BadRequestException, UserAlreadyExistException {
        // Block 09.2 Spring Security
        request.validate();

        Optional<UserEntity> check = userRepository.findByEmail(request.getEmail());
        if (check.isPresent()) throw new UserAlreadyExistException();

        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                // Block 09.2 Spring Security
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);
        return UserResponse.of(user);
    }

    // db-15:GET user by ID
    @GetMapping(UserRoutes.BY_ID)
    public UserResponse byId(@PathVariable Long id) throws UserNotFoundException {
        return UserResponse.of(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    // db-16:GET all users
    @GetMapping(UserRoutes.SEARCH)
    public List<UserResponse> search(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            // db-17:GET filtered users
            @RequestParam(defaultValue = "") String query
    ) {
        Pageable pageable = PageRequest.of(page, size);

        // db-17:GET filtered Users
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<UserEntity> example = Example.of(
                UserEntity.builder().firstName(query).lastName(query).build(),
                exampleMatcher);

        //return userRepository.findAll(pageable).stream().map(UserResponse::of).collect(Collectors.toList());
        // db-17:GET filtered users
        return userRepository.findAll(example, pageable).stream().map(UserResponse::of).collect(Collectors.toList());
    }

    // Block 08, Swagger
    @Operation(summary = "Редактируем пользователя", description = "Редактирование существующего пользователя")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Пользователь отредактирован",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponse.class)) }),
                    @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Нет пользователя с таким id",
                            content = @Content)
            }
    )
    // db-18:PUT/edit user
    @PutMapping(UserRoutes.EDIT)
    public UserResponse edit(Principal principal, @RequestBody EditUserRequest request) throws UserNotFoundException {
        UserEntity user = userRepository.findByEmail(principal.getName()).orElseThrow(UserNotFoundException::new);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user = userRepository.save(user);

        return UserResponse.of(user);
    }

    // db-19:DELETE user
    @DeleteMapping(UserRoutes.BY_ID)
    public String delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return HttpStatus.OK.name();
    }

    // Block 09 - Spring Boot Security
    // @GetMapping(UserRoutes.TEST)
    // Block 09.2 Spring Security
    @GetMapping(UserRoutes.INIT)
    public UserResponse init() {
        Optional<UserEntity> checkUser = userRepository.findByEmail(initUser);
        UserEntity user;

        if (checkUser.isEmpty()) {
            user = UserEntity.builder()
                    .firstName("Default")
                    .lastName("Default")
                    .email(initUser)
                    .password(passwordEncoder.encode(initPassword))
                    .build();
            user = userRepository.save(user);
        } else {
            user = checkUser.get();
        }
        // Block 09.2 Spring Security
        // return HttpStatus.OK.name();
        return UserResponse.of(user);
    }
}
