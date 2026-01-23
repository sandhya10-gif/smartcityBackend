package com.example.SmartCity.Controller;

import com.example.SmartCity.security.JwtUtil;
import com.example.SmartCity.dto.LoginRequest;
import com.example.SmartCity.model.User;
import com.example.SmartCity.Service.UserService;
import com.example.SmartCity.dto.LoginResponse;
import com.example.SmartCity.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    public AuthController(UserService userService,JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        System.out.println("CONTROLLER HIT: " + request);

        userService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        User user = userService.login(request);

        // ✅ FIX: convert enum → String
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return ResponseEntity.ok(
                new LoginResponse(token, user.getRole().name())
        );
    }


}
