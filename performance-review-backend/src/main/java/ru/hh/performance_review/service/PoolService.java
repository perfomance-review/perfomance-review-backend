package ru.hh.performance_review.service;

import org.springframework.stereotype.Component;
import ru.hh.performance_review.dto.PoolGetDtoResponse;
import ru.hh.performance_review.model.PoolStatus;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class PoolService {


  public List<PoolGetDtoResponse> getPools(String user) {
    /*
    TODO:
    1. Взять из "user" роль user-а
    2. Если респондент - взять из "respondents_of_poll" id и status опросов для user-а и число респондентов,
      из "pool" name и deadline,
      из "content_of_poll" число вопросов.
    3. Если менеджер -
      из "pool" name и deadline,
      из "content_of_poll" число вопросов,
      взять из "respondents_of_poll" число респондентов и status опросов для всех user-ов -
      status исходя из логики:
      в процессе - если в процессе хотя бы у одного,
      завершен - если завершен у всех,
      закрыт, если истек дедлайн.
     */
    return Arrays.asList(
        new PoolGetDtoResponse("First pool", LocalDate.of(2022, 04, 12), 7, 9, PoolStatus.CLOSED),
        new PoolGetDtoResponse("Second pool", LocalDate.of(2022, 05, 13), 10, 10, PoolStatus.COMPLETED),
        new PoolGetDtoResponse("Third pool", LocalDate.of(2022, 06, 10), 8, 8, PoolStatus.PROGRESS),
        new PoolGetDtoResponse("Fourth pool", LocalDate.of(2022, 07, 12), 10, 10, PoolStatus.OPEN));
  }
}
