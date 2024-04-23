package com.example.postgres;

import com.example.postgres.user.dto.request.RegistrationRequest;
import com.example.postgres.user.dto.request.EditUserRequest;
import com.example.postgres.user.dto.response.UserResponse;
import com.example.postgres.user.entity.UserEntity;
import com.example.postgres.user.repository.UserRepository;
import com.example.postgres.user.routes.UserRoutes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@SpringBootTest
@SpringBootTest
@AutoConfigureMockMvc
// @RequiredArgsConstructor
public class WebTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    // Block 09 Sec4
    @Value("${init.email}")
    private String initUser;
    @Value("${init.password}")
    private String initPassword;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void config() {
        Optional<UserEntity> check = userRepository.findByEmail(initUser);
        if (check.isPresent()) return;

        UserEntity user = UserEntity.builder()
                .firstName("")
                .lastName("")
                .email(initUser)
                .password(passwordEncoder.encode(initPassword))
                .build();
        user = userRepository.save(user);
    }

    public String authHeader() {
        return "Basic " + Base64.getEncoder().encodeToString((initUser + ":" + initPassword).getBytes());
    }

    @Test
    void contextLoad() throws Exception {
        UserEntity user = UserEntity.builder()
                .firstName("1")
                .lastName("2")
                .build();
        user = userRepository.save(user);
        mockMvc.perform(get(UserRoutes.BY_ID, user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authHeader())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    void createTest() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .firstName("createUser")
                .lastName("createUser")
                .email("eee@ee.ru")
                .password("1")
                .build();
        mockMvc.perform(
          post(UserRoutes.REGISTRATION)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(content().string(containsString("createUser")));
    }
    @Test
    void findByIdTest() throws Exception {
        UserEntity user = UserEntity.builder()
                .firstName("findById")
                .lastName("findById")
                .build();

        userRepository.save(user);

        mockMvc.perform(
                get(UserRoutes.BY_ID, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authHeader())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("findById"))
                );
    }
    @Test
    void findById_NotFound_Test() throws Exception {
        mockMvc.perform(
                        get(UserRoutes.BY_ID, 12345)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, authHeader())
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    void updateTest() throws Exception {
        UserEntity user = UserEntity.builder()
                .firstName("first")
                .lastName("last")
                .build();
        userRepository.save(user);

        EditUserRequest request = EditUserRequest.builder()
                //.id(user.getId())
                .firstName("updateUser")
                .lastName("updateUser")
                .build();

        //mockMvc.perform(put(UserRoutes.BY_ID, user.getId().toString())
        // Spring Sec - 04
        mockMvc.perform(put(UserRoutes.EDIT, user.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(HttpHeaders.AUTHORIZATION, authHeader())
        ).andDo(print())
                .andExpect(content().string(containsString("updateUser")));
    }
    @Test
    void deleteTest() throws Exception {
        UserEntity user = UserEntity.builder()
                .firstName("first")
                .lastName("last")
                .build();
        userRepository.save(user);

        mockMvc.perform(
                delete(UserRoutes.BY_ID, user.getId())
                        .header(HttpHeaders.AUTHORIZATION, authHeader())
                ).andDo(print())
                 .andExpect(status().isOk());
        assert userRepository.findById(user.getId()).isEmpty();
    }
    @Test
    void searchTest() throws Exception {
        List<UserResponse> result = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            UserEntity user = UserEntity.builder()
                    .firstName("userName_" + i)
                    .lastName("userName_" + i)
                    .build();
            user = userRepository.save(user);
            result.add(UserResponse.of(user));
        }
        mockMvc.perform(get(UserRoutes.SEARCH)
                        .param("size", "1000")
                        .param("query", "userName_")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, authHeader())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }
}
