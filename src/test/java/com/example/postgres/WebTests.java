package com.example.postgres;

import com.example.postgres.user.dto.request.RegistrationRequest;
import com.example.postgres.user.dto.request.EditUserRequest;
import com.example.postgres.user.dto.response.UserResponse;
import com.example.postgres.user.entity.UserEntity;
import com.example.postgres.user.repository.UserRepository;
import com.example.postgres.user.routes.UserRoutes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@SpringBootTest
@AutoConfigureMockMvc
public class WebTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Test
    void contextLoad() throws Exception {
        UserEntity user = UserEntity.builder()
                .firstName("1")
                .lastName("2")
                .build();
        user = userRepository.save(user);
        mockMvc.perform(get(UserRoutes.BY_ID, user.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    void createTest() throws Exception {
        RegistrationRequest request = RegistrationRequest.builder()
                .firstName("createUser")
                .lastName("createUser")
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
                get(UserRoutes.BY_ID, user.getId()).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("findById"))
                );
    }
    @Test
    void findById_NotFound_Test() throws Exception {
        mockMvc.perform(
                        get(UserRoutes.BY_ID, 12345).contentType(MediaType.APPLICATION_JSON))
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

        mockMvc.perform(put(UserRoutes.BY_ID, user.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
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

        mockMvc.perform(delete(UserRoutes.BY_ID, user.getId()))
                .andDo(print())
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
                .param("size", "1000").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }
}
