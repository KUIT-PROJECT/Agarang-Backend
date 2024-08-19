package com.kuit.agarang.domain.baby.repository;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BabyRepository extends JpaRepository<Baby, Long> {
  Optional<Baby> findByCode(String code);

  @Query("SELECT b FROM Baby b JOIN b.members m WHERE m.id = :memberId")
  Optional<Baby> findByMemberId(Long memberId);
}

