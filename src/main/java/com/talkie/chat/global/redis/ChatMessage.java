package com.talkie.chat.global.redis;

import com.talkie.chat.message.dto.MessageResponse;

public record ChatMessage(
        Long roomId,
        MessageResponse message
) {
}
