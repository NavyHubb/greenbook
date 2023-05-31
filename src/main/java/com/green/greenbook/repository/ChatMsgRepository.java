package com.green.greenbook.repository;

import com.green.greenbook.domain.model.ChatMsg;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMsgRepository extends JpaRepository<ChatMsg, Long> {
    List<ChatMsg> findTop10ByChatRoom_IdOrderByCreatedAtDesc(Long roomId);
}
