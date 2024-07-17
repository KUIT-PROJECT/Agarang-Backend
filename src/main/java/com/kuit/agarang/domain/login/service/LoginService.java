package com.kuit.agarang.domain.login.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuit.agarang.domain.login.model.entitiy.CustomOAuth2User;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService extends DefaultOAuth2UserService {

  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

    OAuth2User oAuth2Member = super.loadUser(request);
    String oauthClientName = request.getClientRegistration().getClientName();

    try {
      log.info(new ObjectMapper().writeValueAsString(oAuth2Member.getAttributes()));
    } catch (Exception e) {
      e.printStackTrace();
    }

    Member member = null;
    String oauthId = null;

    if (oauthClientName.equals("kakao")) {
      oauthId = "kakao_" + oAuth2Member.getAttributes().get("id");
      member = Member.builder()
          .oauthId(oauthId)
          .build();
    }

    // TODO: Google 로그인 추가

    memberRepository.save(member);

    return new CustomOAuth2User(oauthId);
  }
}
