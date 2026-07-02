package com.talkie.chat.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank String nickname,
        String profileImageUrl
) {
}
