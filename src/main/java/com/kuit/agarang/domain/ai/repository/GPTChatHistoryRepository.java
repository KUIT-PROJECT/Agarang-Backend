package com.kuit.agarang.domain.ai.repository;

import com.kuit.agarang.domain.ai.model.entity.GPTChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GPTChatHistoryRepository extends JpaRepository<GPTChatHistory, String> {
}
