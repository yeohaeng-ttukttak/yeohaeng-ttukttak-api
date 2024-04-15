package com.yeohangttukttak.api.domain.member.service;

import com.yeohangttukttak.api.domain.member.dao.MemberRepository;
import com.yeohangttukttak.api.domain.member.dao.RefreshTokenRepository;
import com.yeohangttukttak.api.domain.member.dto.SignInDTO;
import com.yeohangttukttak.api.domain.member.entity.Member;
import com.yeohangttukttak.api.domain.member.entity.RefreshToken;
import com.yeohangttukttak.api.global.common.ApiErrorCode;
import com.yeohangttukttak.api.global.common.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthRenewService {

    private final TokenService tokenService;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public SignInDTO renew (String refreshToken, String email) {
        // 1. 토큰의 Email로 Member ID(Seq)를 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ApiErrorCode.MEMBER_NOT_FOUND));

        // 2. 서버 저장소에 Refresh Token이 존재하는지 확인
        RefreshToken existToken = refreshTokenRepository.findById(member.getId())
                .orElseThrow(() -> new ApiException(ApiErrorCode.INVALIDED_AUTHORIZATION));

        // 3. 저장된 Refresh Token이 유효한지 확인
        if (!existToken.getToken().equals(refreshToken))
            throw new ApiException(ApiErrorCode.INVALIDED_AUTHORIZATION);

        Instant now = Instant.now();
        Long accessTokenTTL = 1800L;
        Long refreshTokenTTL = 3600 * 24 * 14L;

        String newAccessToken = tokenService.issue(email, now, accessTokenTTL);
        String newRefreshToken = tokenService.issue(email, now, refreshTokenTTL);

        refreshTokenRepository.save(new RefreshToken(member.getId(), newRefreshToken, refreshTokenTTL));

        return new SignInDTO(newAccessToken, newRefreshToken);
    }

}