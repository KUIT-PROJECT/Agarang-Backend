package com.kuit.agarang.domain.baby.repository;

import com.kuit.agarang.domain.baby.model.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {

}
