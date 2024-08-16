package com.kuit.agarang.domain.member.repository;

import com.kuit.agarang.domain.member.model.entity.Member;
import com.kuit.agarang.domain.member.model.entity.RefreshToken;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByProviderId(String providerId);

  @Query("SELECT m FROM Member m JOIN FETCH m.baby WHERE m.id = :memberId")
  Optional<Member> findByIdWithBaby(@Param("memberId") Long memberId);

  @Query("SELECT m FROM Member m JOIN FETCH Baby b ON m.baby = b WHERE m.id = :memberId")
  Optional<Member> findByIdFetchJoinBaby(@Param("memberId") Long memberId);

  @Query("SELECT m FROM Member m JOIN FETCH m.refreshToken WHERE m.id = :memberId")
  Optional<Member> findByIdWithRefreshToken(@Param("memberId") Long memberId);

  void deleteByRefreshToken(RefreshToken refreshToken);

  Optional<Member> findByRefreshToken(RefreshToken refreshToken);
}
