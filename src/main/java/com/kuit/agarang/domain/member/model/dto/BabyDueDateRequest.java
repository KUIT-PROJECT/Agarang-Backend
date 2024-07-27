package com.kuit.agarang.domain.member.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class BabyDueDateRequest {
  private String providerId;
  private LocalDate date;
}
