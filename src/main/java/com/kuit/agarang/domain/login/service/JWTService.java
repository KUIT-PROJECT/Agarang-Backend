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

  public ReissueDto reissueTokens(Long memberId) {

    Member member = memberRepository.findByIdWithRefreshToken(memberId)
        .orElseThrow(() -> new BusinessException(NOT_FOUND_MEMBER));
    String refresh = member.getRefreshToken().getValue();

    // RefreshToken 유효성 검증
    validateRefreshToken(refresh);

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

  private void validateRefreshToken(String refresh) {

    if (refresh == null) {
      throw new BusinessException(NOT_FOUND_REFRESH_TOKEN);
    }

    try {
      jwtUtil.isExpired(refresh);
    } catch (ExpiredJwtException e) {
      throw new BusinessException(EXPIRED_REFRESH_TOKEN);
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
