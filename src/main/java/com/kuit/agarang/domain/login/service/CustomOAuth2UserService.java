package com.kuit.agarang.domain.login.service;

import com.kuit.agarang.domain.login.model.dto.*;
import com.kuit.agarang.domain.member.model.dto.MemberDTO;
import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User oAuth2User = super.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    OAuth2Response oAuth2Response = null;

    if (registrationId.equals("naver")) {
      oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
    } else if (registrationId.equals("kakao")) {
      oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
    } else if (registrationId.equals("google")) {
      oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
    } else {
      return null;
    }

    String providerId = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
    Member existData = memberRepository.findByProviderId(providerId);

    if (existData == null) {

      Member member = Member.builder().
          providerId(providerId)
          .name(oAuth2Response.getName())
          .email(oAuth2Response.getEmail())
          .role("ROLE_USER").build();

      memberRepository.save(member);

      MemberDTO memberDTO = new MemberDTO();
      memberDTO.setProviderId(providerId);
      memberDTO.setName(oAuth2Response.getName());
      memberDTO.setRole("ROLE_USER");

      return new CustomOAuth2User(memberDTO);

    } else {

      existData.changeInfo(oAuth2Response.getName(), oAuth2Response.getEmail());
      memberRepository.save(existData);

      MemberDTO memberDTO = new MemberDTO();
      memberDTO.setProviderId(existData.getProviderId());
      memberDTO.setName(oAuth2Response.getName());
      memberDTO.setRole(existData.getRole());

      return new CustomOAuth2User(memberDTO);
    }
  }
}
