package com.talkie.chat.message.repository;

import com.talkie.chat.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.room.id = :roomId AND m.id < :cursor ORDER BY m.id DESC LIMIT :size")
    List<Message> findMessages(@Param("roomId") Long roomId, @Param("cursor") Long cursor, @Param("size") int size);
    @Query("SELECT m FROM Message m WHERE m.room.id = :roomId ORDER BY m.id DESC LIMIT :size")
    List<Message> findFirstMessages(@Param("roomId") Long roomId, @Param("size") int size);
}

