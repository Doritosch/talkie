package com.talkie.chat.message.controller;

import com.talkie.chat.global.redis.ChatMessage;
import com.talkie.chat.global.redis.RedisPublisher;
import com.talkie.chat.message.dto.ChatMessageRequest;
import com.talkie.chat.message.dto.MessageResponse;
import com.talkie.chat.message.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import tools.jackson.databind.ObjectMapper;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ObjectMapper objectMapper;
    private final ChannelTopic channelTopic;
    private final MessageService messageService;

    @MessageMapping("/rooms/{roomId}/send")
    public void sendMessage(@DestinationVariable Long roomId, @Valid @Payload ChatMessageRequest request, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        MessageResponse messageResponse = messageService.saveMessage(userId, roomId, request.content());

        ChatMessage chatMessage = new ChatMessage(roomId, messageResponse);

        try {
            String json = objectMapper.writeValueAsString(chatMessage);
            redisPublisher.publish(channelTopic, json);
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 메시지입니다.");
        }
    }

    @MessageExceptionHandler(IllegalArgumentException.class)
    @SendToUser("/queue/errors")
    public String handleException(IllegalArgumentException e) {
        return e.getMessage();
    }
}
