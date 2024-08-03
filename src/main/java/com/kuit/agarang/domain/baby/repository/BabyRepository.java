package com.kuit.agarang.domain.baby.repository;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BabyRepository extends JpaRepository<Baby, Long> {
  Optional<Baby> findByCode(String code);
}

