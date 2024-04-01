// db-14: POST user API
package com.example.postgres.user.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserRequest {
    private String firstName;
    private String lastName;

}
