package ru.hh.performance_review.dto;

import lombok.AllArgsConstructor;
import ru.hh.performance_review.model.PoolStatus;

import java.time.LocalDate;

@AllArgsConstructor
public class PoolGetDtoResponse {
  private String title;
  private LocalDate deadline;
  private long questionsCount;
  private long respondentsCount;
  private PoolStatus status;
}
