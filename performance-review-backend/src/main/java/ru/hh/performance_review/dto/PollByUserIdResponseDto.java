package ru.hh.performance_review.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.hh.performance_review.model.PollStatus;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class PollByUserIdResponseDto {
  private UUID pollId;
  private String title;
  private String description;
  @JsonIgnore
  private LocalDate createdDt;
  private LocalDate deadline;
  private long questionsCount;
  private long respondentsCount;
  private PollStatus status;
}
