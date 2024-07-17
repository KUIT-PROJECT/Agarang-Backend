package com.kuit.agarang.domain.memory.repository;

import com.kuit.agarang.domain.baby.model.entity.Baby;
import com.kuit.agarang.domain.memory.model.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MemoryRepository extends JpaRepository<Memory,Long> {
//  @Query("SELECT mm FROM Memory mm JOIN MemoryBookmark mbm WHERE Date(mm.createdAt) = :date AND mm.baby.id = :babyId")
  @Query("SELECT mm, " +
          "CASE WHEN mbm.id IS NOT NULL THEN true ELSE false END as bookmarked " +
          "FROM Memory mm " +
          "LEFT JOIN MemoryBookmark mbm ON mm.id = mbm.memory.id " +
          "WHERE DATE(mm.createdAt) = :date AND mm.baby = :baby " +
          "ORDER BY mm.createdAt DESC")
  List<Object[]> findByMemoriesByDateAndBabyOrdeOrderByCreatedAtDesc(LocalDate date, Baby baby);

  @Query("SELECT m.imageUrl FROM Memory m WHERE Date(m.createdAt) >= :startDate AND Date(m.createdAt) <= :endDate")
  List<String> findImageUrlsByDate(LocalDate startDate, LocalDate endDate);

  List<Memory> findByBabyOrderByCreatedAtDesc(Baby baby);
  List<Memory> findByBaby(Baby baby);
}
