package com.talkie.chat.room.dto;

import com.talkie.chat.room.entity.Room;
import com.talkie.chat.room.enums.RoomType;

import java.util.List;

public record RoomCreateRequest(
        String roomName,
        RoomType roomType,
        List<Long> memberIds
) {
}
