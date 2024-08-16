package com.kuit.agarang.domain.login.service;

import com.kuit.agarang.domain.login.model.dto.ReissueDto;
import com.kuit.agarang.domain.login.utils.JWTUtil;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.model.entity.RefreshToken;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import com.kuit.agarang.domain.member.repository.RefreshRepository;
import com.kuit.agarang.global.common.exception.exception.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class JWTService {

  private final JWTUtil jwtUtil;
  private final MemberRepository memberRepository;
  private final RefreshRepository refreshRepository;

  public ReissueDto reissueTokens(String refresh) {

    // RefreshToken 유효성 검증
    Member member = validateRefreshToken(refresh);

    // AccessToken 생성 및 Refresh Rotate
    String newAccess = jwtUtil.createAccessToken(member.getProviderId(), member.getRole(), member.getId());
    String newRefresh = jwtUtil.createRefreshToken(member.getProviderId(), member.getRole(), member.getId());

    // Refresh Token Update
    member.getRefreshToken().setValue(newRefresh);

    return ReissueDto.builder()
        .newAccessToken(newAccess)
        .newRefreshToken(newRefresh)
        .providerId(member.getProviderId())
        .role(member.getRole())
        .build();
  }

  private Member validateRefreshToken(String refresh) {
    // not found or null
    RefreshToken refreshToken = refreshRepository.findByValue(refresh)
      .orElseThrow(() -> new BusinessException(NOT_FOUND_REFRESH_TOKEN));

    Member member = memberRepository.findByRefreshToken(refreshToken)
      .orElseThrow(() -> {
        deleteRefreshToken(refreshToken); // 탈취 가능성
        return new BusinessException(NOT_FOUND_REFRESH_TOKEN);
      });

    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {
      throw new BusinessException(EXPIRED_REFRESH_TOKEN);
    }

    String category = jwtUtil.getCategory(refresh);
    if (!category.equals("refresh")) {
      throw new BusinessException(INVALID_REFRESH_TOKEN);
    }

    return member;
  }

  private void deleteRefreshToken(RefreshToken refreshToken) {
    refreshRepository.delete(refreshToken);
    memberRepository.deleteByRefreshToken(refreshToken);
  }
}
