package com.green.greenbook.controller;

import com.green.greenbook.config.JwtAuthenticationProvider;
import com.green.greenbook.domain.dto.MemberDto;
import com.green.greenbook.domain.dto.chat.ChatRoomRequest;
import com.green.greenbook.domain.dto.chat.ChatRoomDto;
import com.green.greenbook.service.ChatRoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final JwtAuthenticationProvider provider;

    private final String TOKEN_NAME = "X-AUTH-TOKEN";

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestHeader(name = TOKEN_NAME) String token,
                                  @RequestBody ChatRoomRequest request) {
        MemberDto dto = provider.getMemberDto(token);

        chatRoomService.create(request, dto.getId());

        return ResponseEntity.ok("create success");
    }

    @PostMapping("/{chatRoomId}")
    public ResponseEntity<?> join(@RequestHeader(name = TOKEN_NAME) String token,
                                     @PathVariable Long chatRoomId) {
        MemberDto dto = provider.getMemberDto(token);
        chatRoomService.join(chatRoomId, dto.getId());
        return ResponseEntity.ok("join success");
    }

    @GetMapping
    public List<ChatRoomDto> findAllRoom(Pageable pageable) {
        return chatRoomService.findAllRoom(pageable);
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<?> delete(@RequestHeader(name = TOKEN_NAME) String token,
        @PathVariable Long chatRoomId) {
        MemberDto dto = provider.getMemberDto(token);
        chatRoomService.delete(chatRoomId, dto.getId());
        return ResponseEntity.ok("delete success");
    }

}