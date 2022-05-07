package ru.hh.performance_review.dto;

import lombok.AllArgsConstructor;
import ru.hh.performance_review.model.PollStatus;

import java.time.LocalDate;

@AllArgsConstructor
public class PollGetDtoResponse {
  private String title;
  private LocalDate deadline;
  private long questionsCount;
  private long respondentsCount;
  private PollStatus status;
}
