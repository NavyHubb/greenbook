package com.green.greenbook.repository;

import com.green.greenbook.domain.model.ChatMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

    // chatRoomId에 해당하는 ChatRoom에 참여하고 있는 Member들의 Id 조회
    @Query("select m.member.id from ChatMember m where m.chatRoom.id = ?1")
    List<Long> findChatMemberIdByChatRoom_Id(Long chatRoomId);

}