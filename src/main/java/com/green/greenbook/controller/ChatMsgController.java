package com.green.greenbook.controller;

import com.green.greenbook.config.JwtAuthenticationProvider;
import com.green.greenbook.domain.dto.MemberDto;
import com.green.greenbook.domain.dto.chat.ChatMsgRequest;
import com.green.greenbook.domain.dto.chat.ChatMsgResponse;
import com.green.greenbook.service.ChatMsgService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat/msg")
public class ChatMsgController {

    private final ChatMsgService chatMsgService;
    private final JwtAuthenticationProvider provider;

    private final String TOKEN_NAME = "X-AUTH-TOKEN";

    @PostMapping("/{roomId}")
    public ResponseEntity<Object> send(
        @RequestHeader(name = TOKEN_NAME) String token,
        @PathVariable Long roomId,
        @RequestBody @Valid ChatMsgRequest message) {
        MemberDto dto = provider.getMemberDto(token);

        return ResponseEntity.ok(chatMsgService.send(message, dto.getId(), roomId));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Object> getChatList(
        @RequestHeader(name = TOKEN_NAME) String token,
        @PathVariable Long roomId) {
        MemberDto dto = provider.getMemberDto(token);

        List<ChatMsgResponse> chatMsgList = chatMsgService.getChatMsgList(roomId, dto.getId());
        return ResponseEntity.ok(chatMsgList);
    }

}