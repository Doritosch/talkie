package com.talkie.chat.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
