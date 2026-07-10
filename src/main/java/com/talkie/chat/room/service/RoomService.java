package com.talkie.chat.room.service;

import com.talkie.chat.room.dto.RoomResponse;
import com.talkie.chat.room.entity.Room;
import com.talkie.chat.room.entity.RoomMember;
import com.talkie.chat.room.enums.Role;
import com.talkie.chat.room.enums.RoomType;
import com.talkie.chat.room.repository.RoomMemberRepository;
import com.talkie.chat.room.repository.RoomRepository;
import com.talkie.chat.user.entity.User;
import com.talkie.chat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public RoomResponse createRoom(Long userId, String roomName, RoomType roomType, List<Long> memberIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("없는 유저입니다."));

        if (roomType == RoomType.GROUP) {
            if (roomName == null) {
                throw new IllegalArgumentException("그룹 채팅은 방 이름이 필수입니다.");
            }
        } else {
            if (roomName == null) {
                User inviteUser = userRepository.findById(memberIds.get(0))
                        .orElseThrow(() -> new IllegalArgumentException("없는 유저입니다."));
                roomName = inviteUser.getNickname();
            }
        }
        Room room = new Room(roomName, roomType);
        Room createdRoom = roomRepository.save(room);

        RoomMember roomOwner = new RoomMember(Role.OWNER, user, createdRoom);
        roomMemberRepository.save(roomOwner);

        List<User> members = userRepository.findAllById(memberIds);
        List<RoomMember> roomMembers = new ArrayList<>();
        for(User member : members) {
            if (member.getId().equals(userId)) {
                continue;
            }
            roomMembers.add(new RoomMember(Role.MEMBER, member, createdRoom));
        }
        roomMemberRepository.saveAll(roomMembers);
        return new RoomResponse(createdRoom.getId(), createdRoom.getRoomName(), createdRoom.getRoomType(), members.size() + 1);
    }

    public List<RoomResponse> getMyRooms(Long userId) {
        List<RoomMember> roomMemberList = roomMemberRepository.findByUserId(userId);

        List<RoomResponse> roomResponses = new ArrayList<>();
        for(RoomMember roomMember : roomMemberList) {
            int countMember = roomMemberRepository.countByRoomId(roomMember.getRoom().getId());
            RoomResponse roomResponse = new RoomResponse(roomMember.getRoom().getId(), roomMember.getRoom().getRoomName(),
                    roomMember.getRoom().getRoomType(), countMember);
            roomResponses.add(roomResponse);
        }
        return roomResponses;
    }

    @Transactional
    public void leaveRoom(Long userId, Long roomId) {
        Room room = findByRoomId(roomId);
        RoomMember roomMember = roomMemberRepository.findByUserIdAndRoomId(userId, room.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 방의 회원이 아닙니다."));
        roomMemberRepository.delete(roomMember);
    }
    private Room findByRoomId(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("방을 찾을 수 없습니다."));
    }
}
