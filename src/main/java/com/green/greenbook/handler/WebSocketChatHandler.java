package com.green.greenbook.handler;

import com.green.greenbook.exception.CustomException;
import com.green.greenbook.exception.ErrorCode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    private Map<Long, List<WebSocketSession>> chatRooms = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long roomId = extractRoomId(session);
        List<WebSocketSession> roomSessions = chatRooms.getOrDefault(roomId, new ArrayList<>());
        roomSessions.add(session);
        chatRooms.put(roomId, roomSessions);
        log.info(session + "의 클라이언트 접속");
    }

    private Long extractRoomId(WebSocketSession session) {
        Long roomId = null;
        String uri = Objects.requireNonNull(session.getUri()).toString();
        String[] uriParts = uri.split("/");
        // /chat/msg/{roomId} 일 때 roomId 추출
        if (uriParts.length >= 4 && uriParts[2].equals("msg")) {
            return Long.valueOf(uriParts[3]);
        }
        // /chat/room/join/{roomId}, /chat/room/out/{roomId}, /chat/room/delete/{roomId} 일 때 roomId 추출
        if (uriParts.length >= 5 && uriParts[2].equals("room") &&
            (uriParts[3].equals("join") || uriParts[3].equals("out") || uriParts[3].equals("delete"))) {
            roomId = Long.valueOf(uriParts[4]);
        }
        return roomId;
    }

    // 클라이언트로부터 받은 메시지를 처리하는 로직
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long roomId = extractRoomId(session);
        List<WebSocketSession> roomSessions = chatRooms.get(roomId);
        if (roomSessions != null) {
            String payload = message.getPayload();
            log.info("전송 메시지: " + payload);

            for (WebSocketSession roomSession : roomSessions) {
                try {
                    roomSession.sendMessage(message);
                } catch (IOException e) {
                    throw new CustomException(ErrorCode.CHAT_ERROR);
                }
            }
        } else {
            log.info("해당 채팅방에 클라이언트가 없습니다.");
            throw new CustomException(ErrorCode.NOT_EXIST_CLIENT);
        }
    }

}