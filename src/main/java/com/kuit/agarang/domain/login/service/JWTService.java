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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.kuit.agarang.global.common.model.dto.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class JWTService {

  private final JWTUtil jwtUtil;
  private final MemberRepository memberRepository;
  private final RefreshRepository refreshRepository;

  public ReissueDto reissueTokens(String refresh) {

    validateRefreshToken(refresh);

    String providerId = jwtUtil.getProviderId(refresh);
    String role = jwtUtil.getRole(refresh);
    Long memberId = jwtUtil.getMemberId(refresh);

    // AccessToken 생성 및 Refresh Rotate
    String newAccess = jwtUtil.createAccessToken(providerId, role, memberId);
    String newRefresh = jwtUtil.createRefreshToken(providerId, role, memberId);

    // Refresh Token Update
    Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));
    RefreshToken refreshToken = refreshRepository.findById(member.getRefreshToken().getId())
        .orElseThrow(() -> new BusinessException(NOT_FOUND_REFRESH_TOKEN));
    refreshToken.changeValue(newRefresh);

    return ReissueDto.builder()
        .newAccessToken(newAccess)
        .newRefreshToken(newRefresh)
        .providerId(providerId)
        .role(role)
        .build();
  }

  private void validateRefreshToken(String refresh) {

    if (refresh == null) {
      throw new BusinessException(NOT_FOUND_REFRESH_TOKEN);
    }

    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {
      refreshRepository.deleteByValue(refresh);
      throw new BusinessException(NOT_FOUND_REFRESH_TOKEN);
    }

    String category = jwtUtil.getCategory(refresh);
    if (!category.equals("refresh")) {
      throw new BusinessException(NOT_FOUND_REFRESH_TOKEN);
    }

    Boolean isExist = refreshRepository.existsByValue(refresh);
    if (!isExist) {
      throw new BusinessException(NOT_FOUND_REFRESH_TOKEN);
    }
  }
}
