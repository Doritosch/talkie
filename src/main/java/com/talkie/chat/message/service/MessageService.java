package com.talkie.chat.message.service;

import com.talkie.chat.message.dto.MessageResponse;
import com.talkie.chat.message.entity.Message;
import com.talkie.chat.message.repository.MessageRepository;
import com.talkie.chat.room.entity.Room;
import com.talkie.chat.room.repository.RoomRepository;
import com.talkie.chat.user.entity.User;
import com.talkie.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public MessageResponse saveMessage(Long userId, Long roomId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));

        Message message = new Message(content, user, room);
        Message savedMessage = messageRepository.save(message);
        return MessageResponse.from(savedMessage);
    }

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
