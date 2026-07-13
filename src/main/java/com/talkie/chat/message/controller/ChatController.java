package com.talkie.chat.message.controller;

import com.talkie.chat.message.dto.ChatMessageRequest;
import com.talkie.chat.message.dto.MessageResponse;
import com.talkie.chat.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/rooms/{roomId}/send")
    public void sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageRequest request, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        MessageResponse messageResponse = messageService.saveMessage(userId, roomId, request.content());
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, messageResponse);
    }
}
