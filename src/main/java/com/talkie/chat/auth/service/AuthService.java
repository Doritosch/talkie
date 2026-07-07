package com.talkie.chat.auth.service;

import com.talkie.chat.auth.dto.LoginRequest;
import com.talkie.chat.auth.dto.SignupRequest;
import com.talkie.chat.auth.dto.TokenResponse;
import com.talkie.chat.auth.repository.RefreshTokenRepository;
import com.talkie.chat.global.jwt.JwtProvider;
import com.talkie.chat.user.dto.UserResponse;
import com.talkie.chat.user.entity.User;
import com.talkie.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        try {
            User user = new User(
                    request.name(),
                    passwordEncoder.encode(request.password()),
                    request.email(),
                    request.nickname(),
                    null);
            return UserResponse.from(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.", e);
        }
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        user.updateLastActiveAt();
        String accessToken = jwtProvider.generateAccessToken(user.getId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        refreshTokenRepository.save(user.getId(), refreshToken, jwtProvider.getRefreshTokenExpiration());

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse reissue(String refreshToken) {
        if (!jwtProvider.isValid(refreshToken)) {
            throw new IllegalArgumentException("만료된 토큰입니다.");
        }

        Long extractUserId = jwtProvider.extractUserId(refreshToken);

        String token = refreshTokenRepository.find(extractUserId);
        if (!refreshToken.equals(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        String generatedAccessToken = jwtProvider.generateAccessToken(extractUserId);
        String generatedRefreshToken = jwtProvider.generateRefreshToken(extractUserId);

        refreshTokenRepository.save(extractUserId, generatedRefreshToken, jwtProvider.getRefreshTokenExpiration());
        return new TokenResponse(generatedAccessToken, generatedRefreshToken);
    }

    public void logout(String token) {
        if (!jwtProvider.isValid(token)) {
            throw new IllegalArgumentException("만료된 토큰입니다.");
        }

        Long extractUserId = jwtProvider.extractUserId(token);
        refreshTokenRepository.delete(extractUserId);
    }
}