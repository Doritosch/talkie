package com.talkie.chat.message.dto;

import com.talkie.chat.message.entity.Message;

import java.time.LocalDateTime;

public record MessageResponse(
        Long id,
        String content,
        String senderNickname,
        LocalDateTime createdAt
) {
    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.getUser().getNickname(),
                message.getCreatedAt()
        );
    }
}
