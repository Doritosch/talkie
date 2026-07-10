package com.talkie.chat.room.dto;

import com.talkie.chat.room.enums.RoomType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoomCreateRequest(
        String roomName,
        @NotNull RoomType roomType,
        @NotNull List<Long> memberIds
) {
}
