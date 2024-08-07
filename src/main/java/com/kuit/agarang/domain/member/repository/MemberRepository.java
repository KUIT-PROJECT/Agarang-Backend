package com.kuit.agarang.domain.member.repository;

import com.kuit.agarang.domain.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


  Optional<Member> findByProviderId(String providerId);

  @Modifying
  @Query("UPDATE Member m SET m.familyRole = :familyRole WHERE m.id = :id")
  void updateFamilyRoleById(Long id, String familyRole);
}
