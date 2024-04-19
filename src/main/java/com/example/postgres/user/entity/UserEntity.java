package com.example.postgres.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
// db-15:GET user by ID
@NoArgsConstructor
@Table(name = "users")
// db-14: POST user API
// @Builder
@SuperBuilder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // db-14: POST user API
    // private Long id;
    protected Long id;
    @Column(name = "first_name")
    protected String firstName;
    // private String firstName;
    protected String lastName;
    // private String lastName;
    // Block 09 Spring Security
    protected String email;
    protected String password;
}
