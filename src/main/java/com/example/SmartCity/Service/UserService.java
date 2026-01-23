package com.example.SmartCity.Service;

import com.example.SmartCity.dto.LoginRequest;
import com.example.SmartCity.dto.RegisterRequest;
import com.example.SmartCity.model.User;
import com.example.SmartCity.model.Role;
import com.example.SmartCity.model.Status;
import com.example.SmartCity.Repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ REGISTER
    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    BAD_REQUEST, "Email already registered"
            );
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // ✅ ENUM VALUES
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);

        // ✅ AUDIT FIELDS
        user.setJoinedDate(LocalDate.now());
        user.setLastActive(LocalDateTime.now());

        return userRepository.save(user);
    }

    // ✅ LOGIN
    public User login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                UNAUTHORIZED, "User not found"
                        ));

        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    UNAUTHORIZED, "Invalid credentials"
            );
        }

        // ✅ UPDATE LAST ACTIVE
        user.setLastActive(LocalDateTime.now());
        userRepository.save(user);

        return user;
    }
}
