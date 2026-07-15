package com.talkie.chat.room.repository;

import com.talkie.chat.room.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    @Query("SELECT rm FROM RoomMember rm WHERE rm.user.id = :userId")
    List<RoomMember> findByUserId(@Param("userId") Long userId);
    @Query("SELECT rm FROM RoomMember rm WHERE rm.user.id = :userId AND rm.room.id = :roomId")
    Optional<RoomMember> findByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);
    @Query("SELECT COUNT(rm) FROM RoomMember rm WHERE rm.room.id = :roomId")
    int countByRoomId(@Param("roomId") Long roomId);
    @Query("SELECT COUNT(rm) > 0 FROM RoomMember rm WHERE rm.user.id = :userId AND rm.room.id = :roomId")
    boolean existsByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);
}
