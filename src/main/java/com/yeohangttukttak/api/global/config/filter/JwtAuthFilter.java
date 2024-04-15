package com.yeohangttukttak.api.global.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.dto.TokenPayload;
import com.yeohangttukttak.api.domain.member.exception.TokenExpiredException;
import com.yeohangttukttak.api.domain.member.exception.TokenInvalidException;
import com.yeohangttukttak.api.domain.member.service.TokenService;
import com.yeohangttukttak.api.global.common.ApiError;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.util.DecodeTokenExceptionHandler;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

import static com.yeohangttukttak.api.global.util.HttpMessageSerializer.serialize;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter implements Filter {

    private final String[] whitelist = {
            "/api/v1/members/sign-in", "/api/v1/members/sign-up", "/api/v1/members/renew"
    };

    private final TokenService tokenService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[JWT_AUTH_FILTER] init");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.info("[JWT_AUTH_FILTER] destroyed");
    }

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();

        // 1. Auth Filter 화이트 리스트 검사
        if (PatternMatchUtils.simpleMatch(whitelist, uri)) {
            chain.doFilter(request, response);
            return;
        }

        String token = parseToken(httpRequest);

        if (token == null) {
            serialize(httpResponse, SC_UNAUTHORIZED, new ApiError(
                    ApiErrorCode.AUTHORIZATION_REQUIRED, "인증 정보가 제공되지 않았습니다.", null));
            return;
        }

        try {
            TokenPayload payload = tokenService.decode(token);
            httpRequest.setAttribute("payload", payload);

            chain.doFilter(request, response);
        } catch (RuntimeException e) {
            DecodeTokenExceptionHandler.handle(e, httpRequest, httpResponse);
        }
    }

    private String parseToken(HttpServletRequest httpRequest) {
        String bearerToken = httpRequest.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) return null;

        return bearerToken.substring(7);
    }
}
