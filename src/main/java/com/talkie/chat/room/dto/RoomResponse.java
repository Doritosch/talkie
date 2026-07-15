package com.talkie.chat.room.dto;

import com.talkie.chat.room.entity.Room;
import com.talkie.chat.room.enums.RoomType;

public record RoomResponse(
        Long roomId,
        String roomName,
        RoomType roomType,
        int memberCount
) {
    public static RoomResponse from(Room room, int memberCount) {
        return new RoomResponse(
                room.getId(),
                room.getRoomName(),
                room.getRoomType(),
                memberCount
        );
    }
}
