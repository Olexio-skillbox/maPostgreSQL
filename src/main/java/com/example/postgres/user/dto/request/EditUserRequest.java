// db-18:PUT/edit user
package com.example.postgres.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
// @Setter
public class EditUserRequest {
    private Long id;
    private String firstName;
    private String lastName;
}
