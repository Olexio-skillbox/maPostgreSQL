// db-18:PUT/edit user
package com.example.postgres.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
// @Setter
@Builder
public class EditUserRequest {
    private Long id;
    private String firstName;
    private String lastName;
}
