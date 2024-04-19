package com.example.postgres.user.service;

import com.example.postgres.user.entity.UserEntity;
import com.example.postgres.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.List;
import java.util.Optional;

// Block 09 - Spring Boot Security
@Component
@AllArgsConstructor
public class UserAuthService implements UserDetailsService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // Block 09.2 Spring Security
    private final UserRepository userRepository;
    @Override
    //public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Block 09.2 Spring Security
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        if (optionalUserEntity.isEmpty()) throw new UsernameNotFoundException(
                "User with this Email isn't found");
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("user")
        );
        // Block 09.2 Spring Security
        UserEntity user = optionalUserEntity.get();
        //return new User("admin", passwordEncoder.encode("password"), authorities);
        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
