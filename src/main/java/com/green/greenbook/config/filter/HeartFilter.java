package com.green.greenbook.config.filter;

import com.green.greenbook.config.JwtAuthenticationProvider;
import com.green.greenbook.domain.dto.MemberDto;
import com.green.greenbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/heart/*")
@RequiredArgsConstructor
public class HeartFilter implements Filter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final MemberRepository memberRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("X-AUTH-TOKEN");

        if (!jwtAuthenticationProvider.validateToken(token)) {
            throw new ServletException("Invalid Access");
        }

        MemberDto dto = jwtAuthenticationProvider.getMemberDto(token);
        memberRepository.findByIdAndEmail(dto.getId(), dto.getEmail()).orElseThrow(
                () -> new ServletException("Invalid Aceess")
        );

        chain.doFilter(request, response);
    }
}