package com.kuit.agarang.domain.member.repository;

import com.kuit.agarang.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Member findByProviderId(String providerId);
}
