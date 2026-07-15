package com.talkie.chat.auth.controller;

import com.talkie.chat.auth.dto.LoginRequest;
import com.talkie.chat.auth.dto.SignupRequest;
import com.talkie.chat.auth.dto.TokenResponse;
import com.talkie.chat.auth.service.AuthService;
import com.talkie.chat.auth.utils.CookieUtil;
import com.talkie.chat.user.dto.UserResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        UserResponse signup = authService.signup(request);
        return ResponseEntity.ok(signup);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse http) {
        TokenResponse response = authService.login(request);

        CookieUtil.createCookie(http, response.refreshToken());

        return ResponseEntity.ok(new TokenResponse(response.accessToken(), null));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("REFRESH_TOKEN".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        TokenResponse reissuedResponse = authService.reissue(refreshToken);
        CookieUtil.createCookie(response, reissuedResponse.refreshToken());

        return ResponseEntity.ok(new TokenResponse(reissuedResponse.accessToken(), null));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        String refreshToken = null;
        if (cookies != null) {
            for(Cookie cookie : cookies) {
                if ("REFRESH_TOKEN".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        CookieUtil.deleteCookie(response);
        authService.logout(refreshToken);

        return ResponseEntity.noContent().build();
    }
}
