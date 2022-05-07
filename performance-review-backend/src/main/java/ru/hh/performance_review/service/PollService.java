package ru.hh.performance_review.service;

import org.springframework.stereotype.Component;
import ru.hh.performance_review.dto.PollGetDtoResponse;
import ru.hh.performance_review.model.PollStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class PollService {


  public List<PollGetDtoResponse> getPolls(String user) {
    /*
    TODO:
    1. Взять из "user" роль user-а
    2. Если респондент - взять из "respondents_of_poll" id и status опросов для user-а и число респондентов,
      из "poll" name и deadline,
      из "content_of_poll" число вопросов.
    3. Если менеджер -
      из "poll" name и deadline,
      из "content_of_poll" число вопросов,
      взять из "respondents_of_poll" число респондентов и status опросов для всех user-ов -
      status исходя из логики:
      в процессе - если в процессе хотя бы у одного,
      завершен - если завершен у всех,
      закрыт, если истек дедлайн.
     */
    return Arrays.asList(
        new PollGetDtoResponse("First poll", LocalDate.of(2022, 04, 12), 7, 9, PollStatus.CLOSED),
        new PollGetDtoResponse("Second poll", LocalDate.of(2022, 05, 13), 10, 10, PollStatus.COMPLETED),
        new PollGetDtoResponse("Third poll", LocalDate.of(2022, 06, 10), 8, 8, PollStatus.PROGRESS),
        new PollGetDtoResponse("Fourth poll", LocalDate.of(2022, 07, 12), 10, 10, PollStatus.OPEN));
  }
}
