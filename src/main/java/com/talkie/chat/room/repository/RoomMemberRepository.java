package com.talkie.chat.room.repository;

import com.talkie.chat.room.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    @Query("SELECT rm.room.id, rm.room.roomName, rm.room.roomType, " +
            "(SELECT COUNT(rm2) FROM RoomMember rm2 WHERE rm2.room.id = rm.room.id) " +
            "FROM RoomMember rm WHERE rm.user.id = :userId")
    List<Object[]> findRoomsWithMemberCountByUserId(@Param("userId") Long userId);
    @Query("SELECT rm FROM RoomMember rm WHERE rm.user.id = :userId AND rm.room.id = :roomId")
    Optional<RoomMember> findByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);

    @Query("SELECT COUNT(rm) > 0 FROM RoomMember rm WHERE rm.user.id = :userId AND rm.room.id = :roomId")
    boolean existsByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);
}
