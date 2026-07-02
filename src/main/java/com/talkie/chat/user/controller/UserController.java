package com.talkie.chat.user.controller;

import com.talkie.chat.user.dto.UserResponse;
import com.talkie.chat.user.dto.PasswordUpdateRequest;
import com.talkie.chat.user.dto.UserUpdateRequest;
import com.talkie.chat.user.entity.User;
import com.talkie.chat.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal Long id) {
        User user = userService.getUser(id);

        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal Long id, @Valid @RequestBody UserUpdateRequest request) {

        User user = userService.updateProfile(
                id, request.nickname(), request.profileImageUrl()
        );

        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PutMapping("/update/password")
    public ResponseEntity<UserResponse> updatePassword(
            @AuthenticationPrincipal Long id, @Valid @RequestBody PasswordUpdateRequest request
            ) {
        User user = userService.updatePassword(id, request.password());

        return ResponseEntity.ok(UserResponse.from(user));
    }

}
