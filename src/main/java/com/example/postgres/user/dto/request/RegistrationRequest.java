// db-14: POST user API
package com.example.postgres.user.dto.request;

import com.example.postgres.user.exception.BadRequestException;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    // Block 09 Spring Security
    private String email;
    private String password;
    public void validate() throws BadRequestException {
        if (email == null || email.isBlank()) throw new BadRequestException();
        if (email == password || password.isBlank()) throw new BadRequestException();
    }

}
