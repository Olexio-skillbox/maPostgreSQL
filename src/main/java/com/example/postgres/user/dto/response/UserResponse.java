// db-14: POST user API
package com.example.postgres.user.dto.response;

import com.example.postgres.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
// Block 09 Spring Security
//public class UserResponse extends UserEntity {
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public static UserResponse of(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())    // Block 09 Spring Security
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())     // Block 09 Spring Security
                .build();
    }
}
