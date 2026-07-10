package com.talkie.chat.message.service;

import com.talkie.chat.message.dto.MessageResponse;
import com.talkie.chat.message.entity.Message;
import com.talkie.chat.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRepository messageRepository;

    public List<MessageResponse> findMessagesByRoomId(Long roomId, Long cursor, int size) {

        List<Message> findMessages;
        if (cursor == null) {
            findMessages = messageRepository.findFirstMessages(roomId, size);
        } else {
            findMessages = messageRepository.findMessages(roomId, cursor, size);
        }

        return findMessages.stream()
                .map(MessageResponse::from)
                .toList();
    }
    public Message findMessageEntity(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다."));
    }
}
