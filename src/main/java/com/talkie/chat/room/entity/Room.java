package com.talkie.chat.room.entity;

import com.talkie.chat.room.enums.RoomType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "rooms")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;
    private RoomType roomType;

    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public Room(String roomName, RoomType roomType) {
        this.roomName = roomName;
        this.roomType = roomType;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
