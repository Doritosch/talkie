package com.talkie.chat.message.controller;

import com.talkie.chat.message.dto.MessageResponse;
import com.talkie.chat.message.service.MessageService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(@PathVariable Long roomId, @RequestParam(required = false) Long cursor,
                                                       @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size) {
        List<MessageResponse> messageResponses = messageService.findMessagesByRoomId(roomId, cursor, size);
        return ResponseEntity.ok(messageResponses);
    }
}
