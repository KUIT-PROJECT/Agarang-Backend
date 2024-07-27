package com.kuit.agarang.domain.login.jwt;

import com.kuit.agarang.domain.login.model.dto.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Iterator;

public class AuthenticationUtil {

  public static String getProviderId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
    return customUserDetails.getProviderId();
  }

  public static String getRole() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();
    return auth.getAuthority();
  }
}
