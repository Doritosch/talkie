package com.talkie.chat.user.dto;

import com.talkie.chat.user.entity.User;

import java.time.LocalDateTime;

public record UserResponse(
        String name,
        String email,
        String nickname,
        String profileImageUrl,
        LocalDateTime lastActiveAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getName(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImageUrl(),
                user.getLastActiveAt()
        );
    }
}
