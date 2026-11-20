package com.lemarikita.app.controller;

import com.lemarikita.app.dto.LoginRequest;
import com.lemarikita.app.dto.RegistrasiRequest;
import com.lemarikita.app.dto.UserResponse;
import com.lemarikita.app.model.User;
import com.lemarikita.app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegistrasiRequest request) {
        User userBaru = userService.registrasiDonatur(
                request.nama(),
                request.email(),
                request.noTelp(),
                request.password(),
                request.username()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromEntity(userBaru));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.email(), request.password());
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<Object> getLeaderboard() {
        var leaderboard = userService.ambilLeaderboard().stream()
                .map(p -> Map.of(
                        "username", p.getUsername(),
                        "level", p.getLevel(),
                        "totalPoin", p.getTotalPoin()
                ))
                .toList();
        return ResponseEntity.ok(leaderboard);
    }
}
