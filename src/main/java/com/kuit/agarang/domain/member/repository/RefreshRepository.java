package com.kuit.agarang.domain.member.repository;

import com.kuit.agarang.domain.member.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshRepository extends JpaRepository<RefreshToken, Long> {

  Optional<RefreshToken> findByValue(String refreshTokenValue);
}
